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

import org.junit.Test;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.AtnaMessageDao;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;

import java.util.Date;


/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 7, 2009: 2:13:16 PM
 * @date $Date:$ modified by $Author:$
 */


public class TestMessage {

    @Test
    public void testMessage() throws AtnaPersistenceException {
        DaoFactory factory = SpringDaoFactory.getFactory();
        AtnaMessageDao dao = factory.atnaMessageDao();
        AtnaMessageEntity msgEnt = createMessage();

        SourceCodeEntity code = new SourceCodeEntity("1");
        code.setCodeSystemName("RFC-3881");
        AtnaSourceEntity asource = new AtnaSourceEntity(new SourceEntity("cat", code));
        msgEnt.addAtnaSource(asource);

        ParticipantCodeEntity pcode = new ParticipantCodeEntity("110150");
        pcode.setCodeSystemName("DCM");
        ParticipantEntity part = new ParticipantEntity("scmabh");
        part.setUserName("andrew");

        AtnaParticipantEntity pentity = new AtnaParticipantEntity(part);
        NetworkAccessPointEntity net = new NetworkAccessPointEntity(new Short("2"), "192.168.0.1");
        pentity.setNetworkAccessPoint(net);
        msgEnt.addAtnaParticipant(pentity);

        ObjectIdTypeCodeEntity ocode = new ObjectIdTypeCodeEntity("110180");
        ocode.setCodeSystemName("DCM");

        ObjectEntity obj = new ObjectEntity("obj1", ocode);
        obj.setObjectName("machine");
        obj.setObjectSensitivity("N");

        AtnaObjectEntity objEnt = new AtnaObjectEntity(obj);
        objEnt.setObjectDataLifeCycle(new Short("1"));
        objEnt.addObjectDetail(new ObjectDetailEntity("version", "1.2"));
        msgEnt.addAtnaObject(objEnt);

        dao.save(msgEnt);
    }

    @Test
    public void testMinimalMessage() throws AtnaPersistenceException {
        DaoFactory factory = SpringDaoFactory.getFactory();
        AtnaMessageDao dao = factory.atnaMessageDao();

        AtnaMessageEntity msgEnt = createMinimalMessage();

        SourceEntity source = new SourceEntity();
        source.setSourceId("cat");
        AtnaSourceEntity asource = new AtnaSourceEntity();
        asource.setSource(source);
        msgEnt.getAtnaSources().add(asource);

        ParticipantEntity part = new ParticipantEntity();
        part.setUserId("scmabh");
        AtnaParticipantEntity pentity = new AtnaParticipantEntity();
        pentity.setParticipant(part);
        msgEnt.getAtnaParticipants().add(pentity);

        ObjectEntity obj = new ObjectEntity();
        obj.setObjectId("obj1");
        AtnaObjectEntity objEnt = new AtnaObjectEntity();
        objEnt.setObject(obj);
        msgEnt.getAtnaObjects().add(objEnt);

        dao.save(msgEnt);
    }

    protected AtnaMessageEntity createMinimalMessage() {
        AtnaEventEntity msgEvt = new AtnaEventEntity();
        EventIdCodeEntity code = new EventIdCodeEntity();
        code.setCode("110108");
        code.setCodeSystemName("DCM");
        msgEvt.setEventId(code);

        msgEvt.setEventDateTime(new Date());
        msgEvt.setEventOutcome(0);
        AtnaMessageEntity msg = new AtnaMessageEntity();
        msg.setEvent(msgEvt);
        return msg;
    }

    protected AtnaMessageEntity createMessage() {
        AtnaEventEntity msgEvt = new AtnaEventEntity();
        EventIdCodeEntity code = new EventIdCodeEntity();
        code.setCode("110108");
        code.setCodeSystemName("DCM");
        msgEvt.setEventId(code);
        EventTypeCodeEntity evtType = new EventTypeCodeEntity();
        evtType.setCode("110120");
        evtType.setCodeSystemName("DCM");
        msgEvt.getEventTypeCodes().add(evtType);
        msgEvt.setEventDateTime(new Date());
        msgEvt.setEventActionCode("R");
        msgEvt.setEventOutcome(0);
        AtnaMessageEntity msg = new AtnaMessageEntity();
        msg.setEvent(msgEvt);
        return msg;
    }


}
