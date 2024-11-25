package org.openhealthtools.openatna.audit.log;

import org.openhealthtools.openatna.anom.AtnaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class logs errors at the ATNA message parsing layer.
 *
 * @author Andrew Harrison
 */
public class AtnaErrorLogger {

    private static List<ErrorHandler<AtnaException>> handlers = new ArrayList<>();

    private AtnaErrorLogger() {
    }

    @SuppressWarnings("unused")
    public static void addErrorHandler(ErrorHandler<AtnaException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(AtnaException e) {
        for (ErrorHandler<AtnaException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(AtnaException e) {

        Logger logger = LoggerFactory.getLogger("ATNA.ATNA_ERROR_LOG");
        invokeHandlers(e);

        logger.error("===> ATNA EXCEPTION THROWN\n** ATNA ERROR: {} **", e.getError(), e);
    }
}
