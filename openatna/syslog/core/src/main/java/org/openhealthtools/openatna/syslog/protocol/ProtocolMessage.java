package org.openhealthtools.openatna.syslog.protocol;

import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RFC 5424 Syslog message format implementation.
 *
 * @author Andrew Harrison
 */
public class ProtocolMessage<M> extends SyslogMessage {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolMessage.class);
    private static final long serialVersionUID = 4839446464986500200L;

    private String appName = "-";
    private String messageId = "-";
    private String procId = "-";
    private List<StructuredElement> structuredElement = new ArrayList<>();

    public ProtocolMessage(int facility, int severity, String timestamp, String hostName, LogMessage<M> message,
                           String appName, String messageId, String procId) throws SyslogException {
        super(facility, severity, timestamp, hostName, message);
        if (timestamp == null) {
            timestamp = "-";
        }
        if (!timestamp.equals("-")) {
            ProtocolMessageFactory.createDate(timestamp);
        }
        this.appName = appName;
        this.messageId = messageId;
        this.procId = procId;
    }

    public ProtocolMessage(int facility, int severity, String hostName, LogMessage<M> message, String appName, String messageId, String procId)
            throws SyslogException {
        this(facility, severity, ProtocolMessageFactory.formatDate(new Date()), hostName, message, appName, messageId, procId);
    }

    public ProtocolMessage(int priority, String hostName, LogMessage<M> message, String appName, String messageId) throws SyslogException {
        this(priority / 8, priority % 8, ProtocolMessageFactory.formatDate(new Date()), hostName, message, appName, messageId, "-");
    }

    public String getProcId() {
        return procId;
    }

    public String getAppName() {
        return appName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void addStructuredElement(StructuredElement element) {
        structuredElement.add(element);
    }

    public void removeStructuredElement(StructuredElement element) {
        structuredElement.remove(element);
    }

    private String getHeader() {

        return "<" + (getFacility() * 8 + getSeverity()) + ">" +
                ProtocolMessageFactory.VERSION_CHAR + " " +
                getTimestamp() + " " +
                getHostName() + " " +
                appName + " " +
                procId + " " +
                messageId + " ";
    }

    public void write(OutputStream out) throws SyslogException {

        try {
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(getHeader());
            if (!structuredElement.isEmpty()) {
                for (StructuredElement element : structuredElement) {
                    writer.write(element.toString());
                }
            } else {
                writer.write("-");
            }
            writer.write(" ");
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
            getMessage().write(bout);
            Document dom = XMLUtil.parseContent(bout.toString());
            String result = XMLUtil.prettyPrint(dom.getDocumentElement());
            return result.getBytes();
        } catch (Exception e) {
            logger.error("{}: '{}'", e.getClass(), e.getMessage(), e);
            throw new SyslogException(e);
        }
    }

    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHeader());
        if (!structuredElement.isEmpty()) {
            for (StructuredElement element : structuredElement) {
                stringBuilder.append(element.toString());
            }
        } else {
            stringBuilder.append("-");
        }
        stringBuilder.append(" ");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            getMessage().write(bout);
        } catch (SyslogException e) {
            assert false;
        }
        try {
            stringBuilder.append(new String(bout.toByteArray(), getMessage().getExpectedEncoding()));
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
        return stringBuilder.toString();
    }
}
