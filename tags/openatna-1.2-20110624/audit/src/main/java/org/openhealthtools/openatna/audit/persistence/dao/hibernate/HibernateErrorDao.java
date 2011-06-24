/**
 * Copyright (c) 2009-2011 University of Cardiff and others.
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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Feb 6, 2010: 4:03:35 PM
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateErrorDao extends AbstractHibernateDao<ErrorEntity> implements ErrorDao {

    public HibernateErrorDao(SessionFactory sessionFactory) {
        super(ErrorEntity.class, sessionFactory);
    }

    public ErrorEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends ErrorEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends ErrorEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public List<? extends ErrorEntity> getBySourceIp(String ip) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)));
    }

    public List<? extends ErrorEntity> getAfter(Date date) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.ge("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getAfter(String ip, Date date) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)).add(Restrictions.ge("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getBefore(Date date) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.le("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getBefore(String ip, Date date) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)).add(Restrictions.le("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getBetween(Date first, Date second) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.ge("errorTimestamp", first)).add(Restrictions.le("errorTimestamp", second)));
    }

    public List<? extends ErrorEntity> getBetween(String ip, Date first, Date second) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)).add(Restrictions.ge("errorTimestamp", first))
                .add(Restrictions.le("errorTimestamp", second)));
    }

    public void save(ErrorEntity entity) throws AtnaPersistenceException {
        currentSession().saveOrUpdate(entity);
    }
}
