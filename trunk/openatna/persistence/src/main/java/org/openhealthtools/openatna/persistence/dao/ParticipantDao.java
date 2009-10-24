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
import org.openhealthtools.openatna.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.persistence.model.codes.ParticipantCodeEntity;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 6:19:55 PM
 * @date $Date:$ modified by $Author:$
 */
public interface ParticipantDao extends Dao {

    public ParticipantEntity getById(Long id) throws AtnaPersistenceException;

    public ParticipantEntity getByUserId(String userId) throws AtnaPersistenceException;

    public ParticipantEntity getByAltUserId(String altUserId) throws AtnaPersistenceException;

    public List<? extends ParticipantEntity> getByCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException;

    public List<? extends ParticipantEntity> getByUserName(String userName) throws AtnaPersistenceException;

    public List<? extends ParticipantEntity> getAll() throws AtnaPersistenceException;

    public void save(ParticipantEntity ap, PersistencePolicies policies) throws AtnaPersistenceException;

    public void delete(ParticipantEntity ap) throws AtnaPersistenceException;

}