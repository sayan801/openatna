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

package org.openhealthtools.openatna.persistence.test;

import java.util.List;

import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.MessageDao;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.MessageEntity;
import org.openhealthtools.openatna.persistence.model.Query;
import org.junit.Test;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 1, 2009: 12:08:02 PM
 * @date $Date:$ modified by $Author:$
 */

public class QueryTest {

    @Test
    public void testQuery() {

        Query query = new Query();
        query.notNull(Query.Target.EVENT_OUTCOME)
                .equals("ITI-8", Query.Target.EVENT_ID_CODE)
                .equals("IHE Transactions", Query.Target.EVENT_ID_CODE_SYSTEM_NAME)
                .equals(0, Query.Target.EVENT_OUTCOME)
                .equals("scmabh", Query.Target.PARTICIPANT_ID)
                .orderDescending(Query.Target.EVENT_TIME);
        SpringDaoFactory fac = SpringDaoFactory.getFactory();
        MessageDao dao = fac.messageDao();
        try {
            List<? extends MessageEntity> ents = dao.getByQuery(query);
            for (MessageEntity ent : ents) {
                System.out.println(ent);
            }
        } catch (AtnaPersistenceException e) {
            e.printStackTrace();
        }

    }
}
