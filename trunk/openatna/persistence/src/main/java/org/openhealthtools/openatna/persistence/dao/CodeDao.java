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

package org.openhealthtools.openatna.persistence.dao;

import org.openhealthtools.openatna.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;

import java.util.List;

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

    public List<? extends CodeEntity> getByType(CodeEntity.CodeType type) throws AtnaPersistenceException;

    public CodeEntity getByCode(String code) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByCodeSystem(String codeSystem) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getByCodeSystemName(String codeSystemName) throws AtnaPersistenceException;

    public CodeEntity getByCodeAndSystem(String code, String codeSystem) throws AtnaPersistenceException;

    public CodeEntity getByCodeAndSystemName(String code, String codeSystemName) throws AtnaPersistenceException;

    public CodeEntity getByCodeAndSystemAndSystemName(String code, String codeSystem, String codeSystemName) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getBySystemAndType(String codeSystem, CodeEntity.CodeType type) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getBySystemNameAndType(String codeSystemName, CodeEntity.CodeType type) throws AtnaPersistenceException;

    public List<? extends CodeEntity> getAll() throws AtnaPersistenceException;

    public void save(CodeEntity code) throws AtnaPersistenceException;

    public void delete(CodeEntity code) throws AtnaPersistenceException;

    public CodeEntity get(CodeEntity code) throws AtnaPersistenceException;

    public CodeEntity find(CodeEntity code) throws AtnaPersistenceException;

}
