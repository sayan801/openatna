/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */

package org.openhealthtools.openatna.all.test.persistence;

import org.junit.Test;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 1, 2009: 12:08:02 PM
 * @date $Date:$ modified by $Author:$
 */
public class QueryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTest.class);

    @Test
    public void testQuery() {

        Query query = new Query();
        query.notNull(Query.Target.EVENT_OUTCOME)
                .equals("ITI-8", Query.Target.EVENT_ID_CODE)
                .equals("IHE Transactions", Query.Target.EVENT_ID_CODE_SYSTEM_NAME)
                .equals(0, Query.Target.EVENT_OUTCOME)
                .equals("scmabh", Query.Target.PARTICIPANT_ID)
                .orderDescending(Query.Target.EVENT_TIME);
        MessageDao dao = AtnaFactory.messageDao();
        try {
            List<? extends MessageEntity> ents = dao.getByQuery(query);
            for (MessageEntity ent : ents) {
                LOGGER.info("Entity: '{}'", ent);
            }
        } catch (AtnaPersistenceException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
        }
    }
}
