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

import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.model.MessageObjectEntity;
import org.openhealthtools.openatna.persistence.model.codes.ObjectIdTypeCodeEntity;

import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 6:19:55 PM
 * @date $Date:$ modified by $Author:$
 */
public interface MessageObjectDao extends Dao {

    public MessageObjectEntity getById(Long id) throws AtnaPersistenceException;

    public List<? extends MessageObjectEntity> getByIdTypeCode(ObjectIdTypeCodeEntity idEntity) throws AtnaPersistenceException;

    public List<? extends MessageObjectEntity> getByTypeCode(Short code) throws AtnaPersistenceException;

    public List<? extends MessageObjectEntity> getByTypeCodeRole(Short code) throws AtnaPersistenceException;

    public List<? extends MessageObjectEntity> getBySensitivity(String code) throws AtnaPersistenceException;

    public List<? extends MessageObjectEntity> getAll() throws AtnaPersistenceException;

    public void save(MessageObjectEntity ao) throws AtnaPersistenceException;

    public void delete(MessageObjectEntity ao) throws AtnaPersistenceException;

    public void normalize(MessageObjectEntity ao) throws AtnaPersistenceException;

}