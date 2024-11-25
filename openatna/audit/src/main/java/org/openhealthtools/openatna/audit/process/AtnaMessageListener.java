package org.openhealthtools.openatna.audit.process;

import eu.epsos.util.audit.AuditLogSerializer;
import eu.epsos.util.audit.AuditLogSerializer.Type;
import eu.epsos.util.audit.AuditLogSerializerImpl;
import eu.epsos.util.audit.MessageHandlerListener;
import org.apache.commons.lang3.StringUtils;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.log.PersistenceErrorLogger;
import org.openhealthtools.openatna.audit.log.SyslogErrorLogger;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.openhealthtools.openatna.audit.service.ServiceConfiguration;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AtnaMessageListener implements SyslogListener<AtnaMessage>, MessageHandlerListener {

    private static final String SERVER_EHEALTH_MODE = "server.ehealth.mode";
    private final Logger logger = LoggerFactory.getLogger(AtnaMessageListener.class);
    private final Logger loggerClinical = LoggerFactory.getLogger("LOGGER_CLINICAL");
    private AuditService service;
    private AuditLogSerializer auditLogSerializer;

    public AtnaMessageListener(AuditService service) {
        this.service = service;
        auditLogSerializer = new AuditLogSerializerImpl(Type.ATNA);
    }

    public void messageArrived(SyslogMessage<AtnaMessage> message) {

        boolean persisted = false;
        try {
            persisted = processMessage(message);
        } finally {

            if (!persisted) {
                auditLogSerializer.writeObjectToFile(message);
            }
        }
    }

    public boolean handleMessage(Serializable message) {

        logger.info("[ATNA Listener] Handling Message: '{}'", message.getClass());
        if (message instanceof SyslogMessage<?>) {
            return processMessage((SyslogMessage<AtnaMessage>) message);
        } else {
            logger.warn("Message null or unknown type! Cannot handle message.");
            return false;
        }
    }

    /**
     * Processes the Syslog ATNA message received by the secured node.
     *
     * @param message Audit Message marshalled as SyslogMessage<AtnaMessage>.
     * @return true or false according the result of the save operation.
     */
    public boolean processMessage(SyslogMessage<AtnaMessage> message) {

        synchronized (this) {

            byte[] bytes = "No message available".getBytes(StandardCharsets.UTF_8);
            AtnaMessage atnaMessage = message.getMessage().getMessageObject();
            atnaMessage.setSourceAddress(message.getSourceIp());
            logger.info("[ATNA Listener] Message Processed: [{}] Facility:'{}' Severity:'{}' ('{}'/'{}') at '{}'",
                    message.getMessage().getMessageObject().getEventCode().getCode(), message.getFacility(),
                    message.getSeverity(), message.getHostName(), message.getSourceIp(), message.getTimestamp());

            boolean persisted = false;
            try {
                persisted = service.process(atnaMessage);
                if (!StringUtils.equals(System.getProperty(SERVER_EHEALTH_MODE), "PRODUCTION") && loggerClinical.isDebugEnabled()) {
                    logger.debug("[ATNA Listener] Syslog message '{}' persisted: '{}'\n'{}'", atnaMessage.getMessageId(),
                            persisted, new String(atnaMessage.getMessageContent(), StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                logger.error("Exception: '{}'", e.getMessage(), e);
                SyslogException ex = new SyslogException(e.getMessage(), e, bytes);
                if (message.getSourceIp() != null) {
                    ex.setSourceIp(message.getSourceIp());
                }
                exceptionThrown(ex);
            }
            return persisted;
        }
    }

    public void exceptionThrown(SyslogException exception) {

        SyslogErrorLogger.log(exception);
        ServiceConfiguration config = service.getServiceConfig();
        if (config != null) {
            PersistencePolicies pp = config.getPersistencePolicies();
            if (pp != null && pp.isPersistErrors()) {

                ErrorDao dao = AtnaFactory.errorDao();
                ErrorEntity ent = createEntity(exception);
                synchronized (this) {
                    try {
                        dao.save(ent);
                    } catch (AtnaPersistenceException e) {
                        PersistenceErrorLogger.log(e);
                    }
                }
            }
        }
    }

    private ErrorEntity createEntity(SyslogException e) {

        ErrorEntity ent = new ErrorEntity();
        ent.setErrorTimestamp(new Date());

        if (e.getBytes() != null) {
            ent.setPayload(e.getBytes());
        }
        if (e.getSourceIp() != null) {
            ent.setSourceIp(e.getSourceIp());
        }
        if (e.getMessage() != null) {
            ent.setErrorMessage(e.getClass().getName() + ":" + e.getMessage());
        }
        ent.setStackTrace(createStackTrace(e));
        return ent;
    }

    private byte[] createStackTrace(Throwable e) {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bout);
        e.printStackTrace(writer);
        writer.flush();
        writer.close();
        return bout.toByteArray();
    }
}
