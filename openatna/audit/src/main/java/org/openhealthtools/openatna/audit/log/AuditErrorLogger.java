package org.openhealthtools.openatna.audit.log;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class AuditErrorLogger {

    private static List<ErrorHandler<AuditException>> handlers = new ArrayList<>();

    private AuditErrorLogger() {
    }

    @SuppressWarnings("unused")
    public static void addErrorHandler(ErrorHandler<AuditException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(AuditException e) {
        for (ErrorHandler<AuditException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(AuditException e) {

        Logger logger = LoggerFactory.getLogger("ATNA.AUDIT_ERROR_LOG");
        invokeHandlers(e);

        AuditException.AuditError error = e.getError();
        AtnaMessage msg = e.getAtnaMessage();
        if (msg == null) {
            logger.error("===> ATNA EXCEPTION THROWN\n** AUDIT ERROR: {}**\nno message available.", error, e);
        } else {
            logger.error("===> ATNA EXCEPTION THROWN\n** AUDIT ERROR: {}**\nmessage is:\n{}", error, msg, e);
        }
    }
}
