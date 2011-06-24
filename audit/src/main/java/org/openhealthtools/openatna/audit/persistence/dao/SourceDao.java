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
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 8, 2009: 6:20:53 PM
 * @date $Date:$ modified by $Author:$
 */
public interface SourceDao {

    public SourceEntity getById(Long id) throws AtnaPersistenceException;

    public List<? extends SourceEntity> getBySourceId(String id) throws AtnaPersistenceException;

    public SourceEntity getByEnterpriseSiteId(String id) throws AtnaPersistenceException;

    public SourceEntity get(SourceEntity other) throws AtnaPersistenceException;

    public List<? extends SourceEntity> getByCode(SourceCodeEntity code) throws AtnaPersistenceException;

    public List<? extends SourceEntity> getAll() throws AtnaPersistenceException;

    public List<? extends SourceEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    public void save(SourceEntity entity, PersistencePolicies policies) throws AtnaPersistenceException;

    public void delete(SourceEntity entity) throws AtnaPersistenceException;
}
