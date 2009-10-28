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
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.CodeDao;
import org.openhealthtools.openatna.persistence.dao.PersistencePolicies;
import org.openhealthtools.openatna.persistence.dao.SourceDao;
import org.openhealthtools.openatna.persistence.model.SourceEntity;
import org.openhealthtools.openatna.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.SourceCodeEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 10:39:44 AM
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateSourceDao extends AbstractHibernateDao<SourceEntity> implements SourceDao {

    public HibernateSourceDao(SessionFactory sessionFactory) {
        super(SourceEntity.class, sessionFactory);
    }

    public SourceEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public SourceEntity getBySourceId(String id) throws AtnaPersistenceException {
        return uniqueResult(criteria().add(Restrictions.eq("sourceId", id)));

    }

    public SourceEntity getByEnterpriseSiteId(String id) throws AtnaPersistenceException {
        return uniqueResult(criteria().add(Restrictions.eq("enterpriseSiteId", id)));
    }

    public List<? extends SourceEntity> getByCode(SourceCodeEntity codeEntity) throws AtnaPersistenceException {
        return list(criteria().createCriteria("sourceTypeCodes").add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public void save(SourceEntity entity, PersistencePolicies policies) throws AtnaPersistenceException {
        Set<SourceCodeEntity> codes = entity.getSourceTypeCodes();
        if (codes.size() > 0) {
            CodeDao cd = SpringDaoFactory.getFactory().codeDao();
            for (SourceCodeEntity code : codes) {
                codes.remove(code);
                CodeEntity ce = cd.find(code);
                if (!(ce instanceof SourceCodeEntity)) {
                    throw new AtnaPersistenceException(ce.toString(), AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                }
                code = (SourceCodeEntity) ce;
                if (code.getVersion() != null) {
                    codes.add(code);
                } else {
                    if (policies.isAllowNewCodes()) {
                        cd.save(code, policies);
                        codes.add(code);
                    } else {
                        throw new AtnaPersistenceException(code.toString(), AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                    }
                }
            }
            entity.setSourceTypeCodes(codes);
        }
        if (entity.getVersion() == null) {
            // new one.
            SourceEntity existing = getBySourceId(entity.getSourceId());
            if (existing != null) {
                if (policies.isErrorOnDuplicateInsert()) {
                    throw new AtnaPersistenceException(entity.toString(), AtnaPersistenceException.PersistenceError.DUPLICATE_SOURCE);
                } else {
                    return;
                }
            }
        } else {
            // from DB - update. All ok?
        }
        currentSession().saveOrUpdate(entity);
    }

    public void delete(SourceEntity entity) throws AtnaPersistenceException {
        currentSession().delete(entity);
    }


}