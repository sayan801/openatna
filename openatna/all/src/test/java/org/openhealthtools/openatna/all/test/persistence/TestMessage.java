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
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.*;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 7, 2009: 2:13:16 PM
 * @date $Date:$ modified by $Author:$
 */


public class TestMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMessage.class);

    @Test
    public void testMessage() throws AtnaPersistenceException {
        MessageDao dao = AtnaFactory.messageDao();
        MessageEntity msgEnt = createMessage();

        SourceCodeEntity code = new SourceCodeEntity("1");
        code.setCodeSystemName("RFC-3881");
        MessageSourceEntity asource = new MessageSourceEntity(new SourceEntity("cat", code));
        msgEnt.addMessageSource(asource);

        ParticipantCodeEntity pcode = new ParticipantCodeEntity("110150");
        pcode.setCodeSystemName("DCM");
        ParticipantEntity part = new ParticipantEntity("scmabh");
        part.setUserName("andrew");

        MessageParticipantEntity pentity = new MessageParticipantEntity(part);
        NetworkAccessPointEntity net = new NetworkAccessPointEntity(new Short("2"), "192.168.0.1");
        pentity.setNetworkAccessPoint(net);
        msgEnt.addMessageParticipant(pentity);

        ObjectIdTypeCodeEntity ocode = new ObjectIdTypeCodeEntity("110180");
        ocode.setCodeSystemName("DCM");

        ObjectEntity obj = new ObjectEntity("obj1", ocode);
        obj.setObjectName("machine");
        obj.setObjectSensitivity("N");

        MessageObjectEntity objEnt = new MessageObjectEntity(obj);
        objEnt.setObjectDataLifeCycle(new Short("1"));
        objEnt.addObjectDetail(new ObjectDetailEntity("version", Base64.encodeString("1.2").getBytes()));
        msgEnt.addMessageObject(objEnt);

        PersistencePolicies pp = new PersistencePolicies();
        pp.setAllowNewParticipants(true);
        pp.setAllowNewNetworkAccessPoints(true);
        pp.setAllowNewSources(true);
        pp.setAllowNewObjects(true);
        dao.save(msgEnt, pp);
    }

    @Test
    public void testMinimalMessage() throws AtnaPersistenceException {
        MessageDao dao = AtnaFactory.messageDao();

        MessageEntity msgEnt = createMinimalMessage();

        SourceEntity source = new SourceEntity();
        source.setSourceId("cat");
        MessageSourceEntity asource = new MessageSourceEntity();
        asource.setSource(source);
        msgEnt.getMessageSources().add(asource);

        ParticipantEntity part = new ParticipantEntity();
        part.setUserId("scmabh");
        MessageParticipantEntity pentity = new MessageParticipantEntity();
        pentity.setParticipant(part);
        msgEnt.getMessageParticipants().add(pentity);

        ObjectEntity obj = new ObjectEntity();
        obj.setObjectId("obj1");
        MessageObjectEntity objEnt = new MessageObjectEntity();
        objEnt.setObject(obj);
        msgEnt.getMessageObjects().add(objEnt);

        PersistencePolicies pp = new PersistencePolicies();
        pp.setAllowNewParticipants(true);
        pp.setAllowNewNetworkAccessPoints(true);
        pp.setAllowNewSources(true);
        pp.setAllowNewObjects(true);
        dao.save(msgEnt, pp);
    }

    @Test
    public void testQuery0() {
        try {
            MessageDao dao = AtnaFactory.messageDao();
            List<? extends MessageEntity> l = dao.getAll();
            for (MessageEntity entity : l) {
                LOGGER.info("Entity: '{}'", entity);
                LOGGER.info("=========================");
            }
        } catch (AtnaPersistenceException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
        }


    }

    protected MessageEntity createMinimalMessage() {
        MessageEntity msg = new MessageEntity();

        EventIdCodeEntity code = new EventIdCodeEntity();
        code.setCode("110108");
        code.setCodeSystemName("DCM");
        msg.setEventId(code);
        msg.setEventDateTime(new Date());
        msg.setEventOutcome(0);
        return msg;
    }

    protected MessageEntity createMessage() {
        MessageEntity msg = new MessageEntity();
        EventIdCodeEntity code = new EventIdCodeEntity();
        code.setCode("110108");
        code.setCodeSystemName("DCM");
        msg.setEventId(code);
        EventTypeCodeEntity evtType = new EventTypeCodeEntity();
        evtType.setCode("110120");
        evtType.setCodeSystemName("DCM");
        msg.getEventTypeCodes().add(evtType);
        msg.setEventDateTime(new Date());
        msg.setEventActionCode("R");
        msg.setEventOutcome(0);
        return msg;
    }


}
