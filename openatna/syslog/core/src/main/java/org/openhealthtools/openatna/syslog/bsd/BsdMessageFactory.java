package org.openhealthtools.openatna.syslog.bsd;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Reads in data and creates BSD style syslog messages
 *
 * @author Andrew Harrison
 */
public class BsdMessageFactory extends SyslogMessageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BsdMessageFactory.class);
    private static final String DATE_FORMAT = "MMM d HH:mm:ss";
    private static final String SINGLE_DATE_FORMAT = "MMM  d HH:mm:ss";

    public static String createDate(Date date) {

        Calendar c = new GregorianCalendar(TimeZone.getDefault());
        c.setTime(date);
        if (c.get(Calendar.DATE) > 9) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            return format.format(date);
        } else {
            SimpleDateFormat singleDateFormat = new SimpleDateFormat(SINGLE_DATE_FORMAT);
            return singleDateFormat.format(date);
        }
    }

    public static Date formatDate(String date) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.parse(date);
    }

    /**
     * Reads the priority value and positions the stream at the space after greater than sign.
     * This reads up to 5 characters to read the priority and the following space.
     *
     * @param inputStream
     * @return
     * @throws org.openhealthtools.openatna.syslog.SyslogException
     */
    private int readPriority(InputStream inputStream) throws SyslogException {
        try {
            int max = 5;
            int count = 0;
            StringBuilder pri = new StringBuilder();
            boolean afterOpen = false;
            while (count < max) {
                char c = (char) inputStream.read();
                count++;
                switch (c) {
                    case '<':
                        afterOpen = true;
                        break;
                    case '>':
                        int priority = Integer.parseInt(pri.toString());
                        if (!afterOpen || priority < 0 || priority > 191) {
                            throw new SyslogException("syntax error");
                        }
                        return priority;
                    default:
                        if (afterOpen) {
                            pri.append(c);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            throw new SyslogException(e);
        }
        throw new SyslogException("too many characters");
    }


    public SyslogMessage read(InputStream in) throws SyslogException {

        try {
            PushbackInputStream pin = new PushbackInputStream(in, 5);
            int priority = readPriority(pin);
            int facility;
            int severity;
            byte c;
            int spaces = 4;
            int count = 0;
            boolean spaceBefore = false;
            ByteBuffer buff = ByteBuffer.wrap(new byte[256]);

            String timestamp;
            String month = null;
            String date = null;
            String time = null;
            String host = "";
            int max = 256;
            int curr = 0;

            while (count < spaces && curr < max) {
                c = (byte) pin.read();
                curr++;
                if (c == ' ') {
                    if (!spaceBefore) {
                        count++;
                        String currHeader = new String(buff.array(), 0, buff.position(), StandardCharsets.UTF_8);
                        buff.clear();
                        switch (count) {
                            case 1:
                                month = currHeader;
                                break;
                            case 2:
                                date = currHeader;
                                break;
                            case 3:
                                time = currHeader;
                                break;
                            case 4:
                                host = currHeader;
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + count);
                        }
                    }
                    spaceBefore = true;

                } else {
                    spaceBefore = false;
                    buff.put(c);
                }
            }
            if (month == null || date == null || time == null) {
                timestamp = createDate(new Date());
            } else {
                String gap = " ";
                if (date.length() == 1) {
                    gap = "  ";
                }
                timestamp = (month + gap + date + " " + time);
                try {
                    formatDate(timestamp);
                } catch (Exception e) {
                    LOGGER.debug("Exception: '{}'", e.getMessage(), e);
                    timestamp = createDate(new Date());
                }
            }
            String tag = null;
            int tagLen = 32;
            buff.clear();
            for (int i = 0; i < tagLen; i++) {
                c = (byte) pin.read();
                curr++;
                if (!Character.isLetterOrDigit((char) (c & 0xff))) {
                    pin.unread(c);
                    break;
                }
                buff.put(c);
            }
            if (buff.position() > 0) {
                tag = new String(buff.array(), 0, buff.position(), StandardCharsets.UTF_8);
            }

            LogMessage logMessage = getLogMessage(tag);
            String encoding = readBom(pin, logMessage.getExpectedEncoding());
            logMessage.read(pin, encoding);
            facility = priority / 8;
            severity = priority % 8;
            return new BsdMessage(facility, severity, timestamp, host, logMessage, tag);

        } catch (IOException e) {

            LOGGER.error("IOException: '{}'", e.getMessage(), e);
            throw new SyslogException(e);
        }
    }
}
