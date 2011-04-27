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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.dao.ObjectDao;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.CodesUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 11:10:30 AM
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateObjectDao extends AbstractHibernateDao<ObjectEntity> implements ObjectDao {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.persistence.dao.hibernate.HibernateObjectDao");


    public HibernateObjectDao(SessionFactory sessionFactory) {
        super(ObjectEntity.class, sessionFactory);
    }

    public ObjectEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public ObjectEntity getByObjectId(String id) throws AtnaPersistenceException {
        return uniqueResult(criteria().add(Restrictions.eq("objectId", id)));
    }

    public ObjectEntity get(ObjectEntity other) throws AtnaPersistenceException {
        Criteria c = criteria();
        c.add(Restrictions.eq("objectId", other.getObjectId()));
        if (other.getObjectName() != null) {
            c.add(Restrictions.eq("objectName", other.getObjectName()));
        } else {
            c.add(Restrictions.isNull("objectName"));
        }
        if (other.getObjectTypeCode() != null) {
            c.add(Restrictions.eq("objectTypeCode", other.getObjectTypeCode()));
        } else {
            c.add(Restrictions.isNull("objectTypeCode"));
        }
        if (other.getObjectTypeCodeRole() != null) {
            c.add(Restrictions.eq("objectTypeCodeRole", other.getObjectTypeCodeRole()));
        } else {
            c.add(Restrictions.isNull("objectTypeCodeRole"));
        }
        if (other.getObjectSensitivity() != null) {
            c.add(Restrictions.eq("objectSensitivity", other.getObjectSensitivity()));
        } else {
            c.add(Restrictions.isNull("objectSensitivity"));
        }

        List<? extends ObjectEntity> ret = list(c);
        if (ret == null || ret.size() == 0) {
            return null;
        }
        for (ObjectEntity objectEntity : ret) {
            if (CodesUtils.equivalent(objectEntity.getObjectIdTypeCode(), other.getObjectIdTypeCode())) {
                return objectEntity;
            }
        }
        return null;
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

    public List<? extends ObjectEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends ObjectEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public List<? extends ObjectEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity)
            throws AtnaPersistenceException {
        return list(criteria().createCriteria("objectIdTypeCode").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    // TODO - check for INCONSISTENT_REPRESENTATION, e.g sensitivity
    public void save(ObjectEntity entity, PersistencePolicies policies) throws AtnaPersistenceException {
        ObjectIdTypeCodeEntity code = entity.getObjectIdTypeCode();
        if (code != null) {
            CodeDao dao = AtnaFactory.codeDao();
            CodeEntity existing = dao.get(code);
            if (existing == null) {
                if (policies.isAllowNewCodes()) {
                    dao.save(code, policies);
                } else {
                    throw new AtnaPersistenceException("no or unknown object id type code defined.",
                            AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                }
            } else {
                if (existing instanceof ObjectIdTypeCodeEntity) {
                    entity.setObjectIdTypeCode((ObjectIdTypeCodeEntity) existing);
                } else {
                    throw new AtnaPersistenceException("code is defined but is of a different type.",
                            AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                }
            }
        } else {
            throw new AtnaPersistenceException(code.toString(),
                    AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
        }

        if (entity.getVersion() == null) {
            // new one.
            ObjectEntity existing = get(entity);
            if (existing != null) {
                if (policies.isErrorOnDuplicateInsert()) {
                    throw new AtnaPersistenceException(entity.toString(),
                            AtnaPersistenceException.PersistenceError.DUPLICATE_OBJECT);
                } else {
                    return;
                }
            }
        }
        currentSession().saveOrUpdate(entity);
    }

    public void delete(ObjectEntity entity) throws AtnaPersistenceException {
        currentSession().delete(entity);
    }
}
