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

package org.openhealthtools.openatna.persistence.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.*;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;
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
        CodeDao dao = SpringDaoFactory.getFactory().codeDao();
        CodeEntity existing = dao.get(ce);
        if (existing == null) {
            if (policies.isAllowNewCodes()) {
                currentSession().saveOrUpdate(ce);

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
            for (EventTypeCodeEntity code : codes) {
                CodeEntity codeEnt = dao.get(code);
                if (codeEnt == null) {
                    if (policies.isAllowNewCodes()) {
                        currentSession().saveOrUpdate(code);

                    } else {
                        throw new AtnaPersistenceException(code.toString(),
                                AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                    }
                } else {
                    if (codeEnt instanceof EventTypeCodeEntity) {
                        codes.remove(code);
                        codes.add((EventTypeCodeEntity) codeEnt);
                    } else {
                        throw new AtnaPersistenceException("code is defined but is of a different type.",
                                AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                    }
                }
            }
            messageEntity.setEventTypeCodes(codes);
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
        ParticipantDao dao = SpringDaoFactory.getFactory().participantDao();
        ParticipantEntity existing = dao.getByUserId(pe.getUserId());
        if (existing == null) {
            if (policies.isAllowNewParticipants()) {
                saveParticipantCodes(pe, policies);
                currentSession().saveOrUpdate(pe);

            } else {
                throw new AtnaPersistenceException("unknown participant.",
                        AtnaPersistenceException.PersistenceError.NON_EXISTENT_PARTICIPANT);
            }
        } else {
            saveParticipantCodes(pe, policies);
            ap.setParticipant(existing);
        }
        NetworkAccessPointEntity net = ap.getNetworkAccessPoint();
        if (net != null) {
            NetworkAccessPointDao netdao = SpringDaoFactory.getFactory().networkAccessPointDao();
            NetworkAccessPointEntity there = netdao.getByTypeAndIdentifier(net.getType(), net.getIdentifier());
            if (there == null) {
                if (policies.isAllowNewNetworkAccessPoints()) {
                    currentSession().saveOrUpdate(net);

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
        CodeDao dao = SpringDaoFactory.getFactory().codeDao();

        for (ParticipantCodeEntity code : codes) {
            CodeEntity codeEnt = dao.get(code);
            if (codeEnt == null) {
                if (policies.isAllowNewCodes()) {
                    currentSession().saveOrUpdate(code);

                } else {
                    throw new AtnaPersistenceException(code.toString(),
                            AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                }
            } else {
                if (codeEnt instanceof ParticipantCodeEntity) {
                    codes.remove(code);
                    codes.add((ParticipantCodeEntity) codeEnt);
                } else {
                    throw new AtnaPersistenceException("code is defined but is of a different type.",
                            AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                }
            }
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
        SourceDao dao = SpringDaoFactory.getFactory().sourceDao();
        SourceEntity existing = dao.getBySourceId(se.getSourceId());
        if (existing == null) {
            if (policies.isAllowNewSources()) {
                saveSourceCodes(se, policies);
                currentSession().saveOrUpdate(se);

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
        CodeDao dao = SpringDaoFactory.getFactory().codeDao();

        for (SourceCodeEntity code : codes) {
            CodeEntity codeEnt = dao.get(code);
            if (codeEnt == null) {
                if (policies.isAllowNewCodes()) {
                    currentSession().saveOrUpdate(code);

                } else {
                    throw new AtnaPersistenceException(code.toString(),
                            AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                }
            } else {
                if (codeEnt instanceof SourceCodeEntity) {
                    codes.remove(code);
                    codes.add((SourceCodeEntity) codeEnt);
                } else {
                    throw new AtnaPersistenceException("code is defined but is of a different type.",
                            AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                }
            }
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

        ObjectDao dao = SpringDaoFactory.getFactory().objectDao();
        ObjectEntity existing = dao.getByObjectId(oe.getObjectId());
        if (existing == null) {
            if (policies.isAllowNewObjects()) {
                saveObjectCode(oe, policies);
                currentSession().saveOrUpdate(oe);

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
        CodeDao dao = SpringDaoFactory.getFactory().codeDao();
        CodeEntity existing = dao.get(oe);
        if (existing == null) {
            if (policies.isAllowNewCodes()) {
                currentSession().saveOrUpdate(oe);

            } else {
                throw new AtnaPersistenceException("no event id code defined.",
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
