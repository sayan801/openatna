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

import java.util.List;

import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.model.SourceEntity;
import org.openhealthtools.openatna.persistence.model.codes.SourceCodeEntity;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 8, 2009: 6:20:53 PM
 * @date $Date:$ modified by $Author:$
 */
public interface SourceDao {

    public SourceEntity getById(Long id) throws AtnaPersistenceException;

    public SourceEntity getBySourceId(String id) throws AtnaPersistenceException;

    public SourceEntity getByEnterpriseSiteId(String id) throws AtnaPersistenceException;

    public List<? extends SourceEntity> getByCode(SourceCodeEntity code) throws AtnaPersistenceException;

    public void save(SourceEntity entity, PersistencePolicies policies) throws AtnaPersistenceException;

    public void delete(SourceEntity entity) throws AtnaPersistenceException;
}
