package org.openhealthtools.openatna.syslog;

import java.util.logging.Logger;


public class SyslogException extends Exception {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.SyslogException");


    public SyslogException(String s) {
        super(s);
        log.warning("Error -->" + getMessage());

    }

    public SyslogException(String s, Throwable throwable) {
        super(s, throwable);
        log.warning("Error -->" + getMessage());

    }

    public SyslogException(Throwable throwable) {
        super(throwable);
        log.warning("Error -->" + throwable.getMessage());

    }

}
