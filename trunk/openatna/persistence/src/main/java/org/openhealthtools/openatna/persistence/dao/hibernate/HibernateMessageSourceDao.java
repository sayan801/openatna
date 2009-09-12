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
import org.openhealthtools.openatna.persistence.dao.MessageSourceDao;
import org.openhealthtools.openatna.persistence.dao.SourceDao;
import org.openhealthtools.openatna.persistence.model.MessageSourceEntity;
import org.openhealthtools.openatna.persistence.model.SourceEntity;
import org.openhealthtools.openatna.persistence.model.codes.SourceCodeEntity;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 12:20:31 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateMessageSourceDao extends AbstractHibernateDao<MessageSourceEntity> implements MessageSourceDao {

    public HibernateMessageSourceDao(SessionFactory sessionFactory) {
        super(MessageSourceEntity.class, sessionFactory);
    }

    public MessageSourceEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public MessageSourceEntity getBySourceId(String sourceId) throws AtnaPersistenceException {
        return uniqueResult(criteria().createCriteria("source").add(Restrictions.eq("sourceId", sourceId)));
    }

    public MessageSourceEntity getByEnterpriseSiteId(String enterpriseSiteId) throws AtnaPersistenceException {
        return uniqueResult(criteria().createCriteria("source").add(Restrictions.eq("enterpriseSiteId", enterpriseSiteId)));
    }

    public List<? extends MessageSourceEntity> getByCode(SourceCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("source").createCriteria("sourceTypeCodes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends MessageSourceEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public void save(MessageSourceEntity as) throws AtnaPersistenceException {
        normalize(as);
        currentSession().saveOrUpdate(as);
    }

    /**
     * it is here, but in general audit messages should not be deleted
     *
     * @param as
     * @throws AtnaPersistenceException
     */
    public void delete(MessageSourceEntity as) throws AtnaPersistenceException {
        currentSession().delete(as);
    }

    public void normalize(MessageSourceEntity as) throws AtnaPersistenceException {
        if (as.getSource() == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NO_SOURCE);
        }
        if (as.getId() != null) {
            // hmm. Should not be able to modify audit messages?
            // this is a bit harsh. There will be situations where certain users could do this.
            throw new AtnaPersistenceException("audit messages cannot be modified.", AtnaPersistenceException.PersistenceError.UNMODIFIABLE);

        }
        SourceEntity se = as.getSource();
        SourceDao dao = SpringDaoFactory.getFactory().sourceDao();
        SourceEntity existing = dao.getBySourceId(se.getSourceId());
        if (existing == null) {
            throw new AtnaPersistenceException("no audit source defined.", AtnaPersistenceException.PersistenceError.NON_EXISTENT_SOURCE);
        } else {
            as.setSource(existing);
        }
    }
}
