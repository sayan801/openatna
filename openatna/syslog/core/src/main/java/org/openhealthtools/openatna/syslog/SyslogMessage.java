package org.openhealthtools.openatna.syslog;


import java.io.OutputStream;
import java.io.Serializable;

/**
 * Super class for SyslogMessage implementations
 *
 * @author Andrew Harrison
 */
public abstract class SyslogMessage<M> implements Serializable {

    private static final long serialVersionUID = 8166263646197250842L;
    private int facility;
    private int severity;
    private String timestamp;
    private String hostName;
    private String sourceIp;
    private LogMessage<M> message;

    protected SyslogMessage(int facility, int severity, String timestamp, String hostName, LogMessage<M> message) {
        this.facility = facility;
        this.severity = severity;
        this.timestamp = timestamp;
        this.hostName = hostName;
        this.message = message;
    }

    public int getFacility() {
        return facility;
    }

    public int getSeverity() {
        return severity;
    }

    public String getHostName() {
        return hostName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public LogMessage<M> getMessage() {
        return message;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public abstract void write(OutputStream out) throws SyslogException;

    public abstract byte[] toByteArray() throws SyslogException;
}
