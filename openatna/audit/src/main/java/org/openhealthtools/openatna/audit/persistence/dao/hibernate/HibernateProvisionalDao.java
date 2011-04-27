/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.ProvisionalDao;
import org.openhealthtools.openatna.audit.persistence.model.ProvisionalEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateProvisionalDao extends AbstractHibernateDao<ProvisionalEntity> implements ProvisionalDao {


    public HibernateProvisionalDao(SessionFactory sessionFactory) {
        super(ProvisionalEntity.class, sessionFactory);
    }

    public ProvisionalEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends ProvisionalEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends ProvisionalEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public void save(ProvisionalEntity pe) throws AtnaPersistenceException {
        currentSession().saveOrUpdate(pe);
    }

    public void delete(ProvisionalEntity pe) throws AtnaPersistenceException {
        currentSession().delete(pe);
    }

}