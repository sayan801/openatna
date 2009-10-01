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

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.*;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 1:16:55 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateMessageDao extends AbstractHibernateDao<MessageEntity> implements MessageDao {

    public HibernateMessageDao(SessionFactory sessionFactory) {
        super(MessageEntity.class, sessionFactory);
    }

    public List<? extends MessageEntity> getByQuery(Query query) throws AtnaPersistenceException {
        return null;
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
    public void save(MessageEntity messageEntity) throws AtnaPersistenceException {
        normalize(messageEntity);
        currentSession().saveOrUpdate(messageEntity);
    }

    public void delete(MessageEntity messageEntity) throws AtnaPersistenceException {
        currentSession().delete(messageEntity);
    }

    public void normalize(MessageEntity messageEntity) throws AtnaPersistenceException {
        if (messageEntity.getEventId() == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NO_EVENT_ID);
        }
        if (messageEntity.getId() != null) {
            // hmm. Should not be able to modify audit messages?
            // this is a bit harsh. There will be situations where certain users could do this.
            throw new AtnaPersistenceException("audit messages cannot be modified.", AtnaPersistenceException.PersistenceError.UNMODIFIABLE);

        }
        EventIdCodeEntity ce = messageEntity.getEventId();
        CodeDao dao = SpringDaoFactory.getFactory().codeDao();
        EventIdCodeEntity existing = (EventIdCodeEntity) dao.get(ce);
        if (existing == null) {
            throw new AtnaPersistenceException("no event id code defined.", AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
        } else {
            messageEntity.setEventId(existing);
        }
        Set<EventTypeCodeEntity> codes = messageEntity.getEventTypeCodes();
        if (codes.size() > 0) {
            for (EventTypeCodeEntity code : codes) {
                codes.remove(code);
                code = (EventTypeCodeEntity) dao.find(code);
                if (code.getVersion() != null) {
                    codes.add(code);
                } else {
                    throw new AtnaPersistenceException(code.toString(), AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                }
            }
            messageEntity.setEventTypeCodes(codes);
        }

        Set<MessageParticipantEntity> messageParticipants = messageEntity.getMessageParticipants();
        if (messageParticipants.size() == 0) {
            throw new AtnaPersistenceException("no participants defined", AtnaPersistenceException.PersistenceError.NO_PARTICIPANT);
        }
        MessageParticipantDao pdao = SpringDaoFactory.getFactory().messageParticipantDao();
        for (MessageParticipantEntity entity : messageParticipants) {
            pdao.normalize(entity);
        }
        Set<MessageSourceEntity> atnaSources = messageEntity.getMessageSources();
        if (atnaSources.size() == 0) {
            throw new AtnaPersistenceException("no sources defined", AtnaPersistenceException.PersistenceError.NO_SOURCE);
        }
        MessageSourceDao sdao = SpringDaoFactory.getFactory().messageSourceDao();
        for (MessageSourceEntity entity : atnaSources) {
            sdao.normalize(entity);
        }
        Set<MessageObjectEntity> messageObjects = messageEntity.getMessageObjects();
        if (messageObjects.size() > 0) {
            MessageObjectDao odao = SpringDaoFactory.getFactory().messageObjectDao();
            for (MessageObjectEntity entity : messageObjects) {
                odao.normalize(entity);
            }
        }
    }
}
