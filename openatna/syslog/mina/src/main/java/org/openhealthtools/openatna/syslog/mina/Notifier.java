package org.openhealthtools.openatna.syslog.mina;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;

public interface Notifier {

    void notifyMessage(SyslogMessage msg);

    void notifyException(SyslogException e);
}
