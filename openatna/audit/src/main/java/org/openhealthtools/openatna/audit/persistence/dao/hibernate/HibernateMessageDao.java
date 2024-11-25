package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.*;
import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateMessageDao extends AbstractHibernateDao<MessageEntity> implements MessageDao {

    private static Logger logger = LoggerFactory.getLogger(HibernateMessageDao.class);

    public HibernateMessageDao(SessionFactory sessionFactory) {
        super(MessageEntity.class, sessionFactory);
    }

    public List<? extends MessageEntity> getByQuery(Query query) {

        HibernateQueryBuilder builder = new HibernateQueryBuilder(this);
        Criteria c = builder.build(query);
        if (c == null) {
            return new ArrayList<>();
        }
        return list(c);
    }

    public MessageEntity getById(Long id) {
        return get(id);
    }

    public List<? extends MessageEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends MessageEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public List<? extends MessageEntity> getByEventId(EventIdCodeEntity codeEntity) {

        return list(criteria().createCriteria("eventId").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getBySourceAddress(String address) {

        return list(criteria().add(Restrictions.eq("sourceAddress", address)));
    }

    public List<? extends MessageEntity> getByEventType(EventTypeCodeEntity codeEntity) {

        return list(criteria().createCriteria("eventTypeCodes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByEventOutcome(Integer outcome) {

        return list(criteria().add(Restrictions.eq("eventOutcome", outcome)));
    }

    public List<? extends MessageEntity> getByEventAction(String action) {

        return list(criteria().add(Restrictions.eq("eventActionCode", action)));
    }

    public List<? extends MessageEntity> getByParticipantUserId(String id) {

        return list(criteria().createCriteria("messageParticipants").createCriteria("participant")
                .add(Restrictions.eq("userId", id)));
    }

    public List<? extends MessageEntity> getByParticipantAltUserId(String id) {

        return list(criteria().createCriteria("messageParticipants").createCriteria("participant")
                .add(Restrictions.eq("altUserId", id)));
    }

    public List<? extends MessageEntity> getByParticipantCode(ParticipantCodeEntity codeEntity) {

        return list(criteria().createCriteria("messageParticipants").createCriteria("participant")
                .createCriteria("participantTypeCodes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByAuditSourceId(String id) {

        return list(criteria().createCriteria("messageSources").createCriteria("source")
                .add(Restrictions.eq("sourceId", id)));
    }

    public List<? extends MessageEntity> getByAuditSourceEnterpriseId(String id) {

        return list(criteria().createCriteria("messageSources").createCriteria("source")
                .add(Restrictions.eq("enterpriseSiteId", id)));

    }

    public List<? extends MessageEntity> getByAuditSourceCode(SourceCodeEntity codeEntity) {

        return list(criteria().createCriteria("messageSources").createCriteria("source")
                .createCriteria("sourceTypeCodes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByObjectId(String id) {

        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .add(Restrictions.eq("objectId", id)));
    }

    public List<? extends MessageEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity) {

        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .createCriteria("objectIdTypeCode")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByObjectTypeCode(Short code) {

        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .add(Restrictions.eq("objectTypeCode", code)));
    }

    public List<? extends MessageEntity> getByObjectTypeCodeRole(Short code) {

        return list(criteria().createCriteria("messageObjects").createCriteria("object").add(Restrictions.eq("objectTypeCodeRole", code)));
    }

    public List<? extends MessageEntity> getByObjectSensitivity(String sensitivity) {

        return list(criteria().createCriteria("messageObjects").createCriteria("object").add(Restrictions.eq("objectSensitivity", sensitivity)));
    }

    /**
     * is this right?
     *
     * @param messageEntity
     * @throws AtnaPersistenceException
     */
    public void save(MessageEntity messageEntity, PersistencePolicies policies) throws AtnaPersistenceException {

        normalize(messageEntity, policies);
        currentSession().saveOrUpdate(messageEntity);
    }

    //TODO: will this remove everything?
    public void delete(MessageEntity messageEntity) {
        currentSession().delete(messageEntity);
    }

    private void normalize(MessageEntity messageEntity, PersistencePolicies policies) throws AtnaPersistenceException {

        if (messageEntity.getEventId() == null) {
            throw new AtnaPersistenceException("no audit source defined.",
                    AtnaPersistenceException.PersistenceError.NO_EVENT_ID);
        }
        if (messageEntity.getId() != null) {
            if (!policies.isAllowModifyMessages()) {
                throw new AtnaPersistenceException("audit messages cannot be modified.",
                        AtnaPersistenceException.PersistenceError.UNMODIFIABLE);
            }
        }
        EventIdCodeEntity ce = messageEntity.getEventId();
        CodeDao dao = AtnaFactory.codeDao();
        CodeEntity existing = dao.get(ce);
        if (existing == null) {
            if (policies.isAllowNewCodes()) {
                dao.save(ce, policies);
            } else {
                throw new AtnaPersistenceException("no event id code defined.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
            }
        } else {
            if (existing instanceof EventIdCodeEntity) {
                messageEntity.setEventId((EventIdCodeEntity) existing);
            } else {
                throw new AtnaPersistenceException("code is defined but is of a different type.",
                        AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
            }
        }

        Set<EventTypeCodeEntity> codes = messageEntity.getEventTypeCodes();
        if (!codes.isEmpty()) {
            EventTypeCodeEntity[] arr = codes.toArray(new EventTypeCodeEntity[codes.size()]);
            for (int i = 0; i < arr.length; i++) {
                EventTypeCodeEntity code = arr[i];
                CodeEntity codeEnt = dao.get(code);
                if (codeEnt == null) {
                    if (policies.isAllowNewCodes()) {
                        dao.save(code, policies);
                    } else {
                        throw new AtnaPersistenceException(code.toString(),
                                AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                    }
                } else {
                    if (codeEnt instanceof EventTypeCodeEntity) {
                        arr[i] = ((EventTypeCodeEntity) codeEnt);
                    } else {
                        throw new AtnaPersistenceException("code is defined but is of a different type.",
                                AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                    }
                }
            }
            messageEntity.setEventTypeCodes(new HashSet<>(Arrays.asList(arr)));
        }
        Set<MessageParticipantEntity> messageParticipants = messageEntity.getMessageParticipants();
        if (messageParticipants.isEmpty()) {
            throw new AtnaPersistenceException("no participants defined",
                    AtnaPersistenceException.PersistenceError.NO_PARTICIPANT);
        }
        for (MessageParticipantEntity entity : messageParticipants) {
            normalize(entity, policies);
        }
        Set<MessageSourceEntity> atnaSources = messageEntity.getMessageSources();
        if (atnaSources.isEmpty()) {
            throw new AtnaPersistenceException("no sources defined", AtnaPersistenceException.PersistenceError.NO_SOURCE);
        }
        for (MessageSourceEntity entity : atnaSources) {
            normalize(entity, policies);
        }
        Set<MessageObjectEntity> messageObjects = messageEntity.getMessageObjects();
        for (MessageObjectEntity entity : messageObjects) {
            normalize(entity, policies);
        }
    }

    private void normalize(MessageParticipantEntity ap, PersistencePolicies policies) throws AtnaPersistenceException {

        if (ap.getParticipant() == null) {
            throw new AtnaPersistenceException("no active participant defined.",
                    AtnaPersistenceException.PersistenceError.NO_PARTICIPANT);
        }
        if (ap.getId() != null) {
            if (!policies.isAllowModifyMessages()) {
                throw new AtnaPersistenceException("audit messages cannot be modified.",
                        AtnaPersistenceException.PersistenceError.UNMODIFIABLE);
            }
        }
        ParticipantEntity pe = ap.getParticipant();
        ParticipantDao dao = AtnaFactory.participantDao();
        ParticipantEntity existing = dao.get(pe);
        if (existing == null) {
            if (policies.isAllowNewParticipants()) {
                dao.save(pe, policies);
            } else {
                throw new AtnaPersistenceException("unknown participant.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_PARTICIPANT);
            }
        } else {
            ap.setParticipant(existing);
        }
        NetworkAccessPointEntity net = ap.getNetworkAccessPoint();
        if (net != null) {
            NetworkAccessPointDao netdao = AtnaFactory.networkAccessPointDao();
            NetworkAccessPointEntity there = netdao.getByTypeAndIdentifier(net.getType(), net.getIdentifier());
            if (there == null) {
                if (policies.isAllowNewNetworkAccessPoints()) {
                    netdao.save(net, policies);
                } else {
                    throw new AtnaPersistenceException("unknown network access point.",
                            AtnaPersistenceException.PersistenceError.NON_EXISTENT_NETWORK_ACCESS_POINT);
                }
            } else {
                ap.setNetworkAccessPoint(there);
            }
        }
    }

    private boolean isParticipantNonUniquelyEqual(ParticipantEntity update, ParticipantEntity existing) {

        if (update.getUserName() != null && !update.getUserName().equals(existing.getUserName())) {
            return false;
        }
        return update.getParticipantTypeCodes().equals(existing.getParticipantTypeCodes());
    }

    private boolean isSourceNonUniquelyEqual(SourceEntity update, SourceEntity existing) {

        return update.getSourceTypeCodes().equals(existing.getSourceTypeCodes());
    }

    private boolean isObjectNonUniquelyEqual(ObjectEntity update, ObjectEntity existing) {

        if (update.getObjectName() != null && !update.getObjectName().equals(existing.getObjectName())) {
            return false;
        }
        if (!update.getObjectIdTypeCode().equals(existing.getObjectIdTypeCode())) {
            return false;
        }
        if (!update.getObjectSensitivity().equals(existing.getObjectSensitivity())) {
            return false;
        }
        if (!update.getObjectTypeCode().equals(existing.getObjectTypeCode())) {
            return false;
        }
        if (!update.getObjectTypeCodeRole().equals(existing.getObjectTypeCodeRole())) {
            return false;
        }
        if (!update.getObjectDetailTypes().equals(existing.getObjectDetailTypes())) {
            return false;
        }
        //TODO: doesn't include SopClasses
        return update.getObjectDescriptions().equals(existing.getObjectDescriptions());
    }

    private void updateParticipant(ParticipantDao dao, PersistencePolicies policies, MessageParticipantEntity ap,
                                   ParticipantEntity update, ParticipantEntity existing) throws AtnaPersistenceException {

        if (!isParticipantNonUniquelyEqual(update, existing)) {
            update.setVersion(existing.getVersion() + 1);
            dao.save(existing, policies);
            dao.save(update, policies);
            ap.setParticipant(update);
        } else {
            ap.setParticipant(existing);
        }
    }

    private void updateSource(SourceDao dao, PersistencePolicies policies, MessageSourceEntity ap, SourceEntity update,
                              SourceEntity existing) throws AtnaPersistenceException {

        if (!isSourceNonUniquelyEqual(update, existing)) {
            update.setVersion(existing.getVersion() + 1);
            dao.save(existing, policies);
            dao.save(update, policies);
            ap.setSource(update);
        } else {
            ap.setSource(existing);
        }
    }

    private void updateObject(ObjectDao dao, PersistencePolicies policies, MessageObjectEntity ap, ObjectEntity update,
                              ObjectEntity existing) throws AtnaPersistenceException {

        if (!isObjectNonUniquelyEqual(update, existing)) {
            update.setVersion(existing.getVersion() + 1);
            dao.save(existing, policies);
            dao.save(update, policies);
            ap.setObject(update);
            Set<ObjectDetailEntity> details = ap.getDetails();
            for (ObjectDetailEntity detail : details) {
                if (!update.containsDetailType(detail.getType())
                        && !policies.isAllowUnknownDetailTypes()) {
                    throw new AtnaPersistenceException("bad object detail key.",
                            AtnaPersistenceException.PersistenceError.UNKNOWN_DETAIL_TYPE);
                }
            }
        } else {
            ap.setObject(existing);
        }
    }

    private void normalize(MessageSourceEntity as, PersistencePolicies policies) throws AtnaPersistenceException {

        if (as.getSource() == null) {
            throw new AtnaPersistenceException("no audit source defined.",
                    AtnaPersistenceException.PersistenceError.NO_SOURCE);
        }
        if (as.getId() != null) {
            if (!policies.isAllowModifyMessages()) {
                throw new AtnaPersistenceException("audit messages cannot be modified.",
                        AtnaPersistenceException.PersistenceError.UNMODIFIABLE);
            }
        }
        SourceEntity se = as.getSource();
        SourceDao dao = AtnaFactory.sourceDao();
        SourceEntity existing = dao.get(se);
        if (existing == null) {
            if (policies.isAllowNewSources()) {
                dao.save(se, policies);
            } else {
                throw new AtnaPersistenceException("no audit source defined.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_SOURCE);
            }
        } else {
            as.setSource(existing);
        }
    }

    private void normalize(MessageObjectEntity ao, PersistencePolicies policies) throws AtnaPersistenceException {
        if (ao.getObject() == null) {
            throw new AtnaPersistenceException("no participant object defined.",
                    AtnaPersistenceException.PersistenceError.NO_OBJECT);
        }
        if (ao.getId() != null) {
            if (!policies.isAllowModifyMessages()) {
                throw new AtnaPersistenceException("audit messages cannot be modified.",
                        AtnaPersistenceException.PersistenceError.UNMODIFIABLE);
            }
        }
        ObjectEntity oe = ao.getObject();
        ObjectDao dao = AtnaFactory.objectDao();
        ObjectEntity existing = dao.getByObjectId(oe.getObjectId());
        if (existing == null) {
            if (policies.isAllowNewObjects()) {
                dao.save(oe, policies);
            } else {
                throw new AtnaPersistenceException("no object defined.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_OBJECT);
            }
        } else {
            ao.setObject(existing);
            Set<ObjectDetailEntity> details = ao.getDetails();
            for (ObjectDetailEntity detail : details) {
                if (!existing.containsDetailType(detail.getType())
                        && !policies.isAllowUnknownDetailTypes()) {
                    throw new AtnaPersistenceException("bad object detail key.",
                            AtnaPersistenceException.PersistenceError.UNKNOWN_DETAIL_TYPE);
                }
            }
        }
    }
}
