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

package org.openhealthtools.openatna.audit.trail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.MessageDao;
import org.openhealthtools.openatna.persistence.model.MessageEntity;
import org.openhealthtools.openatna.persistence.model.Query;
import org.openhealthtools.openatna.persistence.util.EntityConverter;

/**
 * Provides access to the trail of messages in the DB
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 11:27:42 PM
 * @date $Date:$ modified by $Author:$
 */

public class AuditTrail {

    private DaoFactory factory;

    public AuditTrail(DaoFactory factory) {
        this.factory = factory;
    }

    public List<AtnaMessage> getTrail(Query query) throws AtnaPersistenceException {
        List<AtnaMessage> msgs = new ArrayList<AtnaMessage>();
        MessageDao dao = factory.messageDao();
        List<? extends MessageEntity> ents = dao.getByQuery(query);
        for (MessageEntity ent : ents) {
            msgs.add(EntityConverter.createMessage(ent));
        }
        return msgs;
    }


    public Query between(Date start, Date end, Query query) {
        return query.between(start, end);
    }

    public Query eventIdCode(String code, Query query) {
        return query.equals(code, Query.Target.EVENT_ID_CODE);
    }

    public Query eventIdCodeSystem(String codeSystem, Query query) {
        return query.equals(codeSystem, Query.Target.EVENT_ID_CODE_SYSTEM);
    }

    public Query eventIdCodeSystemName(String codeSystemName, Query query) {
        return query.equals(codeSystemName, Query.Target.EVENT_ID_CODE_SYSTEM_NAME);
    }

    public Query eventOutcome(EventOutcome outcome, Query query) {
        return query.equals(outcome.value(), Query.Target.EVENT_OUTCOME);
    }

    public Query eventAction(EventAction action, Query query) {
        return query.equals(action.value(), Query.Target.EVENT_ACTION);
    }

    public Query patient(String id, Query query) {
        return query.equals(ObjectType.PERSON.value(), Query.Target.OBJECT_TYPE)
                .equals(ObjectTypeCodeRole.PATIENT.value(), Query.Target.OBJECT_TYPE_ROLE)
                .equals(id, Query.Target.OBJECT_ID);
    }


}
