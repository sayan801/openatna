package org.openhealthtools.openatna.syslog.bsd;

import org.openhealthtools.openatna.syslog.*;
import org.openhealthtools.openatna.syslog.message.StringLogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class BsdMessage<M> extends SyslogMessage {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BsdMessage.class);
    private static final long serialVersionUID = -4985687066173047860L;
    private String tag;

    public BsdMessage(int facility, int severity, String timestamp, String hostName, LogMessage<M> message, String tag) {
        super(facility, severity, timestamp, hostName, message);
        if (tag != null && tag.length() == 0) {
            tag = null;
        }
        this.tag = tag;
    }

    public BsdMessage(int facility, int severity, String hostName, LogMessage<M> message, String tag) {
        this(facility, severity, BsdMessageFactory.createDate(new Date()), hostName, message, tag);

    }

    public BsdMessage(int facility, int severity, String timestamp, String hostName, LogMessage<M> message) {
        this(facility, severity, timestamp, hostName, message, null);
    }

    public static void main(String[] args) {

        try {

            @SuppressWarnings("squid:S1313")
            BsdMessage m = new BsdMessage(10, 5, "Oct  1 22:14:15", "127.0.0.1", new StringLogMessage("Don't panic"), "ATNALOG");
            SyslogMessageFactory.registerLogMessage("ATNALOG", StringLogMessage.class);
            SyslogMessageFactory.setFactory(new BsdMessageFactory());
            String s = m.toString();
            LOGGER.info(s);
            SyslogMessage s2 = SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(s2.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Exception: '{}'", e.getMessage(), e);
        }
    }

    private String getHeader() {

        StringBuilder sb = new StringBuilder();
        sb.append("<").append(getFacility() * 8 + getSeverity()).append(">")
                .append(getTimestamp()).append(" ")
                .append(getHostName()).append(" ");
        if (tag != null) {
            sb.append(tag);
        }
        return sb.toString();
    }

    public void write(OutputStream out) throws SyslogException {

        try {
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(getHeader());
            writer.flush();
            getMessage().write(out);
            writer.flush();
        } catch (IOException e) {
            throw new SyslogException(e);
        }
    }

    public byte[] toByteArray() throws SyslogException {

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(getHeader().getBytes(StandardCharsets.UTF_8));
            getMessage().write(bout);
            return bout.toByteArray();
        } catch (IOException e) {
            throw new SyslogException(e);
        }
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getHeader());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            getMessage().write(bout);
        } catch (SyslogException e) {
            LOGGER.error("SyslogException: '{}'", e.getMessage(), e);
            assert false;
        }
        try {
            sb.append(new String(bout.toByteArray(), getMessage().getExpectedEncoding()));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException: '{}'", e.getMessage(), e);
            assert false;
        }
        return sb.toString();
    }

    public String getTag() {
        return tag;
    }
}
