package org.openhealthtools.openatna.audit.log;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Logs errors at the syslog level
 *
 * @author Andrew Harrison
 */
public class SyslogErrorLogger {
    
    private static List<ErrorHandler<SyslogException>> handlers = new ArrayList<>();

    private SyslogErrorLogger() {
    }

    @SuppressWarnings("unused")
    public static void addErrorHandler(ErrorHandler<SyslogException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(SyslogException e) {
        for (ErrorHandler<SyslogException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(SyslogException e) {

        Logger logger = LoggerFactory.getLogger("ATNA.SYSLOG_ERROR_LOG");
        invokeHandlers(e);

        byte[] bytes = e.getBytes();
        if (bytes.length == 0) {
            logger.error("===> SYSLOG EXCEPTION THROWN\nno bytes available.");
        } else {
            try {
                if (logger.isErrorEnabled()) {
                    logger.error("===> SYSLOG EXCEPTION THROWN\nbytes are:\n{}", new String(bytes, StandardCharsets.UTF_8.name()));
                }
            } catch (UnsupportedEncodingException e1) {
                logger.error("Unsupported encoding exception occurred", e1);
            }
        }
    }
}
