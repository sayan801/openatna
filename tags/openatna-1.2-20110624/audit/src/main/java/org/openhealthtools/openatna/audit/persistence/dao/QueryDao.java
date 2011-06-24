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
import org.openhealthtools.openatna.audit.persistence.model.QueryEntity;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 8, 2010: 9:48:15 AM
 */
public interface QueryDao {

    public QueryEntity getById(Long id) throws AtnaPersistenceException;

    public void delete(QueryEntity pe) throws AtnaPersistenceException;

    public List<? extends QueryEntity> getAll() throws AtnaPersistenceException;

    public List<? extends QueryEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    public void save(QueryEntity pe) throws AtnaPersistenceException;


}