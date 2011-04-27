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
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.NetworkAccessPointDao;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 8, 2009: 12:54:41 PM
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateNetworkAccessPointDao extends AbstractHibernateDao<NetworkAccessPointEntity>
        implements NetworkAccessPointDao {

    public HibernateNetworkAccessPointDao(SessionFactory sessionFactory) {
        super(NetworkAccessPointEntity.class, sessionFactory);
    }

    public NetworkAccessPointEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public NetworkAccessPointEntity getByTypeAndIdentifier(Short type, String identifier)
            throws AtnaPersistenceException {
        return uniqueResult(criteria().add(Restrictions.eq("type", type))
                .add(Restrictions.eq("identifier", identifier)));
    }

    public List<? extends NetworkAccessPointEntity> getByType(Short type) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("type", type)));
    }

    public List<? extends NetworkAccessPointEntity> getByIdentifier(String identifier)
            throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("identifier", identifier)));
    }

    public List<? extends NetworkAccessPointEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends NetworkAccessPointEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public void save(NetworkAccessPointEntity nap, PersistencePolicies policies) throws AtnaPersistenceException {
        if (worthSaving(nap) && !isDuplicate(nap, policies)) {
            currentSession().saveOrUpdate(nap);
        }
    }

    public void delete(NetworkAccessPointEntity nap) throws AtnaPersistenceException {
        currentSession().delete(nap);
    }


    private boolean worthSaving(NetworkAccessPointEntity nap) throws AtnaPersistenceException {
        // both are null - don't persist it
        if (nap.getIdentifier() == null && (nap.getType() < 1 && nap.getType() > 3)) {
            return false;
        }
        return true;
    }

    private boolean isDuplicate(NetworkAccessPointEntity nap, PersistencePolicies policies)
            throws AtnaPersistenceException {
        NetworkAccessPointEntity entity = getByTypeAndIdentifier(nap.getType(), nap.getIdentifier());
        if (entity != null) {
            if (policies.isErrorOnDuplicateInsert()) {
                throw new AtnaPersistenceException("Attempt to load duplicate network access point.",
                        AtnaPersistenceException.PersistenceError.DUPLICATE_NETWORK_ACCESS_POINT);
            }
            return true;
        }
        return false;
    }
}
