package org.openhealthtools.openatna.syslog.protocol;

import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * generates RFC 5424 messages from a stream.
 *
 * @author Andrew Harrison
 */
public class ProtocolMessageFactory extends SyslogMessageFactory {

    public static final char VERSION_CHAR = '1';
    public static final Pattern DATE = Pattern.compile(
            "(\\d{4})-(\\d{2})-(\\d{2})[T](\\d{2}):(\\d{2}):(\\d{2})(\\.(\\d{1,6}))?([+-](\\d{2}):(\\d{2}))?([Z])?");
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolMessageFactory.class);

    /**
     * Parse the serialized string form into a java.util.Date
     * NOTE: This only supports millisecond precision. decimals greater than 3 are truncated
     *
     * @param date The serialized string form of the date
     * @return The created java.util.Date
     */
    public static Date createDate(String date) throws SyslogException {

        Matcher m = DATE.matcher(date);
        if (m.find()) {

            LOGGER.debug("date groups{" + m.group(1) + "}{" + m.group(2) + "}{" + m.group(3) + "}{" +
                    m.group(4) + "}{" + m.group(5) + "}{" + m.group(6) + "}{" +
                    m.group(7) + "}{" + m.group(8) + "}{" + m.group(9) + "}{" +
                    m.group(10) + "}{" + m.group(11) + "}{" + m.group(12) + "}");
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            int hoff = 0;
            int moff = 0;
            int doff;
            if (m.group(9) != null) {
                doff = m.group(9).equals("-") ? 1 : -1;
                hoff = doff * (m.group(10) != null ? Integer.parseInt(m.group(10)) : 0);
                moff = doff * (m.group(11) != null ? Integer.parseInt(m.group(11)) : 0);
            } else {
                /*
                TODO
                if (m.group(12) == null) {
                    throw new SyslogException("Invalid Date Format");
                }*/
            }
            c.set(Calendar.YEAR, Integer.parseInt(m.group(1)));
            c.set(Calendar.MONTH, m.group(2) != null ? Integer.parseInt(m.group(2)) - 1 : 0);
            c.set(Calendar.DATE, m.group(3) != null ? Integer.parseInt(m.group(3)) : 1);
            c.set(Calendar.HOUR_OF_DAY, m.group(4) != null ? Integer.parseInt(m.group(4)) + hoff : 0);
            c.set(Calendar.MINUTE, m.group(5) != null ? Integer.parseInt(m.group(5)) + moff : 0);
            c.set(Calendar.SECOND, m.group(6) != null ? Integer.parseInt(m.group(6)) : 0);
            String millis = m.group(8);
            if (millis == null) {
                millis = "0";
            }
            if (millis.length() > 3) {
                millis = millis.substring(0, 3);
            }
            c.set(Calendar.MILLISECOND, Integer.parseInt(millis));
            return c.getTime();
        } else {
            throw new SyslogException("Invalid Date Format");
        }
    }

    /**
     * Create the serialized string form from a java.util.Date
     *
     * @param date A java.util.Date
     * @return The serialized string form of the date
     */
    public static String formatDate(Date date) {

        StringBuilder stringBuilder = new StringBuilder();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.setTime(date);
        stringBuilder.append(c.get(Calendar.YEAR));
        stringBuilder.append('-');
        int f = c.get(Calendar.MONTH);
        if (f < 9) {
            stringBuilder.append('0');
        }
        stringBuilder.append(f + 1);
        stringBuilder.append('-');
        f = c.get(Calendar.DATE);
        if (f < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(f);
        stringBuilder.append('T');
        f = c.get(Calendar.HOUR_OF_DAY);
        if (f < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(f);
        stringBuilder.append(':');
        f = c.get(Calendar.MINUTE);
        if (f < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(f);
        stringBuilder.append(':');
        f = c.get(Calendar.SECOND);
        if (f < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(f);
        stringBuilder.append('.');
        f = c.get(Calendar.MILLISECOND);
        if (f < 100) {
            stringBuilder.append('0');
        }
        if (f < 10) {
            stringBuilder.append('0');
        }
        stringBuilder.append(f);
        stringBuilder.append('Z');
        return stringBuilder.toString();
    }

    /**
     * reads the priority value and positions the stream at the space after the version number.
     * This reads up to 9 characters to read the priority and version and the following space.
     *
     * @param in
     * @return
     * @throws org.openhealthtools.openatna.syslog.SyslogException
     */
    private int readPriority(InputStream in) throws SyslogException {
        try {
            int max = 9;
            int count = 0;
            String pri = "";
            boolean afterOpen = false;
            while (count < max) {
                char c = (char) in.read();
                count++;
                switch (c) {
                    case '<':
                        afterOpen = true;
                        break;
                    case '>':
                        int priority = Integer.parseInt(pri);
                        if (!afterOpen || priority < 0 || priority > 191) {
                            throw new SyslogException("syntax error");
                        }
                        c = (char) in.read(); // read version
                        count++;
                        if (c != ProtocolMessageFactory.VERSION_CHAR) {
                            throw new SyslogException("unsupported version");
                        }
                        c = (char) in.read(); // read space
                        count++;
                        if (c != ' ') {
                            throw new SyslogException("not a space");
                        }
                        return priority;
                    default:
                        if (afterOpen) {
                            pri += c;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            throw new SyslogException(e);
        }
        throw new SyslogException("too many characters");
    }

    /**
     * This reads up to 256 characters to read headers (excluding SDs). This limit is arbitrary.
     * It is imposed to reduce the risk of badly formed or malicious messages from using too many resources.
     *
     * @param in
     * @return
     * @throws SyslogException
     */
    public SyslogMessage read(InputStream in) throws SyslogException {
        try {
            PushbackInputStream pin = new PushbackInputStream(in, 5);
            int priority = readPriority(pin);
            int facility;
            int severity;
            byte c;
            int spaces = 5;
            int count = 0;
            ByteBuffer buff = ByteBuffer.wrap(new byte[256]);

            String timestamp = null;
            String host = "-";
            String app = "-";
            String proc = "-";
            String mid = "-";
            int max = 256;
            int curr = 0;

            while (count < spaces && curr < max) {
                c = (byte) pin.read();
                curr++;
                if (c == ' ') {
                    count++;
                    String currHeader = new String(buff.array(), 0, buff.position(), StandardCharsets.UTF_8);
                    LOGGER.debug("CurrHeader: '{}'", currHeader);
                    buff.clear();
                    switch (count) {
                        case 1:
                            timestamp = currHeader;
                            break;
                        case 2:
                            host = currHeader;
                            break;
                        case 3:
                            app = currHeader;
                            break;
                        case 4:
                            proc = currHeader;
                            break;
                        case 5:
                            mid = currHeader;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + count);
                    }
                } else {
                    buff.put(c);
                }
            }
            if (timestamp == null) {
                throw new SyslogException("no timestamp defined");
            }

            c = (byte) pin.read();
            List<StructuredElement> els = new ArrayList<>();
            if (c == '-') {
                c = (byte) pin.read();
                if (c != ' ') {
                    throw new SyslogException("not a space");
                }
            } else if (c == '[') {
                pin.unread(c);
                els = StructuredElement.parse(pin);
            } else {
                throw new SyslogException("Illegal Structured data");
            }
            LogMessage logMessage = getLogMessage(mid);
            String encoding = readBom(pin, logMessage.getExpectedEncoding());
            logMessage.read(pin, encoding);
            facility = priority / 8;
            severity = priority % 8;

            ProtocolMessage sm = new ProtocolMessage(facility, severity, timestamp, host, logMessage, app, mid, proc);
            for (StructuredElement el : els) {
                sm.addStructuredElement(el);
            }
            return sm;
        } catch (IOException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
            throw new SyslogException(e);
        }
    }
}
