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

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.*;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;

import java.util.List;
import java.util.Set;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 1:16:55 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateAtnaMessageDao extends AbstractHibernateDao<AtnaMessageEntity> implements AtnaMessageDao {

    public HibernateAtnaMessageDao(SessionFactory sessionFactory) {
        super(AtnaMessageEntity.class, sessionFactory);
    }

    public AtnaMessageEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends AtnaMessageEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends AtnaMessageEntity> getByEventId(EventIdCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("eventId").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaMessageEntity> getByEventType(EventTypeCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("eventTypeCodes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaMessageEntity> getByParticipantUserId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaParticipants").createCriteria("participant").add(Restrictions.eq("userId", id)));
    }

    public List<? extends AtnaMessageEntity> getByParticipantAltUserId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaParticipants").createCriteria("participant").add(Restrictions.eq("altUserId", id)));
    }

    public List<? extends AtnaMessageEntity> getByParticipantCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaParticipants").createCriteria("participant")
                .createCriteria("codes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));

    }

    public List<? extends AtnaMessageEntity> getByAuditSourceId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaSources").createCriteria("source").add(Restrictions.eq("sourceId", id)));
    }

    public List<? extends AtnaMessageEntity> getByAuditSourceEnterpriseId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaSources").createCriteria("source").add(Restrictions.eq("enterpriseSiteId", id)));

    }

    public List<? extends AtnaMessageEntity> getByAuditSourceCode(SourceCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaSources").createCriteria("source")
                .createCriteria("codes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaMessageEntity> getByObjectId(String id) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaObjects").createCriteria("object").add(Restrictions.eq("objectId", id)));
    }

    public List<? extends AtnaMessageEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaObjects").createCriteria("object")
                .createCriteria("objectIdTypeCode")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaMessageEntity> getByObjectTypeCode(Short code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaObjects").createCriteria("object")
                .add(Restrictions.eq("objectTypeCode", code)));
    }

    public List<? extends AtnaMessageEntity> getByObjectTypeCodeRole(Short code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaObjects").createCriteria("object")
                .add(Restrictions.eq("objectTypeCodeRole", code)));
    }

    public List<? extends AtnaMessageEntity> getByObjectSensitivity(String sensitivity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("atnaObjects").createCriteria("object")
                .add(Restrictions.eq("objectSensitivity", sensitivity)));
    }

    /**
     * is this right?
     *
     * @param messageEntity
     * @throws AtnaPersistenceException
     */
    public void save(AtnaMessageEntity messageEntity) throws AtnaPersistenceException {
        normalize(messageEntity);
        currentSession().saveOrUpdate(messageEntity);
    }

    public void delete(AtnaMessageEntity messageEntity) throws AtnaPersistenceException {
        currentSession().delete(messageEntity);
    }

    public void normalize(AtnaMessageEntity messageEntity) throws AtnaPersistenceException {
        AtnaEventEntity evt = messageEntity.getEvent();
        if (evt == null) {
            throw new AtnaPersistenceException("no event identifier defined", AtnaPersistenceException.PersistenceError.NO_EVENT_ID);
        }
        AtnaEventDao edao = SpringDaoFactory.getFactory().atnaEventDao();
        edao.normalize(evt);
        Set<AtnaParticipantEntity> atnaParticipants = messageEntity.getAtnaParticipants();
        if (atnaParticipants.size() == 0) {
            throw new AtnaPersistenceException("no participants defined", AtnaPersistenceException.PersistenceError.NO_PARTICIPANT);
        }
        AtnaParticipantDao pdao = SpringDaoFactory.getFactory().atnaParticipantDao();
        for (AtnaParticipantEntity entity : atnaParticipants) {
            pdao.normalize(entity);
        }
        Set<AtnaSourceEntity> atnaSources = messageEntity.getAtnaSources();
        if (atnaSources.size() == 0) {
            throw new AtnaPersistenceException("no sources defined", AtnaPersistenceException.PersistenceError.NO_SOURCE);
        }
        AtnaSourceDao sdao = SpringDaoFactory.getFactory().atnaSourceDao();
        for (AtnaSourceEntity entity : atnaSources) {
            sdao.normalize(entity);
        }
        Set<AtnaObjectEntity> atnaObjects = messageEntity.getAtnaObjects();
        if (atnaObjects.size() > 0) {
            AtnaObjectDao odao = SpringDaoFactory.getFactory().atnaObjectDao();
            for (AtnaObjectEntity entity : atnaObjects) {
                odao.normalize(entity);
            }
        }
    }
}
