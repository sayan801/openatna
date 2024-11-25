package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaIOFactory;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A wrapper for an AtnaIOFactory that ties in with Syslog.
 * Designed for subclasses to specify a Factory meaning the log message can have a default constructor, and one taking
 * just an AtnaMessage:
 * <p/>
 * e.g.:
 * <p/>
 * class MyLogMessage extends AtnaLogMessage {
 * public MyLogMessage() {
 * super(new MyIOFactory());
 * }
 * }
 * <p/>
 * class MyLogMessage extends AtnaLogMessage {
 * public MyLogMessage(AtnaMEssage msg) {
 * super(msg, new MyIOFactory());
 * }
 * }
 *
 * @author Andrew Harrison
 */
public abstract class AtnaLogMessage implements LogMessage<AtnaMessage> {

    private static final long serialVersionUID = -3082789906388081897L;
    private AtnaMessage message;
    private AtnaIOFactory factory;

    public AtnaLogMessage(AtnaIOFactory factory) {
        this.factory = factory;
    }

    public AtnaLogMessage(AtnaMessage message, AtnaIOFactory factory) {
        this.message = message;
        this.factory = factory;
    }

    public String getExpectedEncoding() {
        return Constants.ENC_UTF8;
    }

    public void read(InputStream in, String encoding) throws SyslogException {
        try {
            message = factory.read(in);
        } catch (AtnaException e) {
            throw new SyslogException(e.getMessage(), e);
        }
    }

    public void write(OutputStream out) throws SyslogException {
        if (getMessageObject() == null) {
            throw new SyslogException("no AtnaMessage to write out.");
        }
        try {
            factory.write(getMessageObject(), out);
        } catch (AtnaException e) {
            throw new SyslogException(e.getMessage(), e);
        }
    }

    public AtnaMessage getMessageObject() {
        return message;
    }
}
