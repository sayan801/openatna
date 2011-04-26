package org.openhealthtools.openatna.syslog.mina;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 28, 2010
 */
public interface Notifier {

    public void notifyMessage(SyslogMessage msg);

    public void notifyException(SyslogException e);

}
