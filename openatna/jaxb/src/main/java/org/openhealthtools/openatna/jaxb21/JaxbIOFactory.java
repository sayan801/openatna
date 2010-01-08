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

package org.openhealthtools.openatna.jaxb21;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaIOFactory;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.AtnaMessageObject;
import org.openhealthtools.openatna.anom.AtnaMessageParticipant;
import org.openhealthtools.openatna.anom.AtnaObject;
import org.openhealthtools.openatna.anom.AtnaObjectDetail;
import org.openhealthtools.openatna.anom.AtnaParticipant;
import org.openhealthtools.openatna.anom.AtnaSource;
import org.openhealthtools.openatna.anom.EventAction;
import org.openhealthtools.openatna.anom.EventOutcome;
import org.openhealthtools.openatna.anom.NetworkAccessPoint;
import org.openhealthtools.openatna.anom.ObjectDataLifecycle;
import org.openhealthtools.openatna.anom.ObjectType;
import org.openhealthtools.openatna.anom.ObjectTypeCodeRole;
import org.openhealthtools.openatna.anom.ProvisionalMessage;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 11:18:47 AM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbIOFactory implements AtnaIOFactory {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.jaxb21.JaxbIOFactory");


    static JAXBContext jc;

    static {
        try {
            jc = JAXBContext.newInstance("org.openhealthtools.openatna.jaxb21");
        } catch (JAXBException e) {
            // this is fatal and our fault
            throw new RuntimeException("Error creating JAXB context:", e);
        }
    }

    public AtnaMessage read(InputStream in) throws AtnaException, IOException {
        if (jc == null) {
            throw new AtnaException("Could not create Jaxb Context");
        }
        try {
            Document doc = newDocument(in);
            if (doc.getDocumentElement().getTagName().equalsIgnoreCase("IHEYr4")) {
                return createProv(doc);
            }
            Unmarshaller u = jc.createUnmarshaller();
            AuditMessage a = (AuditMessage) u.unmarshal(doc);
            AtnaMessage jm = null;
            AtnaException ae = null;
            try {
                jm = createMessage(a);
            } catch (AtnaException e) {
                ae = e;
            }
            try {
                if (log.isInfoEnabled()) {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    Marshaller marshaller = jc.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(a, bout);
                    log.info("\n" + new String(bout.toByteArray()));
                }

            } catch (JAXBException e) {

            }
            if (ae != null) {
                throw ae;
            }
            return jm;
        } catch (JAXBException e) {
            throw new AtnaException(e);
        }
    }

    private Document newDocument(InputStream stream) throws IOException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(stream);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private StreamResult transform(Document doc, OutputStream out) throws IOException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "no");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        } catch (TransformerConfigurationException tce) {
            assert (false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(out);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            throw new IOException(te.getMessage());
        }
        return sr;
    }

    private AtnaMessage createProv(Document doc) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        transform(doc, bout);
        byte[] bytes = bout.toByteArray();
        if (log.isInfoEnabled()) {
            log.info("\n" + new String(bytes));
        }
        return new ProvisionalMessage(bytes);
    }

    public void write(AtnaMessage message, OutputStream out) throws AtnaException, IOException {
        if (jc == null) {
            throw new AtnaException("Could not create Jaxb Context");
        }
        if (message.getEventDateTime() == null) {
            message.setEventDateTime(new Date());
        }
        try {
            AuditMessage jmessage = createMessage(message);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.marshal(jmessage, out);
            if (log.isDebugEnabled()) {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                marshaller.marshal(jmessage, bout);
                log.debug("Written Audit Message:\n" + new String(bout.toByteArray()));
            }

        } catch (JAXBException e) {
            throw new AtnaException(e);
        }
    }

    private AtnaMessage createMessage(AuditMessage msg) throws AtnaException {
        EventIdentificationType evt = msg.getEventIdentification();
        if (evt == null) {
            throw new AtnaException("Message has no event");
        }
        if (evt.getEventOutcomeIndicator() == null) {
            throw new AtnaException("Message has no event outcome");
        }
        if (evt.getEventID() == null) {
            throw new AtnaException("Message has no event id");
        }
        AtnaMessage ret = new AtnaMessage(createCode(AtnaCode.EVENT_ID, evt.getEventID()), EventOutcome.getOutcome(evt.getEventOutcome()));
        ret.setEventActionCode(EventAction.getAction(evt.getEventActionCode()));
        ret.setEventDateTime(evt.getEventTime());
        List<CodedValueType> eventTypes = msg.getEventIdentification().getEventTypeCode();
        for (CodedValueType type : eventTypes) {
            ret.addEventTypeCode(createCode(AtnaCode.EVENT_TYPE, type));
        }
        List<ActiveParticipantType> ps = msg.getActiveParticipant();
        for (ActiveParticipantType p : ps) {
            ret.addParticipant(createParticipant(p));
        }
        List<AuditSourceIdentificationType> ss = msg.getAuditSourceIdentification();
        for (AuditSourceIdentificationType s : ss) {
            ret.addSource(createSource(s));
        }
        List<ParticipantObjectIdentificationType> as = msg.getParticipantObjectIdentification();
        for (ParticipantObjectIdentificationType a : as) {
            ret.addObject(createObject(a));
        }
        return ret;
    }

    private AuditMessage createMessage(AtnaMessage msg) throws AtnaException {
        if (msg.getEventOutcome() == null) {
            throw new AtnaException("message has no event outcome");
        }
        if (msg.getEventCode() == null) {
            throw new AtnaException("message has no event code");
        }
        AuditMessage ret = new AuditMessage();
        EventIdentificationType evt = new EventIdentificationType();
        if (msg.getEventActionCode() != null) {
            evt.setEventActionCode(msg.getEventActionCode().value());
        }
        if (msg.getEventDateTime() != null) {
            evt.setEventTime(msg.getEventDateTime());
        }
        evt.setEventID(createCode(msg.getEventCode()));
        evt.setEventOutcome(msg.getEventOutcome().value());
        List<AtnaCode> eventTypes = msg.getEventTypeCodes();

        for (AtnaCode eventType : eventTypes) {
            evt.getEventTypeCode().add(createCode(eventType));
        }
        ret.setEventIdentification(evt);

        List<AtnaMessageObject> objs = msg.getObjects();
        for (AtnaMessageObject obj : objs) {
            ret.getParticipantObjectIdentification().add(createObject(obj));
        }
        List<AtnaSource> sources = msg.getSources();
        for (AtnaSource source : sources) {
            ret.getAuditSourceIdentification().add(createSource(source));
        }
        List<AtnaMessageParticipant> parts = msg.getParticipants();
        for (AtnaMessageParticipant part : parts) {
            ret.getActiveParticipant().add(createParticipant(part));
        }
        return ret;
    }

    private AtnaMessageObject createObject(ParticipantObjectIdentificationType obj) throws AtnaException {
        if (obj.getParticipantObjectID() == null) {
            throw new AtnaException("object has no Id");
        }
        if (obj.getParticipantObjectIDTypeCode() == null) {
            throw new AtnaException("object has no Id type code");
        }
        AtnaObject ao = new AtnaObject(obj.getParticipantObjectID(), createCode(AtnaCode.OBJECT_ID_TYPE, obj.getParticipantObjectIDTypeCode()));
        ao.setObjectName(obj.getParticipantObjectName());
        ao.setObjectSensitivity(obj.getParticipantObjectSensitivity());
        if (obj.getParticipantObjectTypeCode() != null) {
            ao.setObjectTypeCode(ObjectType.getType(obj.getParticipantObjectTypeCode()));
        }
        if (obj.getParticipantObjectTypeCodeRole() != null) {
            ao.setObjectTypeCodeRole(ObjectTypeCodeRole.getRole(obj.getParticipantObjectTypeCodeRole()));
        }
        AtnaMessageObject ret = new AtnaMessageObject(ao);
        List<TypeValuePairType> pairs = obj.getParticipantObjectDetail();
        for (TypeValuePairType pair : pairs) {
            AtnaObjectDetail detail = new AtnaObjectDetail();
            detail.setType(pair.getType());
            detail.setValue(pair.getValue());
            ret.addObjectDetail(detail);
        }
        ret.setObjectQuery(obj.getParticipantObjectQuery());
        if (obj.getParticipantObjectDataLifeCycle() != null) {
            ret.setObjectDataLifeCycle(ObjectDataLifecycle.getLifecycle(obj.getParticipantObjectDataLifeCycle()));
        }
        return ret;
    }

    private ParticipantObjectIdentificationType createObject(AtnaMessageObject obj) throws AtnaException {
        if (obj.getObject() == null) {
            throw new AtnaException("object has no object");
        }
        if (obj.getObject().getObjectId() == null) {
            throw new AtnaException("object has no Id");
        }
        if (obj.getObject().getObjectIdTypeCode() == null) {
            throw new AtnaException("object has no Id type code");
        }
        ParticipantObjectIdentificationType ret = new ParticipantObjectIdentificationType();
        ret.setParticipantObjectID(obj.getObject().getObjectId());
        ret.setParticipantObjectIDTypeCode(createCode(obj.getObject().getObjectIdTypeCode()));
        ret.setParticipantObjectName(obj.getObject().getObjectName());
        ret.setParticipantObjectSensitivity(obj.getObject().getObjectSensitivity());
        if (obj.getObject().getObjectTypeCode() != null) {
            ret.setParticipantObjectTypeCode((short) obj.getObject().getObjectTypeCode().value());
        }
        if (obj.getObject().getObjectTypeCodeRole() != null) {
            ret.setParticipantObjectTypeCodeRole((short) obj.getObject().getObjectTypeCodeRole().value());
        }
        if (obj.getObjectDataLifeCycle() != null) {
            ret.setParticipantObjectDataLifeCycle((short) obj.getObjectDataLifeCycle().value());
        }
        ret.setParticipantObjectQuery(obj.getObjectQuery());
        List<AtnaObjectDetail> details = obj.getObjectDetails();
        for (AtnaObjectDetail detail : details) {
            TypeValuePairType pair = new TypeValuePairType();
            pair.setType(detail.getType());
            pair.setValue(detail.getValue());
            ret.getParticipantObjectDetail().add(pair);
        }
        return ret;
    }

    private AuditSourceIdentificationType createSource(AtnaSource source) throws AtnaException {
        if (source.getSourceId() == null) {
            throw new AtnaException("source has no Id");
        }
        AuditSourceIdentificationType ret = new AuditSourceIdentificationType();
        ret.setAuditSourceID(source.getSourceId());
        ret.setAuditEnterpriseSiteID(source.getEnterpriseSiteId());
        List<AtnaCode> codes = source.getSourceTypeCodes();
        for (AtnaCode code : codes) {
            ret.getAuditSourceTypeCode().add(createCode(code));
        }
        return ret;
    }

    private AtnaSource createSource(AuditSourceIdentificationType source) throws AtnaException {
        if (source.getAuditSourceID() == null) {
            throw new AtnaException("source has no Id");
        }
        AtnaSource ret = new AtnaSource(source.getAuditSourceID());
        ret.setEnterpriseSiteId(source.getAuditEnterpriseSiteID());
        List<CodedValueType> code = source.getAuditSourceTypeCode();
        for (CodedValueType type : code) {
            ret.addSourceTypeCode(createCode(AtnaCode.SOURCE_TYPE, type));
        }
        return ret;
    }

    private ActiveParticipantType createParticipant(AtnaMessageParticipant participant) throws AtnaException {
        if (participant.getParticipant() == null) {
            throw new AtnaException("participant has no participant");
        }
        if (participant.getParticipant().getUserId() == null) {
            throw new AtnaException("participant has no Id");
        }
        ActiveParticipantType ret = new ActiveParticipantType();
        ret.setUserID(participant.getParticipant().getUserId());
        ret.setUserName(participant.getParticipant().getUserName());
        ret.setAlternativeUserID(participant.getParticipant().getAlternativeUserId());
        List<AtnaCode> codes = participant.getParticipant().getRoleIDCodes();
        for (AtnaCode code : codes) {
            ret.getRoleIDCode().add(createCode(code));
        }
        ret.setNetworkAccessPointID(participant.getNetworkAccessPointId());
        if (participant.getNetworkAccessPointType() != null) {
            ret.setNetworkAccessPointTypeCode((short) participant.getNetworkAccessPointType().value());
        }
        return ret;

    }

    private AtnaMessageParticipant createParticipant(ActiveParticipantType participant) throws AtnaException {
        if (participant.getUserID() == null) {
            throw new AtnaException("participant has no Id");
        }
        AtnaParticipant ap = new AtnaParticipant(participant.getUserID());
        ap.setUserName(participant.getUserName());
        ap.setAlternativeUserId(participant.getAlternativeUserID());
        List<CodedValueType> codes = participant.getRoleIDCode();
        for (CodedValueType code : codes) {
            ap.addRoleIDCode(createCode(AtnaCode.PARTICIPANT_ROLE_TYPE, code));
        }
        AtnaMessageParticipant ret = new AtnaMessageParticipant(ap);
        ret.setNetworkAccessPointId(participant.getNetworkAccessPointID());
        if (participant.getNetworkAccessPointTypeCode() != null) {
            ret.setNetworkAccessPointType(NetworkAccessPoint.getAccessPoint(participant.getNetworkAccessPointTypeCode()));
        }
        ret.setUserIsRequestor(participant.isUserIsRequestor());
        return ret;
    }

    private AtnaCode createCode(String type, CodedValueType code) throws AtnaException {
        if (code.getCode() == null) {
            throw new AtnaException("Code has no code");
        }
        AtnaCode ac = new AtnaCode(type, code.getCode(),
                code.getCodeSystem(),
                code.getCodeSystemName(),
                code.getDisplayName(),
                code.getOriginalText());
        return ac;
    }

    private CodedValueType createCode(AtnaCode code) throws AtnaException {
        if (code.getCode() == null) {
            throw new AtnaException("Code has no code");
        }
        CodedValueType type = new CodedValueType();
        type.setCode(code.getCode());
        type.setCodeSystem(code.getCodeSystem());
        type.setCodeSystemName(code.getCodeSystemName());
        type.setDisplayName(code.getDisplayName());
        type.setOriginalText(code.getOriginalText());
        return type;
    }

}
