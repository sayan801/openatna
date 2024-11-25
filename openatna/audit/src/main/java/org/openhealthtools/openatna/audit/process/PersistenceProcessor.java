package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.AuditException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.util.EntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrew Harrison
 */
public class PersistenceProcessor implements AtnaProcessor {

    private final Logger logger = LoggerFactory.getLogger(PersistenceProcessor.class);


    public void process(ProcessContext context) throws Exception {

        AtnaMessage atnaMessage = context.getMessage();
        if (atnaMessage == null) {
            throw new AuditException("no message", null, AuditException.AuditError.NULL_MESSAGE);
        }
        MessageEntity entity = EntityConverter.createMessage(atnaMessage);
        if (entity != null) {

            PersistencePolicies persistencePolicies = context.getPolicies();
            if (persistencePolicies == null) {
                persistencePolicies = new PersistencePolicies();
            }
            MessageDao messageDao = AtnaFactory.messageDao();
            if (messageDao != null) {
                synchronized (this) {
                    messageDao.save(entity, persistencePolicies);
                    context.setState(ProcessContext.State.PERSISTED);
                }
            } else {
                throw new AuditException("Message Data Access Object could not be created",
                        atnaMessage, AuditException.AuditError.INVALID_MESSAGE);
            }
        } else {
            throw new AuditException("Message Entity could not be created",
                    atnaMessage, AuditException.AuditError.INVALID_MESSAGE);
        }
        atnaMessage.setMessageId(entity.getId());
        logger.debug("ADDED ID TO ATNA MESSAGE: '{}'", atnaMessage.getMessageId());
    }

    public void error(ProcessContext context) {
        // Not implemented into the eHDSI context.
    }
}
