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
import org.openhealthtools.openatna.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.SourceCodeEntity;

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

        SourceEntity source = new SourceEntity();
        source.setSourceId("localhost");
        SourceCodeEntity code = new SourceCodeEntity();
        code.setCode("E");
        code.setCodeSystem("101");
        source.getSourceTypeCodes().add(code);
        AtnaSourceEntity asource = new AtnaSourceEntity();
        asource.setSource(source);
        msgEnt.getAtnaSources().add(asource);


        ParticipantEntity part = new ParticipantEntity();
        part.setUserId("scmabh");
        part.setUserName("andrew");
        ParticipantCodeEntity pcode = new ParticipantCodeEntity();
        pcode.setCode("D");
        pcode.setCodeSystem("101");
        part.getCodes().add(pcode);
        AtnaParticipantEntity pentity = new AtnaParticipantEntity();
        pentity.setParticipant(part);
        NetworkAccessPointEntity net = new NetworkAccessPointEntity();
        net.setIdentifier("192.168.0.1");
        net.setType(new Short("1"));
        pentity.setNetworkAccessPoint(net);
        msgEnt.getAtnaParticipants().add(pentity);

        dao.save(msgEnt);


    }

    protected AtnaMessageEntity createMessage() {
        AtnaEventEntity msgEvt = new AtnaEventEntity();
        EventIdCodeEntity code = new EventIdCodeEntity();
        code.setCode("A");
        code.setCodeSystem("101");
        msgEvt.setEventId(code);
        EventTypeCodeEntity evtType = new EventTypeCodeEntity();
        evtType.setCode("B");
        evtType.setCodeSystem("101");
        msgEvt.getEventTypeCodes().add(evtType);
        msgEvt.setEventDateTime(new Date());
        msgEvt.setEventActionCode("R");
        msgEvt.setEventOutcome(0);
        AtnaMessageEntity msg = new AtnaMessageEntity();
        msg.setEvent(msgEvt);
        return msg;
    }


}
