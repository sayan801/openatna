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
import org.openhealthtools.openatna.persistence.model.MessageEntity;
import org.openhealthtools.openatna.persistence.model.codes.*;

import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 5:39:35 PM
 * @date $Date:$ modified by $Author:$
 */
public interface MessageDao extends Dao {

    public MessageEntity getById(Long id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getAll() throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventId(EventIdCodeEntity idEntity) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventType(EventTypeCodeEntity typeEntity) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventOutcome(Integer outcome) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventAction(String action) throws AtnaPersistenceException;


    public List<? extends MessageEntity> getByParticipantUserId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByParticipantAltUserId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByParticipantCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException;


    public List<? extends MessageEntity> getByAuditSourceId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByAuditSourceEnterpriseId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByAuditSourceCode(SourceCodeEntity codeEntity) throws AtnaPersistenceException;


    public List<? extends MessageEntity> getByObjectId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectTypeCode(Short code) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectTypeCodeRole(Short code) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectSensitivity(String sensitivity) throws AtnaPersistenceException;


    public void save(MessageEntity messageEntity) throws AtnaPersistenceException;

    public void delete(MessageEntity messageEntity) throws AtnaPersistenceException;

    public void normalize(MessageEntity messageEntity) throws AtnaPersistenceException;

}