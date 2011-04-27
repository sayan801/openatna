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
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 5:39:35 PM
 * @date $Date:$ modified by $Author:$
 */
public interface MessageDao extends Dao {

    public List<? extends MessageEntity> getByQuery(Query query) throws AtnaPersistenceException;

    public MessageEntity getById(Long id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getAll() throws AtnaPersistenceException;

    public List<? extends MessageEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventId(EventIdCodeEntity idEntity) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getBySourceAddress(String address) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventType(EventTypeCodeEntity typeEntity)
            throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventOutcome(Integer outcome) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByEventAction(String action) throws AtnaPersistenceException;


    public List<? extends MessageEntity> getByParticipantUserId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByParticipantAltUserId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByParticipantCode(ParticipantCodeEntity codeEntity)
            throws AtnaPersistenceException;


    public List<? extends MessageEntity> getByAuditSourceId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByAuditSourceEnterpriseId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByAuditSourceCode(SourceCodeEntity codeEntity)
            throws AtnaPersistenceException;


    public List<? extends MessageEntity> getByObjectId(String id) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity codeEntity)
            throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectTypeCode(Short code) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectTypeCodeRole(Short code) throws AtnaPersistenceException;

    public List<? extends MessageEntity> getByObjectSensitivity(String sensitivity) throws AtnaPersistenceException;


    public void save(MessageEntity messageEntity, PersistencePolicies policies) throws AtnaPersistenceException;

    public void delete(MessageEntity messageEntity) throws AtnaPersistenceException;

}
