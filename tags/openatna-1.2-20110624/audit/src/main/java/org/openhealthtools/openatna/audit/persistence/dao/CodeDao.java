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

package org.openhealthtools.openatna.audit.persistence.dao;

import java.util.List;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 5:30:07 PM
 * @date $Date:$ modified by $Author:$
 */
public interface CodeDao extends Dao {

    public CodeEntity getById(Long id) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByType(CodeEntity.CodeType type)
            throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByCode(String code) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByCodeAndType(CodeEntity.CodeType type, String code) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByCodeSystem(String codeSystem) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByCodeSystemName(String codeSystemName)
            throws AtnaPersistenceException;

    public CodeEntity getByCodeAndSystem(CodeEntity.CodeType type, String code, String codeSystem)
            throws AtnaPersistenceException;

    public CodeEntity getByCodeAndSystemName(CodeEntity.CodeType type, String code, String codeSystemName)
            throws AtnaPersistenceException;

    public CodeEntity getByCodeAndSystemAndSystemName(CodeEntity.CodeType type, String code, String codeSystem,
                                                      String codeSystemName) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getBySystemAndType(String codeSystem, CodeEntity.CodeType type)
            throws AtnaPersistenceException;

    public List<? extends CodeEntity> getBySystemNameAndType(String codeSystemName, CodeEntity.CodeType type)
            throws AtnaPersistenceException;

    public List<? extends CodeEntity> getAll() throws AtnaPersistenceException;

    public List<? extends CodeEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    public boolean save(CodeEntity code, PersistencePolicies policies) throws AtnaPersistenceException;

    public void delete(CodeEntity code) throws AtnaPersistenceException;

    public CodeEntity get(CodeEntity code) throws AtnaPersistenceException;

    public CodeEntity find(CodeEntity code) throws AtnaPersistenceException;

}
