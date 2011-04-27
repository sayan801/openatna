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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * NOTE: the fields that determine a code's uniqueness are its code, code system AND system name.
 * Two codes with the same code and code system but with different system names are NOT considered equal.
 * Is this right????
 * <p/>
 * Codes with only a code defined which have the same code, are not considered equal.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 1:26:08 PM
 * @date $Date:$ modified by $Author:$
 */

@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateCodeDao extends AbstractHibernateDao<CodeEntity> implements CodeDao {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.persistence.dao.hibernate.HibernateCodeDao");


    public HibernateCodeDao(SessionFactory sessionFactory) {
        super(CodeEntity.class, sessionFactory);
    }

    public CodeEntity getById(Long id) throws AtnaPersistenceException {
        return get(id);
    }

    public List<? extends CodeEntity> getByType(CodeEntity.CodeType type) throws AtnaPersistenceException {
        return list(criteria(fromCodeType(type)));
    }

    public List<? extends CodeEntity> getByCode(String code) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("code", code)));
    }

    public List<? extends CodeEntity> getByCodeAndType(CodeEntity.CodeType type, String code) throws AtnaPersistenceException {
        // first see if someone was lazy and didn't use the name of the RFC
        List<? extends CodeEntity> ce = list(criteria()
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("code", code))
                .add(Restrictions.eq("codeSystemName", "RFC-3881")));
        if (ce != null) {
            return ce;
        }
        // look for codes with an empty system name
        ce = list(criteria()
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("code", code))
                .add(Restrictions.eq("codeSystemName", "")));
        if (ce != null) {
            return ce;
        }
        // look for codes with a null system name
        return list(criteria()
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("code", code))
                .add(Restrictions.isNull("codeSystemName")));
    }

    public List<? extends CodeEntity> getByCodeSystem(String codeSystem) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("codeSystem", codeSystem)));
    }

    public List<? extends CodeEntity> getByCodeSystemName(String codeSystemName) throws AtnaPersistenceException {
        return list(criteria().add(Restrictions.eq("codeSystemName", codeSystemName)));
    }

    public CodeEntity getByCodeAndSystem(CodeEntity.CodeType type, String code, String codeSystem)
            throws AtnaPersistenceException {
        return uniqueResult(criteria()
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("code", code))
                .add(Restrictions.eq("codeSystem", codeSystem)));
    }

    public CodeEntity getByCodeAndSystemName(CodeEntity.CodeType type, String code, String codeSystemName)
            throws AtnaPersistenceException {
        return uniqueResult(criteria()
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("code", code))
                .add(Restrictions.eq("codeSystemName", codeSystemName)));

    }

    public List<? extends CodeEntity> getBySystemAndType(String codeSystem, CodeEntity.CodeType type)
            throws AtnaPersistenceException {
        return list(criteria(fromCodeType(type)).add(Restrictions.eq("codeSystem", codeSystem)));
    }

    public List<? extends CodeEntity> getBySystemNameAndType(String codeSystemName, CodeEntity.CodeType type)
            throws AtnaPersistenceException {
        return list(criteria(fromCodeType(type)).add(Restrictions.eq("codeSystemName", codeSystemName)));
    }

    public CodeEntity getByCodeAndSystemAndSystemName(CodeEntity.CodeType type, String code, String codeSystem,
                                                      String codeSystemName) throws AtnaPersistenceException {
        return uniqueResult(criteria().add(Restrictions.eq("codeSystemName", codeSystemName))
                .add(Restrictions.eq("code", code))
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("codeSystem", codeSystem)));
    }


    public List<? extends CodeEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends CodeEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    /**
     * this will only save new codes (based on on code, system and system name), or update codes that have
     * been drawn from the DB. Once a code is stored, it cannot have its code, system or system name changed.
     * Otherwise it will be considered a new code.
     *
     * @param ce
     * @throws AtnaPersistenceException
     */
    public boolean save(CodeEntity ce, PersistencePolicies policies) throws AtnaPersistenceException {
        if (!isDuplicate(ce, policies)) {
            currentSession().saveOrUpdate(ce);
            return true;
        }
        return false;
    }

    public void delete(CodeEntity ce) throws AtnaPersistenceException {
        currentSession().delete(ce);
    }

    /**
     * This does an ever decreasingly strict search. It will match against a matching code
     * and a system name as a last resort.
     *
     * @param code
     * @return
     * @throws AtnaPersistenceException
     */
    public CodeEntity get(CodeEntity code) throws AtnaPersistenceException {
        String c = code.getCode();
        String sys = code.getCodeSystem();
        String name = code.getCodeSystemName();
        if (c == null) {
            throw new AtnaPersistenceException("no code in code entity",
                    AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
        }
        if ((sys == null || sys.length() == 0) && (name == null || name.length() == 0)) {
            List<? extends CodeEntity> l = getByCodeAndType(code.getType(), c);
            if (l.size() == 1) {
                return l.get(0);
            }
        }
        CodeEntity ret = null;
        if ((sys != null && sys.length() > 0 && (name != null && name.length() > 0))) {
            ret = getByCodeAndSystemAndSystemName(code.getType(), c, sys, name);
        }
        if (ret != null) {
            return ret;
        }
        if (sys != null && sys.length() > 0) {
            ret = getByCodeAndSystem(code.getType(), c, sys);
        }
        if (ret != null) {
            return ret;
        }
        if (name != null && name.length() > 0) {
            ret = getByCodeAndSystemName(code.getType(), c, name);
        }
        if (ret != null) {
            return ret;
        }
        return ret;
    }

    private Class fromCodeType(CodeEntity.CodeType type) {
        switch (type) {
            case EVENT_ID:
                return EventIdCodeEntity.class;
            case EVENT_TYPE:
                return EventTypeCodeEntity.class;
            case AUDIT_SOURCE:
                return SourceCodeEntity.class;
            case ACTIVE_PARTICIPANT:
                return ParticipantCodeEntity.class;
            case PARTICIPANT_OBJECT_ID_TYPE:
                return ObjectIdTypeCodeEntity.class;
            default:
                return CodeEntity.class;
        }
    }

    private boolean isDuplicate(CodeEntity entity, PersistencePolicies policies) throws AtnaPersistenceException {
        CodeEntity ce = get(entity);
        if (ce != null) {
            if (policies.isErrorOnDuplicateInsert()) {
                throw new AtnaPersistenceException("Attempt to load duplicate Code Entity",
                        AtnaPersistenceException.PersistenceError.DUPLICATE_CODE);
            }
            return true;
        }
        return false;
    }

    /**
     * returns a matching code in the DB if one is there.
     *
     * @param entity
     * @return
     * @throws org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException
     *
     */
    public CodeEntity find(CodeEntity entity) throws AtnaPersistenceException {
        CodeEntity ce = get(entity);
        if (ce != null) {
            return ce;
        }
        return entity;
    }
}
