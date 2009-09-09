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

import org.openhealthtools.openatna.persistence.dao.CodeDao;
import org.openhealthtools.openatna.persistence.dao.ParticipantDao;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 7, 2009: 2:13:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class Test {

    public static void main(String[] args) throws AtnaPersistenceException {
        DaoFactory factory = SpringDaoFactory.getFactory();
        CodeDao dao = factory.codeDao();
        ParticipantCodeEntity code = new ParticipantCodeEntity();
        code.setCode("myEventCode");
        code.setCodeSystem("myEventCodeSystem");
        dao.save(code);

        code = (ParticipantCodeEntity) dao.getByCode("myEventCode");
        System.out.println("event code code system=" + code.getCodeSystem());

        code = (ParticipantCodeEntity) dao.getByCodeAndSystem("myEventCode", "myEventCodeSystem");
        System.out.println("event code code system=" + code.getCodeSystem());

        List<? extends CodeEntity> eventIds = dao.getBySystemAndType("myEventCodeSystem", CodeEntity.CodeType.ACTIVE_PARTICIPANT);
        for (CodeEntity eventId : eventIds) {
            System.out.println("event code code system=" + eventId.getCodeSystem());
            System.out.println("event code code=" + eventId.getCode());

        }
        ParticipantEntity pe = new ParticipantEntity();
        pe.setUserId("user-1");
        pe.setUserName("andrew");
        Set<ParticipantCodeEntity> codes = new HashSet<ParticipantCodeEntity>();
        //codes.add(code);

        ParticipantCodeEntity code1 = new ParticipantCodeEntity();
        code1.setCode("myEventCode");
        code1.setCodeSystem("myEventCodeSystem");
        codes.add(code1);
        System.out.println("Test.main added code not in the DB, but identical to existing one.");
        System.out.println("Test.main added new code. How many are there now? (should be only one still):" + codes.size());

        pe.setCodes(codes);
        ParticipantDao partdao = factory.participantDao();
        partdao.save(pe);

        List<? extends ParticipantEntity> ps = partdao.getByCode(code);
        for (ParticipantEntity p : ps) {
            System.out.println("got participant based on the code for " + p);
        }

        
    }
}
