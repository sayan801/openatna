/*
 * Copyright (c) 2009 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.*;
import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 1:16:55 PM
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateMessageDao extends AbstractHibernateDao<MessageEntity> implements MessageDao {

    public HibernateMessageDao(SessionFactory sessionFactory) {
        super(MessageEntity.class, sessionFactory);
    }

    public List<? extends MessageEntity> getByQuery(Query query) throws AtnaPersistenceException {
        HibernateQueryBuilder builder = new HibernateQueryBuilder(criteria());
        Criteria c = builder.build(query);
        return list(c);
    }

    public MessageEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends MessageEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends MessageEntity> getByEventId(EventIdCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("eventId").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByEventType(EventTypeCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("eventTypeCodes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByEventOutcome(Integer outcome) throws AtnaPersistenceException {

        return list(criteria().add(Restrictions.eq("eventOutcome", outcome)));
    }

    public List<? extends MessageEntity> getByEventAction(String action) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("eventActionCode", action)));
    }

    public List<? extends MessageEntity> getByParticipantUserId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageParticipants").createCriteria("participant").add(Restrictions.eq("userId", id)));
    }

    public List<? extends MessageEntity> getByParticipantAltUserId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageParticipants").createCriteria("participant").add(Restrictions.eq("altUserId", id)));
    }

    public List<? extends MessageEntity> getByParticipantCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageParticipants").createCriteria("participant")
                .createCriteria("participantTypeCodes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));

    }

    public List<? extends MessageEntity> getByAuditSourceId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageSources").createCriteria("source").add(Restrictions.eq("sourceId", id)));
    }

    public List<? extends MessageEntity> getByAuditSourceEnterpriseId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageSources").createCriteria("source").add(Restrictions.eq("enterpriseSiteId", id)));

    }

    public List<? extends MessageEntity> getByAuditSourceCode(SourceCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageSources").createCriteria("source")
                .createCriteria("sourceTypeCodes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByObjectId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageObjects").createCriteria("object").add(Restrictions.eq("objectId", id)));
    }

    public List<? extends MessageEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .createCriteria("objectIdTypeCode")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageEntity> getByObjectTypeCode(Short code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .add(Restrictions.eq("objectTypeCode", code)));
    }

    public List<? extends MessageEntity> getByObjectTypeCodeRole(Short code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .add(Restrictions.eq("objectTypeCodeRole", code)));
    }

    public List<? extends MessageEntity> getByObjectSensitivity(String sensitivity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("messageObjects").createCriteria("object")
                .add(Restrictions.eq("objectSensitivity", sensitivity)));
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

    // TODO: will this remove everything?
    public void delete(MessageEntity messageEntity) throws AtnaPersistenceException {
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
        if (codes.size() > 0) {
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
            messageEntity.setEventTypeCodes(new HashSet<EventTypeCodeEntity>(Arrays.asList(arr)));
        }
        Set<MessageParticipantEntity> messageParticipants = messageEntity.getMessageParticipants();
        if (messageParticipants.size() == 0) {
            throw new AtnaPersistenceException("no participants defined",
                    AtnaPersistenceException.PersistenceError.NO_PARTICIPANT);
        }
        for (MessageParticipantEntity entity : messageParticipants) {
            normalize(entity, policies);
        }
        Set<MessageSourceEntity> atnaSources = messageEntity.getMessageSources();
        if (atnaSources.size() == 0) {
            throw new AtnaPersistenceException("no sources defined",
                    AtnaPersistenceException.PersistenceError.NO_SOURCE);
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
        ParticipantEntity existing = dao.getByUserId(pe.getUserId());
        if (existing == null) {
            if (policies.isAllowNewParticipants()) {
                saveParticipantCodes(pe, policies);
                dao.save(pe, policies);
            } else {
                throw new AtnaPersistenceException("unknown participant.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_PARTICIPANT);
            }
        } else {
            saveParticipantCodes(pe, policies);
            existing.setParticipantTypeCodes(pe.getParticipantTypeCodes());
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

    private void saveParticipantCodes(ParticipantEntity pe, PersistencePolicies policies) throws AtnaPersistenceException {
        Set<ParticipantCodeEntity> codes = pe.getParticipantTypeCodes();
        if (codes.size() > 0) {
            ParticipantCodeEntity[] arr = codes.toArray(new ParticipantCodeEntity[codes.size()]);
            CodeDao dao = AtnaFactory.codeDao();
            for (int i = 0; i < arr.length; i++) {
                ParticipantCodeEntity code = arr[i];
                CodeEntity codeEnt = dao.get(code);
                if (codeEnt == null) {
                    if (policies.isAllowNewCodes()) {
                        dao.save(code, policies);
                    } else {
                        throw new AtnaPersistenceException(code.toString(),
                                AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                    }
                } else {
                    if (codeEnt instanceof ParticipantCodeEntity) {
                        arr[i] = ((ParticipantCodeEntity) codeEnt);
                    } else {
                        throw new AtnaPersistenceException("code is defined but is of a different type.",
                                AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                    }
                }
            }
            pe.setParticipantTypeCodes(new HashSet<ParticipantCodeEntity>(Arrays.asList(arr)));
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
        SourceEntity existing = dao.getBySourceId(se.getSourceId());
        if (existing == null) {
            if (policies.isAllowNewSources()) {
                saveSourceCodes(se, policies);
                dao.save(se, policies);

            } else {
                throw new AtnaPersistenceException("no audit source defined.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_SOURCE);
            }
        } else {
            saveSourceCodes(se, policies);
            as.setSource(existing);
        }
    }

    private void saveSourceCodes(SourceEntity pe, PersistencePolicies policies) throws AtnaPersistenceException {
        Set<SourceCodeEntity> codes = pe.getSourceTypeCodes();
        if (codes.size() > 0) {
            CodeDao dao = AtnaFactory.codeDao();
            SourceCodeEntity[] arr = codes.toArray(new SourceCodeEntity[codes.size()]);
            for (int i = 0; i < arr.length; i++) {
                SourceCodeEntity code = arr[i];
                CodeEntity codeEnt = dao.get(code);
                if (codeEnt == null) {
                    if (policies.isAllowNewCodes()) {
                        dao.save(code, policies);
                    } else {
                        throw new AtnaPersistenceException(code.toString(),
                                AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                    }
                } else {
                    if (codeEnt instanceof SourceCodeEntity) {
                        arr[i] = ((SourceCodeEntity) codeEnt);
                    } else {
                        throw new AtnaPersistenceException("code is defined but is of a different type.",
                                AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                    }
                }
            }
            pe.setSourceTypeCodes(new HashSet<SourceCodeEntity>(Arrays.asList(arr)));
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
                saveObjectCode(oe, policies);
                dao.save(oe, policies);
            } else {
                throw new AtnaPersistenceException("no object defined.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_OBJECT);
            }
        } else {
            saveObjectCode(oe, policies);
            ao.setObject(existing);
            Set<ObjectDetailEntity> details = ao.getDetails();
            for (ObjectDetailEntity detail : details) {
                if (!existing.containsDetailType(detail.getType()) &&
                        !policies.isAllowUnknownDetailTypes()) {
                    throw new AtnaPersistenceException("bad object detail key.",
                            AtnaPersistenceException.PersistenceError.UNKNOWN_DETAIL_TYPE);
                }
            }
        }
    }

    private void saveObjectCode(ObjectEntity entity, PersistencePolicies policies) throws AtnaPersistenceException {
        ObjectIdTypeCodeEntity oe = entity.getObjectIdTypeCode();
        CodeDao dao = AtnaFactory.codeDao();
        CodeEntity existing = dao.get(oe);
        if (existing == null) {
            if (policies.isAllowNewCodes()) {
                dao.save(oe, policies);
            } else {
                throw new AtnaPersistenceException("no or unknown object id type code defined.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
            }
        } else {
            if (existing instanceof ObjectIdTypeCodeEntity) {
                entity.setObjectIdTypeCode((ObjectIdTypeCodeEntity) existing);
            } else {
                throw new AtnaPersistenceException("code is defined but is of a different type.",
                        AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
            }
        }
    }
}
