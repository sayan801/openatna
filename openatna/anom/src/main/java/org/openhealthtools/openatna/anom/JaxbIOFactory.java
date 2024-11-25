package org.openhealthtools.openatna.anom;

import org.apache.commons.lang3.StringUtils;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.openhealthtools.openatna.jaxb21.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class JaxbIOFactory implements AtnaIOFactory, Serializable {

    private static final long serialVersionUID = 6923830059780468692L;
    private static final DatatypeFactory DATATYPE_FACTORY;
    private static final JAXBContext jaxbContext;

    static {
        try {
            DATATYPE_FACTORY = DatatypeFactory.newInstance();
            org.apache.xml.security.Init.init();
            jaxbContext = JAXBContext.newInstance("org.openhealthtools.openatna.jaxb21");

        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException();
        } catch (JAXBException e) {
            // Fatal error if the JAXBContext cannot be instantiated.
            throw new RuntimeException("Error creating JAXB context:", e);
        }
    }

    private final Logger logger = LoggerFactory.getLogger(JaxbIOFactory.class);

    public Document canonicalize(Document document) throws AtnaException {

        try {
            Canonicalizer canonicalizer = Canonicalizer.getInstance(CanonicalizationMethod.INCLUSIVE);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            canonicalizer.canonicalizeSubtree(document, outputStream);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            return documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(outputStream.toByteArray()));

        } catch (InvalidCanonicalizerException | CanonicalizationException | ParserConfigurationException | SAXException | IOException e) {
            throw new AtnaException(e.getMessage(), e);
        }
    }

    /**
     * Returns XML Document as Byte Array.
     *
     * @param document Original Audit Message as XML Document
     * @return byte[] - Original Audit Message as byte[] UTF-8 encoded or empty byte[].
     */
    private byte[] getDocumentAsByteArray(Document document) {

        String originalMessage = getDocumentAsString(document);
        return StringUtils.isNotBlank(originalMessage) ? originalMessage.getBytes(StandardCharsets.UTF_8) : new byte[0];
    }

    /**
     * Returns XML Document as String
     *
     * @param document Original Audit Message as XML Document
     * @return
     */
    private String getDocumentAsString(Document document) {
        try {
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();

        } catch (TransformerException ex) {
            logger.error("TransformerException: '{}'", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * @param inputStream
     * @return
     * @throws AtnaException
     */
    public AtnaMessage read(InputStream inputStream) throws AtnaException {

        if (jaxbContext == null) {
            throw new AtnaException("Could not create JAXB Context");
        }

        try {
            Document doc = newDocument(inputStream);
            if (StringUtils.equalsIgnoreCase(doc.getDocumentElement().getTagName(), "IHEYr4")) {
                return createProv(doc);
            }
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            AuditMessage auditMessage = (AuditMessage) unmarshaller.unmarshal(doc);
            AtnaMessage atnaMessage = createMessage(auditMessage);
            atnaMessage.setMessageContent(getDocumentAsByteArray(doc));
            return atnaMessage;

        } catch (AtnaException | JAXBException | IOException e) {
            throw new AtnaException(e, AtnaException.AtnaError.INVALID_MESSAGE);
        }
    }

    private Document newDocument(InputStream stream) throws IOException {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(stream);

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    private StreamResult transform(Document document, OutputStream out) throws IOException {

        StreamResult streamResult = new StreamResult(out);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, streamResult);

        } catch (TransformerException e) {
            throw new IOException(e.getMessage(), e);
        }
        return streamResult;
    }

    private AtnaMessage createProv(Document doc) throws IOException {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        transform(doc, bout);

        return new ProvisionalMessage(bout.toByteArray());
    }

    /**
     * Writes ATNA Message  into the OutputStream before processing the persistence.
     *
     * @param message
     * @param out
     * @throws AtnaException
     */
    public void write(AtnaMessage message, OutputStream out) throws AtnaException {

        try {
            if (message.getEventDateTime() == null) {
                message.setEventDateTime(new Date());
            }
            out.write(message.getMessageContent());
        } catch (IOException e) {
            throw new AtnaException(e.getMessage(), e);
        }
    }

    /**
     * Writes ATNA Message as an Audit Message into the OutputStream before processing the persistence.
     *
     * @param message
     * @param out
     * @param includeDeclaration
     * @throws AtnaException
     */
    public void write(AtnaMessage message, OutputStream out, boolean includeDeclaration) throws AtnaException {

        if (jaxbContext == null) {
            throw new AtnaException("Could not create JAXB Context");
        }
        if (message.getEventDateTime() == null) {
            message.setEventDateTime(new Date());
        }
        try {
            AuditMessage auditMessage = createMessage(message);
            Marshaller marshaller = jaxbContext.createMarshaller();
            if (!includeDeclaration) {
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            }
            marshaller.marshal(auditMessage, out);

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

        AtnaMessage message = new AtnaMessage(createCode(AtnaCode.EVENT_ID, evt.getEventID()),
                EventOutcome.getOutcome(evt.getEventOutcomeIndicator().intValue()));
        message.setEventActionCode(EventAction.getAction(evt.getEventActionCode()));
        message.setEventDateTime(evt.getEventDateTime().toGregorianCalendar().getTime());
        List<CodedValueType> eventTypes = msg.getEventIdentification().getEventTypeCode();
        for (CodedValueType type : eventTypes) {
            message.addEventTypeCode(createCode(AtnaCode.EVENT_TYPE, type));
        }
        List<AuditMessage.ActiveParticipant> ps = msg.getActiveParticipant();
        for (ActiveParticipantType p : ps) {
            message.addParticipant(createParticipant(p));
        }
        List<AuditSourceIdentificationType> ss = msg.getAuditSourceIdentification();
        for (AuditSourceIdentificationType s : ss) {
            message.addSource(createSource(s));
        }
        List<ParticipantObjectIdentificationType> as = msg.getParticipantObjectIdentification();
        for (ParticipantObjectIdentificationType a : as) {
            message.addObject(createObject(a));
        }

        return message;
    }

    private AuditMessage createMessage(AtnaMessage msg) throws AtnaException {

        if (msg.getEventOutcome() == null) {
            throw new AtnaException("Message has no event outcome");
        }
        if (msg.getEventCode() == null) {
            throw new AtnaException("Message has no event code");
        }
        AuditMessage auditMessage = new AuditMessage();
        EventIdentificationType evt = new EventIdentificationType();
        if (msg.getEventActionCode() != null) {
            evt.setEventActionCode(msg.getEventActionCode().value());
        }
        if (msg.getEventDateTime() != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(msg.getEventDateTime());
            evt.setEventDateTime(DATATYPE_FACTORY.newXMLGregorianCalendar(gregorianCalendar));
        }
        evt.setEventID(createCode(msg.getEventCode()));
        evt.setEventOutcomeIndicator(BigInteger.valueOf(msg.getEventOutcome().value()));
        List<AtnaCode> eventTypes = msg.getEventTypeCodes();

        for (AtnaCode eventType : eventTypes) {
            evt.getEventTypeCode().add(createCode(eventType));
        }
        auditMessage.setEventIdentification(evt);

        List<AtnaMessageObject> objs = msg.getObjects();
        for (AtnaMessageObject obj : objs) {
            auditMessage.getParticipantObjectIdentification().add(createObject(obj));
        }
        List<AtnaSource> sources = msg.getSources();
        for (AtnaSource source : sources) {
            auditMessage.getAuditSourceIdentification().add(createSource(source));
        }
        List<AtnaMessageParticipant> parts = msg.getParticipants();
        for (AtnaMessageParticipant part : parts) {
            auditMessage.getActiveParticipant().add(createParticipant(part));
        }
        return auditMessage;
    }

    private AtnaMessageObject createObject(ParticipantObjectIdentificationType obj) throws AtnaException {

        if (obj.getParticipantObjectIDTypeCode() == null) {
            throw new AtnaException("Object has no Id type code");
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
        // ATNA Description are specific to DICOM and not relevant for RFC 3881
        //List<ParticipantObjectDescriptionType> descs = obj.getParticipantObjectDescription();
//        for (ParticipantObjectDescriptionType desc : descs) {
//            ObjectDescription od = new ObjectDescription();
//            List<ParticipantObjectDescriptionType.MPPS> mpps = desc.getMPPS();
//            for (ParticipantObjectDescriptionType.MPPS mpp : mpps) {
//                od.addMppsUid(mpp.getUID());
//            }
//            List<ParticipantObjectDescriptionType.Accession> accs = desc.getAccession();
//            for (ParticipantObjectDescriptionType.Accession acc : accs) {
//                od.addAccessionNumber(acc.getNumber());
//            }
//            List<ParticipantObjectDescriptionType.SOPClass> sops = desc.getSOPClass();
//            for (ParticipantObjectDescriptionType.SOPClass sop : sops) {
//                SopClass sc = new SopClass();
//                sc.setUid(sop.getUID());
//                sc.setNumberOfInstances(sop.getNumberOfInstances().intValue());
//                List<ParticipantObjectDescriptionType.SOPClass.Instance> insts = sop.getInstance();
//                for (ParticipantObjectDescriptionType.SOPClass.Instance inst : insts) {
//                    sc.addInstanceUid(inst.getUID());
//                }
//                od.addSopClass(sc);
//            }
//            ao.addObjectDescription(od);
//        }
        AtnaMessageObject atnaMessageObject = new AtnaMessageObject(ao);
        List<TypeValuePairType> pairs = obj.getParticipantObjectDetail();
        for (TypeValuePairType pair : pairs) {
            AtnaObjectDetail detail = new AtnaObjectDetail();
            detail.setType(pair.getType());
            detail.setValue(pair.getValue());
            atnaMessageObject.addObjectDetail(detail);
        }
        atnaMessageObject.setObjectQuery(obj.getParticipantObjectQuery());
        if (obj.getParticipantObjectDataLifeCycle() != null) {
            atnaMessageObject.setObjectDataLifeCycle(ObjectDataLifecycle.getLifecycle(obj.getParticipantObjectDataLifeCycle()));
        }
        return atnaMessageObject;
    }

    private ParticipantObjectIdentificationType createObject(AtnaMessageObject obj) throws AtnaException {

        if (obj.getObject() == null) {
            throw new AtnaException("Object has no object");
        }
        if (obj.getObject().getObjectId() == null) {
            throw new AtnaException("Object has no Id");
        }
        if (obj.getObject().getObjectIdTypeCode() == null) {
            throw new AtnaException("Object has no Id type code");
        }
        ParticipantObjectIdentificationType participantObjectId = new ParticipantObjectIdentificationType();
        participantObjectId.setParticipantObjectID(obj.getObject().getObjectId());
        participantObjectId.setParticipantObjectIDTypeCode(createCode(obj.getObject().getObjectIdTypeCode()));
        participantObjectId.setParticipantObjectName(obj.getObject().getObjectName());
        participantObjectId.setParticipantObjectSensitivity(obj.getObject().getObjectSensitivity());
        if (obj.getObject().getObjectTypeCode() != null) {
            participantObjectId.setParticipantObjectTypeCode((short) obj.getObject().getObjectTypeCode().value());
        }
        if (obj.getObject().getObjectTypeCodeRole() != null) {
            participantObjectId.setParticipantObjectTypeCodeRole((short) obj.getObject().getObjectTypeCodeRole().value());
        }
        if (obj.getObjectDataLifeCycle() != null) {
            participantObjectId.setParticipantObjectDataLifeCycle((short) obj.getObjectDataLifeCycle().value());
        }
        participantObjectId.setParticipantObjectQuery(obj.getObjectQuery());
        List<AtnaObjectDetail> details = obj.getObjectDetails();
        for (AtnaObjectDetail detail : details) {
            TypeValuePairType pair = new TypeValuePairType();
            pair.setType(detail.getType());
            pair.setValue(detail.getValue());
            participantObjectId.getParticipantObjectDetail().add(pair);
        }
        // ATNA Description are specific to DICOM and not relevant for RFC 3881
//        List<ObjectDescription> descs = obj.getObject().getDescriptions();
//        for (ObjectDescription desc : descs) {
//            ParticipantObjectDescriptionType dt = new ParticipantObjectDescriptionType();
//            List<String> uids = desc.getMppsUids();
//            for (String uid : uids) {
//                ParticipantObjectDescriptionType.MPPS mpps = new ParticipantObjectDescriptionType.MPPS();
//                mpps.setUID(uid);
//                dt.getMPPS().add(mpps);
//            }
//            List<String> nums = desc.getAccessionNumbers();
//            for (String num : nums) {
//                ParticipantObjectDescriptionType.Accession acc = new ParticipantObjectDescriptionType.Accession();
//                acc.setNumber(num);
//                dt.getAccession().add(acc);
//            }
//            List<SopClass> sops = desc.getSopClasses();
//            for (SopClass sop : sops) {
//                ParticipantObjectDescriptionType.SOPClass sc = new ParticipantObjectDescriptionType.SOPClass();
//                sc.setNumberOfInstances(BigInteger.valueOf(sop.getNumberOfInstances()));
//                sc.setUID(sop.getUid());
//                List<String> insts = sop.getInstanceUids();
//                for (String inst : insts) {
//                    ParticipantObjectDescriptionType.SOPClass.Instance i =
//                            new ParticipantObjectDescriptionType.SOPClass.Instance();
//                    i.setUID(inst);
//                    sc.getInstance().add(i);
//                }
//                dt.getSOPClass().add(sc);
//            }
//         participantObjectId.getParticipantObjectDescription().add(dt);
//        }
        return participantObjectId;
    }

    private AuditSourceIdentificationType createSource(AtnaSource source) throws AtnaException {

        if (source.getSourceId() == null) {
            throw new AtnaException("Source has no ID");
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
            throw new AtnaException("Source has no Id");
        }
        AtnaSource ret = new AtnaSource(source.getAuditSourceID());
        ret.setEnterpriseSiteId(source.getAuditEnterpriseSiteID());
        List<CodedValueType> code = source.getAuditSourceTypeCode();
        for (CodedValueType type : code) {
            ret.addSourceTypeCode(createCode(AtnaCode.SOURCE_TYPE, type));
        }
        return ret;
    }

    private AuditMessage.ActiveParticipant createParticipant(AtnaMessageParticipant participant) throws AtnaException {

        if (participant.getParticipant() == null) {
            throw new AtnaException("Participant has no participant");
        }
        if (participant.getParticipant().getUserId() == null) {
            throw new AtnaException("Participant has no Id");
        }
        AuditMessage.ActiveParticipant ret = new ObjectFactory().createAuditMessageActiveParticipant();
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
        ret.setUserIsRequestor(participant.isUserIsRequestor());

        return ret;
    }

    private AtnaMessageParticipant createParticipant(ActiveParticipantType participant) throws AtnaException {

        if (participant.getUserID() == null) {
            throw new AtnaException("Participant has no ID");
        }
        AtnaParticipant ap = new AtnaParticipant(participant.getUserID());
        ap.setUserName(participant.getUserName());
        ap.setAlternativeUserId(participant.getAlternativeUserID());
        List<CodedValueType> codes = participant.getRoleIDCode();
        for (CodedValueType code : codes) {
            ap.addRoleIDCode(createCode(AtnaCode.PARTICIPANT_ROLE_TYPE, code));
        }
        AtnaMessageParticipant messageParticipant = new AtnaMessageParticipant(ap);
        messageParticipant.setNetworkAccessPointId(participant.getNetworkAccessPointID());
        if (participant.getNetworkAccessPointTypeCode() != null) {
            messageParticipant.setNetworkAccessPointType(NetworkAccessPoint.getAccessPoint(participant.getNetworkAccessPointTypeCode()));
        }
        messageParticipant.setUserIsRequestor(participant.isUserIsRequestor());
        return messageParticipant;
    }

    private AtnaCode createCode(String type, CodedValueType code) throws AtnaException {

        if (code.getCode() == null) {
            throw new AtnaException("Code has no code");
        }
        return new AtnaCode(type, code.getCode(), code.getCodeSystem(), code.getCodeSystemName(),
                code.getDisplayName(), code.getOriginalText());
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
