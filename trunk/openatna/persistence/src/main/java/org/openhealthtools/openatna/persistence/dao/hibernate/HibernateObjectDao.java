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

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.CodeDao;
import org.openhealthtools.openatna.persistence.dao.ObjectDao;
import org.openhealthtools.openatna.persistence.dao.PersistencePolicies;
import org.openhealthtools.openatna.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 11:10:30 AM
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateObjectDao extends AbstractHibernateDao<ObjectEntity> implements ObjectDao {

    public HibernateObjectDao(SessionFactory sessionFactory) {
        super(ObjectEntity.class, sessionFactory);
    }

    public ObjectEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public ObjectEntity getByObjectId(String id) throws AtnaPersistenceException {
        return uniqueResult(criteria().add(Restrictions.eq("objectId", id)));
    }

    public List<? extends ObjectEntity> getByName(String name) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("objectName", name)));
    }

    public List<? extends ObjectEntity> getByTypeCode(Short type) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("objectTypeCode", type)));
    }

    public List<? extends ObjectEntity> getByTypeCodeRole(Short type) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("objectTypeCodeRole", type)));
    }

    public List<? extends ObjectEntity> getBySensitivity(String sensitivity) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("objectSensitivity", sensitivity)));
    }

    public List<? extends ObjectEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("objectIdTypeCode").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    // TODO - check for INCONSISTENT_REPRESENTATION, e.g sensitivity
    public void save(ObjectEntity entity, PersistencePolicies policies) throws AtnaPersistenceException {

        CodeDao cd = SpringDaoFactory.getFactory().codeDao();
        ObjectIdTypeCodeEntity code = entity.getObjectIdTypeCode();
        if (code != null) {
            entity.setObjectIdTypeCode(null);
            code = (ObjectIdTypeCodeEntity) cd.find(code);
            if (code.getVersion() != null) {
                entity.setObjectIdTypeCode(code);
            } else {
                if (policies.isAllowNewCodes()) {
                    cd.save(code, policies);
                    entity.setObjectIdTypeCode(code);
                } else {
                    throw new AtnaPersistenceException(code.toString(), AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                }
            }
        } else {
            throw new AtnaPersistenceException(code.toString(), AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
        }

        if (entity.getVersion() == null) {
            // new one.
            ObjectEntity existing = getByObjectId(entity.getObjectId());
            if (existing != null) {
                if (policies.isErrorOnDuplicateInsert()) {
                    throw new AtnaPersistenceException(entity.toString(), AtnaPersistenceException.PersistenceError.DUPLICATE_OBJECT);
                } else {
                    return;
                }
            }
        } else {
            // from DB - update. All ok?
        }
        currentSession().saveOrUpdate(entity);
    }

    public void delete(ObjectEntity entity) throws AtnaPersistenceException {
        currentSession().delete(entity);
    }
}
