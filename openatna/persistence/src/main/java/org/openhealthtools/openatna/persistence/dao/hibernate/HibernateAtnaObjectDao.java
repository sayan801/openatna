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

import org.openhealthtools.openatna.persistence.model.AtnaObjectEntity;
import org.openhealthtools.openatna.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.persistence.dao.AtnaObjectDao;
import org.openhealthtools.openatna.persistence.dao.ObjectDao;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 12:57:17 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateAtnaObjectDao extends AbstractHibernateDao<AtnaObjectEntity> implements AtnaObjectDao {
    public HibernateAtnaObjectDao(SessionFactory sessionFactory) {
        super(AtnaObjectEntity.class, sessionFactory);
    }


    public AtnaObjectEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends AtnaObjectEntity> getByIdTypeCode(ObjectIdTypeCodeEntity idEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("object").add(Restrictions.eq("objectIdTypeCode", idEntity)));
    }

    public List<? extends AtnaObjectEntity> getByTypeCode(Short code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("object").add(Restrictions.eq("objectTypeCode", code)));
    }

    public List<? extends AtnaObjectEntity> getByTypeCodeRole(Short code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("object").add(Restrictions.eq("objectTypeCodeRole", code)));
    }

    public List<? extends AtnaObjectEntity> getBySensitivity(String code) throws AtnaPersistenceException {
        return list(criteria().createCriteria("object").add(Restrictions.eq("objectSensitivity", code)));
    }

    public List<? extends AtnaObjectEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public void save(AtnaObjectEntity ao) throws AtnaPersistenceException {
        normalize(ao);
        currentSession().saveOrUpdate(ao);
    }

    public void delete(AtnaObjectEntity ap) throws AtnaPersistenceException {
        currentSession().delete(ap);
    }

    public void normalize(AtnaObjectEntity ao) throws AtnaPersistenceException {
        if (ao.getObject() == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NO_OBJECT);
        }
        if (ao.getId() != null) {
            // hmm. Should not be able to modify audit messages?
            // this is a bit harsh. There will be situations where certain users could do this.
            throw new AtnaPersistenceException("audit messages cannot be modified.", AtnaPersistenceException.PersistenceError.UNMODIFIABLE);

        }
        ObjectEntity oe = ao.getObject();
        ObjectDao dao = SpringDaoFactory.getFactory().objectDao();
        ObjectEntity existing = dao.getByObjectId(oe.getObjectId());
        if (existing == null) {
            throw new AtnaPersistenceException("no object defined.", AtnaPersistenceException.PersistenceError.NON_EXISTENT_OBJECT);
        } else {
            ao.setObject(existing);
        }
    }
}
