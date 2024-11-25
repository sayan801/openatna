package org.openhealthtools.openatna.syslog;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Interface definition of read and write methods for application specific message.
 * It is the responsibility of the LogMessage to include (or not) a UTF BOM, if writing 5424 messages.
 * SyslogMessageFactory contains write&ltUTF-encoding>Bom(OutputStream) methods that can be invoked before writing
 * the message to the stream, if a UTF BOM is desired. <p/>
 * SyslogMessage tries to determine the encoding of incoming stream and defaults to the expected encoding of the LogMessage
 *
 * @author Andrew Harrison
 */
public interface LogMessage<M extends Object> extends Serializable {

    String getExpectedEncoding();

    void read(InputStream in, String encoding) throws SyslogException;

    void write(OutputStream out) throws SyslogException;

    M getMessageObject();
}
