package org.openhealthtools.openatna.audit.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class logs errors that are not specific to ATNA
 *
 * @author Andrew Harrison
 */
public class ErrorLogger {


    private static List<ErrorHandler<Throwable>> handlers = new ArrayList<>();

    private ErrorLogger() {
    }

    @SuppressWarnings("unused")
    public static void addErrorHandler(ErrorHandler<Throwable> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(Throwable e) {
        for (ErrorHandler<Throwable> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(Throwable e) {

        Logger logger = LoggerFactory.getLogger("ATNA.ERROR_LOG");
        invokeHandlers(e);

        logger.error("===> EXCEPTION THROWN\n** ERROR: {} **", e.getClass().getName(), e);
    }
}
