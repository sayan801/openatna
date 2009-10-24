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

package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.AuditException;
import org.openhealthtools.openatna.audit.AuditService;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.MessageDao;
import org.openhealthtools.openatna.persistence.dao.PersistencePolicies;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.MessageEntity;
import org.openhealthtools.openatna.persistence.util.EntityConverter;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 9:16:14 PM
 * @date $Date:$ modified by $Author:$
 */

public class PersistenceProcessor implements AtnaProcessor {

    public synchronized void process(ProcessContext context) throws Exception {
        AtnaMessage msg = context.getMessage();
        if (msg == null) {
            throw new AuditException("no message", null, AuditException.AuditError.NULL_MESSAGE);
        }
        MessageEntity entity = EntityConverter.createMessage(msg);
        if (entity != null) {
            DaoFactory fac = context.getProperty(AuditService.PROPERTY_DAO_FACTORY,
                    DaoFactory.class);
            if (fac == null) {
                fac = SpringDaoFactory.getFactory();
            }
            PersistencePolicies pp = context.getProperty(AuditService.PROPERTY_PERSISTENCE_POLICIES,
                    PersistencePolicies.class);
            if (pp == null) {
                pp = new PersistencePolicies();
            }
            MessageDao dao = fac.messageDao();
            if (dao != null) {
                dao.save(entity, pp);
                context.setState(ProcessContext.State.PERSISTED);
            } else {
                throw new AuditException("Message Data Access Object could not be created",
                        msg, AuditException.AuditError.INVALID_MESSAGE);
            }
        } else {
            throw new AuditException("Message Entity could not be created",
                    msg, AuditException.AuditError.INVALID_MESSAGE);
        }
    }

    public void error(ProcessContext context) {
    }

}