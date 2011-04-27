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

package org.openhealthtools.openatna.all.test.persistence;

import java.util.Date;
import java.util.List;

import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.AtnaMessageObject;
import org.openhealthtools.openatna.anom.AtnaMessageParticipant;
import org.openhealthtools.openatna.anom.AtnaObject;
import org.openhealthtools.openatna.anom.AtnaObjectDetail;
import org.openhealthtools.openatna.anom.AtnaParticipant;
import org.openhealthtools.openatna.anom.AtnaSource;
import org.openhealthtools.openatna.anom.NetworkAccessPoint;
import org.openhealthtools.openatna.anom.ObjectDataLifecycle;
import org.openhealthtools.openatna.anom.ObjectType;
import org.openhealthtools.openatna.anom.ObjectTypeCodeRole;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageSourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectDetailEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.EntityConverter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 2, 2009: 9:40:08 AM
 * @date $Date:$ modified by $Author:$
 */

public class TestConvert {

    @Test
    public void testConvert() {

        MessageEntity msgEnt = createMessage();

        SourceCodeEntity code = new SourceCodeEntity("1");
        code.setCodeSystemName("RFC-3881");
        MessageSourceEntity asource = new MessageSourceEntity(new SourceEntity("cat1", code));
        asource.getSource().setEnterpriseSiteId("enterprise1");
        msgEnt.addMessageSource(asource);

        MessageSourceEntity bsource = new MessageSourceEntity(new SourceEntity("cat2", code));
        bsource.getSource().setEnterpriseSiteId("enterprise2");
        msgEnt.addMessageSource(bsource);

        ParticipantCodeEntity pcode = new ParticipantCodeEntity("110150");
        pcode.setCodeSystemName("DCM");
        ParticipantEntity part = new ParticipantEntity("scmabh");
        part.addParticipantTypeCode(pcode);
        part.setUserName("andrew");
        part.setAlternativeUserId("werdna");

        MessageParticipantEntity pentity = new MessageParticipantEntity(part);
        NetworkAccessPointEntity net = new NetworkAccessPointEntity(new Short("2"), "192.168.0.1");
        pentity.setNetworkAccessPoint(net);
        msgEnt.addMessageParticipant(pentity);

        ObjectIdTypeCodeEntity ocode = new ObjectIdTypeCodeEntity("110180");
        ocode.setCodeSystemName("DCM");

        ObjectEntity obj = new ObjectEntity("obj1", ocode);
        obj.setObjectName("machine");
        obj.setObjectSensitivity("N");
        obj.setObjectTypeCodeRole(new Short("1"));
        obj.setObjectTypeCode(new Short("1"));

        MessageObjectEntity objEnt = new MessageObjectEntity(obj);
        objEnt.setObjectDataLifeCycle(new Short("1"));
        objEnt.addObjectDetail(new ObjectDetailEntity("version", Base64.encodeString("1.2").getBytes()));
        objEnt.setObjectQuery(Base64.encodeString("a query string").getBytes());
        msgEnt.addMessageObject(objEnt);
        AtnaMessage msg = EntityConverter.createMessage(msgEnt);
        assertEquals(msg.getParticipants().size(), 1);
        assertEquals(msg.getSources().size(), 2);
        assertEquals(msg.getObjects().size(), 1);
        AtnaSource as = msg.getSource("cat1");
        assertEquals(as.getSourceId(), "cat1");
        assertEquals(as.getEnterpriseSiteId(), "enterprise1");
        assertEquals(as.getSourceTypeCodes().size(), 1);
        AtnaCode ac = as.getSourceTypeCodes().get(0);
        assertEquals(ac.getCode(), "1");
        assertEquals(ac.getCodeSystemName(), "RFC-3881");
        assertEquals(ac.getCodeSystem(), null);

        as = msg.getSource("cat2");
        assertEquals(as.getSourceId(), "cat2");
        assertEquals(as.getEnterpriseSiteId(), "enterprise2");
        assertEquals(as.getSourceTypeCodes().size(), 1);
        ac = as.getSourceTypeCodes().get(0);
        assertEquals(ac.getCode(), "1");
        assertEquals(ac.getCodeSystemName(), "RFC-3881");
        assertEquals(ac.getCodeSystem(), null);

        AtnaMessageParticipant amp = msg.getParticipants().get(0);
        AtnaParticipant ap = amp.getParticipant();
        assertEquals(ap.getUserId(), "scmabh");
        assertEquals(ap.getUserName(), "andrew");
        assertEquals(ap.getAlternativeUserId(), "werdna");
        assertEquals(ap.getRoleIDCodes().size(), 1);
        ac = ap.getRoleIDCodes().get(0);
        assertEquals(ac.getCode(), "110150");
        assertEquals(ac.getCodeSystemName(), "DCM");
        assertEquals(ac.getCodeSystem(), null);
        AtnaCode other = new AtnaCode(ac.getCodeType(), "110150", null, "DCM", null, null);
        assertEquals(ac, other);
        String napid = amp.getNetworkAccessPointId();
        assertEquals(napid, "192.168.0.1");
        assertEquals(amp.getNetworkAccessPointType(), NetworkAccessPoint.IP_ADDRESS);

        AtnaMessageObject amo = msg.getObject("obj1");
        assertEquals(amo.getObjectDataLifeCycle(), ObjectDataLifecycle.ORIGINATION);
        assertEquals(amo.getObjectDetails().size(), 1);
        List<AtnaObjectDetail> details = amo.getObjectDetails("version");
        assertEquals(details.size(), 1);
        AtnaObjectDetail detail = details.get(0);
        assertEquals(detail.getType(), "version");
        assertEquals(new String(detail.getValue()), Base64.encodeString("1.2"));
        AtnaObject oe = amo.getObject();
        assertEquals(oe.getObjectId(), "obj1");
        assertEquals(oe.getObjectName(), "machine");
        assertEquals(oe.getObjectSensitivity(), "N");
        assertEquals(oe.getObjectTypeCode(), ObjectType.PERSON);
        assertEquals(oe.getObjectTypeCodeRole(), ObjectTypeCodeRole.PATIENT);
        assertEquals(oe.getObjectIdTypeCode().getCode(), "110180");
        assertEquals(oe.getObjectIdTypeCode().getCodeSystemName(), "DCM");
        assertEquals(new String(amo.getObjectQuery()), Base64.encodeString("a query string"));


        MessageEntity msgEntity = EntityConverter.createMessage(msg);
        assertEquals(msgEnt, msgEntity);

        AtnaMessage am = EntityConverter.createMessage(msgEntity);
        assertEquals(msg, am);

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
