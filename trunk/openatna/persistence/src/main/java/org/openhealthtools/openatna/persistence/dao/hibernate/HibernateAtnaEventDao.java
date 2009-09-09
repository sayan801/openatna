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

import org.openhealthtools.openatna.persistence.model.AtnaEventEntity;
import org.openhealthtools.openatna.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.persistence.dao.AtnaEventDao;
import org.openhealthtools.openatna.persistence.dao.CodeDao;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 1:28:12 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateAtnaEventDao extends AbstractHibernateDao<AtnaEventEntity> implements AtnaEventDao {

    public HibernateAtnaEventDao(SessionFactory sessionFactory) {
        super(AtnaEventEntity.class, sessionFactory);
    }

    public AtnaEventEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends AtnaEventEntity> getByEventId(EventIdCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("eventId").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaEventEntity> getByEventAction(String action) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("eventActionCode", action)));
    }

    public List<? extends AtnaEventEntity> getByEventOutcome(Integer outcome) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("eventOutcome", outcome)));

    }

    public List<? extends AtnaEventEntity> getByEventTimestamp(Date begin, Date end) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.ge("eventDateTime", begin)).add(Restrictions.le("eventDateTime", end)));

    }

    public List<? extends AtnaEventEntity> getByEventTypeCode(EventTypeCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("eventTypeCodes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaEventEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public void save(AtnaEventEntity ae) throws AtnaPersistenceException {
        normalize(ae);
        currentSession().saveOrUpdate(ae);
    }

    public void delete(AtnaEventEntity ap) throws AtnaPersistenceException {
        currentSession().delete(ap);
    }

    public void normalize(AtnaEventEntity ae) throws AtnaPersistenceException {
        if (ae.getEventId() == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NO_EVENT_ID);
        }
        if (ae.getId() != null) {
            // hmm. Should not be able to modify audit messages?
            // this is a bit harsh. There will be situations where certain users could do this.
            throw new AtnaPersistenceException("audit messages cannot be modified.", AtnaPersistenceException.PersistenceError.UNMODIFIABLE);

        }
        EventIdCodeEntity ce = ae.getEventId();
        CodeDao dao = SpringDaoFactory.getFactory().codeDao();
        EventIdCodeEntity existing = (EventIdCodeEntity) dao.get(ce);
        if (existing == null) {
            throw new AtnaPersistenceException("no event id code defined.", AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
        } else {
            ae.setEventId(existing);
        }
        Set<EventTypeCodeEntity> codes = ae.getEventTypeCodes();
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
            ae.setEventTypeCodes(codes);
        }
    }
}
