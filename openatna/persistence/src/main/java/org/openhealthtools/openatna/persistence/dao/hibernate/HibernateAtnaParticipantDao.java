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

import org.openhealthtools.openatna.persistence.model.AtnaParticipantEntity;
import org.openhealthtools.openatna.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.persistence.dao.AtnaParticipantDao;
import org.openhealthtools.openatna.persistence.dao.ParticipantDao;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 1:07:36 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateAtnaParticipantDao extends AbstractHibernateDao<AtnaParticipantEntity> implements AtnaParticipantDao {

    public HibernateAtnaParticipantDao(SessionFactory sessionFactory) {
        super(AtnaParticipantEntity.class, sessionFactory);
    }

    public AtnaParticipantEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public AtnaParticipantEntity getByUserId(String userId) throws AtnaPersistenceException {
        return uniqueResult(criteria().createCriteria("participant").add(Restrictions.eq("userId", userId)));
    }

    public AtnaParticipantEntity getByAltUserId(String altUserId) throws AtnaPersistenceException {
        return uniqueResult(criteria().createCriteria("participant").add(Restrictions.eq("alternativeUserId", altUserId)));
    }

    public List<? extends AtnaParticipantEntity> getByCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("participant").createCriteria("codes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends AtnaParticipantEntity> getByUserName(String userName) throws AtnaPersistenceException {
        return list(criteria().createCriteria("participant").add(Restrictions.eq("userName", userName)));
    }

    public List<? extends AtnaParticipantEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public void save(AtnaParticipantEntity ap) throws AtnaPersistenceException {
        normalize(ap);
        currentSession().saveOrUpdate(ap);
    }

    public void delete(AtnaParticipantEntity ap) throws AtnaPersistenceException {
        currentSession().delete(ap);
    }

    public void normalize(AtnaParticipantEntity ap) throws AtnaPersistenceException {
        if (ap.getParticipant() == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NO_SOURCE);
        }
        if (ap.getId() != null) {
            // hmm. Should not be able to modify audit messages?
            // this is a bit harsh. There will be situations where certain users could do this.
            throw new AtnaPersistenceException("audit messages cannot be modified.", AtnaPersistenceException.PersistenceError.UNMODIFIABLE);
        }
        ParticipantEntity pe = ap.getParticipant();
        ParticipantDao dao = SpringDaoFactory.getFactory().participantDao();
        ParticipantEntity existing = dao.getByUserId(pe.getUserId());
        if (existing == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NON_EXISTENT_SOURCE);
        } else {
            ap.setParticipant(existing);
        }
    }
}
