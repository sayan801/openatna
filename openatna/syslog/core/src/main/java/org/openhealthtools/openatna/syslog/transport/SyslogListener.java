package org.openhealthtools.openatna.syslog.transport;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;

/**
 * Implementations of this class receive notification when a message has arrived.
 *
 * @author Andrew Harrison
 */
public interface SyslogListener<M> {

    void messageArrived(SyslogMessage<M> message);

    void exceptionThrown(SyslogException exception);
}
