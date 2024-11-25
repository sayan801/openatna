package org.openhealthtools.openatna.web;

import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

public class OpenATNAServiceLoaderListener implements ApplicationContextAware {

    private static AuditService service = null;
    private final Logger logger = LoggerFactory.getLogger(OpenATNAServiceLoaderListener.class);
    private ApplicationContext context;

    public synchronized void start() {

        if (service == null) {
            logger.info("[ATNA Service] Starting OpenATNA service...");

            AtnaFactory.initialize(context);

            service = (AuditService) context.getBean("auditService");
            try {
                service.start();
            } catch (Exception e) {
                logger.error("Unable to start AuditService: '{}'", e.getMessage(), e);
            }
        }
    }

    public synchronized void destroy() {

        logger.info("[ATNA Service] OpenATNA service...");
        if (service != null) {
            try {
                service.stop();
            } catch (IOException e) {
                logger.error("Unable to stop AuditService: '{}'", e.getMessage(), e);
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
