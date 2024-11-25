package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.openhealthtools.openatna.audit.persistence.model.codes.*;

import java.util.List;

public interface MessageDao extends Dao {

    List<? extends MessageEntity> getByQuery(Query query) throws AtnaPersistenceException;

    MessageEntity getById(Long id) throws AtnaPersistenceException;

    List<? extends MessageEntity> getAll() throws AtnaPersistenceException;

    List<? extends MessageEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByEventId(EventIdCodeEntity idEntity) throws AtnaPersistenceException;

    List<? extends MessageEntity> getBySourceAddress(String address) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByEventType(EventTypeCodeEntity typeEntity) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByEventOutcome(Integer outcome) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByEventAction(String action) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByParticipantUserId(String id) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByParticipantAltUserId(String id) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByParticipantCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByAuditSourceId(String id) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByAuditSourceEnterpriseId(String id) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByAuditSourceCode(SourceCodeEntity codeEntity) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByObjectId(String id) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByObjectTypeCode(Short code) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByObjectTypeCodeRole(Short code) throws AtnaPersistenceException;

    List<? extends MessageEntity> getByObjectSensitivity(String sensitivity) throws AtnaPersistenceException;

    void save(MessageEntity messageEntity, PersistencePolicies policies) throws AtnaPersistenceException;

    void delete(MessageEntity messageEntity) throws AtnaPersistenceException;
}
