package org.openhealthtools.openatna.audit.persistence.util;

import org.apache.commons.lang.StringUtils;
import org.openhealthtools.openatna.anom.Timestamp;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Reads an XML file and loads entities into the DB.
 *
 * @author Andrew Harrison
 */
public class DataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataReader.class);

    private final Document document;

    private final Map<String, CodeEntity> evtIds = new HashMap<>();
    private final Map<String, CodeEntity> evtTypes = new HashMap<>();
    private final Map<String, CodeEntity> sourceTypes = new HashMap<>();
    private final Map<String, CodeEntity> objTypes = new HashMap<>();
    private final Map<String, CodeEntity> partTypes = new HashMap<>();


    private final Map<String, NetworkAccessPointEntity> naps = new HashMap<>();
    private final Map<String, SourceEntity> sources = new HashMap<>();
    private final Map<String, ParticipantEntity> parts = new HashMap<>();
    private final Map<String, ObjectEntity> objects = new HashMap<>();
    private final Set<MessageEntity> messages = new HashSet<>();

    public DataReader(InputStream in) {

        try {
            document = newDocument(in);
            in.close();
        } catch (IOException e) {
            LOGGER.error("IOException: '{}'", e.getMessage(), e);
            throw new RuntimeException("Could not load data file");
        }
    }

    private static Document newDocument(InputStream stream) throws IOException {

        Document document = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(stream);
        } catch (ParserConfigurationException e) {
            LOGGER.error("ParserConfigurationException: '{}'", e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.error("SAXException: '{}'", e.getMessage(), e);
        }
        return document;
    }

    public void parse() throws AtnaPersistenceException {

        readDoc();
        load();
    }

    private void load() throws AtnaPersistenceException {

        var persistencePolicies = new PersistencePolicies();
        persistencePolicies.setErrorOnDuplicateInsert(false);
        persistencePolicies.setAllowNewCodes(true);
        persistencePolicies.setAllowNewNetworkAccessPoints(true);
        persistencePolicies.setAllowNewObjects(true);
        persistencePolicies.setAllowNewParticipants(true);
        persistencePolicies.setAllowNewSources(true);
        if (evtTypes.size() > 0) {
            var codeDao = AtnaFactory.codeDao();
            for (CodeEntity code : evtTypes.values()) {
                codeDao.save(code, persistencePolicies);
            }
        }
        if (evtIds.size() > 0) {
            var codeDao = AtnaFactory.codeDao();
            for (CodeEntity code : evtIds.values()) {
                codeDao.save(code, persistencePolicies);
            }
        }
        if (sourceTypes.size() > 0) {
            var codeDao = AtnaFactory.codeDao();
            for (CodeEntity code : sourceTypes.values()) {
                codeDao.save(code, persistencePolicies);
            }
        }
        if (objTypes.size() > 0) {
            var codeDao = AtnaFactory.codeDao();
            for (CodeEntity code : objTypes.values()) {
                codeDao.save(code, persistencePolicies);
            }
        }
        if (partTypes.size() > 0) {
            var codeDao = AtnaFactory.codeDao();
            for (CodeEntity code : partTypes.values()) {
                codeDao.save(code, persistencePolicies);
            }
        }
        if (naps.size() > 0) {
            var networkAccessPointDao = AtnaFactory.networkAccessPointDao();
            for (NetworkAccessPointEntity nap : naps.values()) {
                networkAccessPointDao.save(nap, persistencePolicies);
            }
        }
        if (sources.size() > 0) {
            var sourceDao = AtnaFactory.sourceDao();
            for (SourceEntity source : sources.values()) {
                sourceDao.save(source, persistencePolicies);
            }
        }
        if (parts.size() > 0) {
            var participantDao = AtnaFactory.participantDao();
            for (ParticipantEntity pe : parts.values()) {
                participantDao.save(pe, persistencePolicies);
            }
        }
        if (objects.size() > 0) {
            var objectDao = AtnaFactory.objectDao();
            for (ObjectEntity e : objects.values()) {
                objectDao.save(e, persistencePolicies);
            }
        }
        if (!messages.isEmpty()) {
            var messageDao = AtnaFactory.messageDao();
            for (MessageEntity e : messages) {
                messageDao.save(e, persistencePolicies);
            }
        }
    }

    private void readDoc() {

        Element el = document.getDocumentElement();
        if (el.getLocalName().equals(DataConstants.ENTITIES)) {
            NodeList children = el.getChildNodes();
            for (var i = 0; i < children.getLength(); i++) {
                Node n = children.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    String name = (e).getLocalName();
                    if (name.equalsIgnoreCase(DataConstants.CODES)) {
                        readCodes(e);
                    } else if (name.equalsIgnoreCase(DataConstants.NETWORK_ACCESS_POINTS)) {
                        readNaps(e);
                    }
                }
            }
            for (var i = 0; i < children.getLength(); i++) {
                Node n = children.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    String name = (e).getLocalName();
                    if (name.equalsIgnoreCase(DataConstants.SOURCES)) {
                        readSources(e);
                    } else if (name.equalsIgnoreCase(DataConstants.PARTICIPANTS)) {
                        readParts(e);
                    } else if (name.equalsIgnoreCase(DataConstants.OBJECTS)) {
                        readObjects(e);
                    }
                }
            }
            for (var i = 0; i < children.getLength(); i++) {
                Node n = children.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    String name = (e).getLocalName();
                    if (name.equalsIgnoreCase(DataConstants.MESSAGE)) {
                        readMessage(e);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown XML format");
        }
    }

    private void readCodes(Element codes) {

        NodeList children = codes.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                Element e = (Element) n;
                if (e.getTagName().equalsIgnoreCase(DataConstants.CODE_TYPE)) {
                    String type = e.getAttribute("name");
                    if (type != null) {
                        readCodeTypes(e, type);
                    }
                }
            }
        }
    }

    private void readCodeTypes(Element code, String type) {

        NodeList children = code.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && ((Element) n).getTagName().equalsIgnoreCase(DataConstants.CODE)) {
                readCode((Element) n, type);
            }
        }
    }

    private void readCode(Element el, String type) {

        CodeEntity entity = null;
        if (type.equalsIgnoreCase(DataConstants.CODE_EVENT_ID)) {
            entity = new EventIdCodeEntity();
        } else if (type.equalsIgnoreCase(DataConstants.CODE_EVENT_TYPE)) {
            entity = new EventTypeCodeEntity();
        } else if (type.equalsIgnoreCase(DataConstants.CODE_OBJ_ID_TYPE)) {
            entity = new ObjectIdTypeCodeEntity();
        } else if (type.equalsIgnoreCase(DataConstants.CODE_PARTICIPANT_TYPE)) {
            entity = new ParticipantCodeEntity();
        } else if (type.equalsIgnoreCase(DataConstants.CODE_SOURCE)) {
            entity = new SourceCodeEntity();
        }
        if (entity == null) {
            return;
        }
        String code = el.getAttribute(DataConstants.CODE);
        if (nill(code)) {
            LOGGER.info("no code defined in coded value. Not loading...");
            return;
        }
        entity.setCode(code);
        String sys = el.getAttribute(DataConstants.CODE_SYSTEM);
        String name = el.getAttribute(DataConstants.CODE_SYSTEM_NAME);
        String dis = el.getAttribute(DataConstants.DISPLAY_NAME);
        String orig = el.getAttribute(DataConstants.ORIGINAL_TEXT);
        entity.setCodeSystem(nill(sys) ? null : sys);
        entity.setCodeSystemName(nill(name) ? null : name);
        entity.setDisplayName(nill(dis) ? null : dis);
        entity.setOriginalText(nill(orig) ? null : orig);

        switch (type) {
            case DataConstants.CODE_EVENT_ID:
                evtIds.put(code, entity);
                break;
            case DataConstants.CODE_EVENT_TYPE:
                evtTypes.put(code, entity);
                break;
            case DataConstants.CODE_OBJ_ID_TYPE:
                objTypes.put(code, entity);
                break;
            case DataConstants.CODE_PARTICIPANT_TYPE:
                partTypes.put(code, entity);
                break;
            case DataConstants.CODE_SOURCE:
                sourceTypes.put(code, entity);
                break;
            default:
                LOGGER.warn("Code not supported!");
        }
    }

    private void readNaps(Element codes) {

        NodeList children = codes.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(DataConstants.NETWORK_ACCESS_POINT)) {
                readNap((Element) n);
            }
        }
    }

    private void readNap(Element el) {

        String netId = el.getAttribute(DataConstants.NETWORK_ACCESS_POINT_ID);
        String type = el.getAttribute(DataConstants.TYPE);
        if (nill(netId) || nill(type)) {
            LOGGER.info("no identifier or type defined in network access point. Not loading...");
            return;
        }
        var networkAccessPointEntity = new NetworkAccessPointEntity();
        networkAccessPointEntity.setIdentifier(netId);
        networkAccessPointEntity.setType(Short.valueOf(type));
        naps.put(netId, networkAccessPointEntity);
    }

    private void readSources(Element codes) {

        NodeList children = codes.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(DataConstants.SOURCE)) {
                readSource((Element) n);
            }
        }
    }

    private void readSource(Element el) {

        String sourceId = el.getAttribute(DataConstants.SOURCE_ID);
        if (nill(sourceId)) {
            LOGGER.info("No Source id set. Not loading...");
            return;
        }
        String ent = el.getAttribute(DataConstants.ENT_SITE_ID);
        var sourceEntity = new SourceEntity();
        sourceEntity.setSourceId(sourceId);
        sourceEntity.setEnterpriseSiteId(nill(ent) ? null : ent);
        NodeList children = el.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(DataConstants.SOURCE_TYPE)) {
                Element ch = (Element) n;
                String ref = ch.getAttribute(DataConstants.CODE);
                if (nill(ref)) {
                    continue;
                }
                CodeEntity code = sourceTypes.get(ref);
                if (code instanceof SourceCodeEntity) {
                    sourceEntity.getSourceTypeCodes().add((SourceCodeEntity) code);
                }
            }
        }
        sources.put(sourceId, sourceEntity);
    }

    private void readParts(Element codes) {

        NodeList children = codes.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(DataConstants.PARTICIPANT)) {
                readPart((Element) n);
            }
        }
    }

    private void readPart(Element el) {

        String partId = el.getAttribute(DataConstants.USER_ID);
        if (nill(partId)) {
            LOGGER.info("no active participant id defined. Not loading...");
        }
        String name = el.getAttribute(DataConstants.USER_NAME);
        String alt = el.getAttribute(DataConstants.ALT_USER_ID);
        var participantEntity = new ParticipantEntity();
        participantEntity.setUserId(partId);
        participantEntity.setUserName(nill(name) ? null : name);
        participantEntity.setAlternativeUserId(nill(alt) ? null : alt);
        NodeList children = el.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(DataConstants.PARTICIPANT_TYPE)) {
                Element ch = (Element) n;
                String ref = ch.getAttribute(DataConstants.CODE);
                if (nill(ref)) {
                    continue;
                }
                CodeEntity code = partTypes.get(ref);
                if (code instanceof ParticipantCodeEntity) {
                    participantEntity.getParticipantTypeCodes().add((ParticipantCodeEntity) code);
                }
            }
        }
        parts.put(partId, participantEntity);
    }

    private void readObjects(Element codes) {

        NodeList children = codes.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(DataConstants.OBJECT)) {
                readObject((Element) n);
            }
        }
    }

    private void readObject(Element el) {

        String obId = el.getAttribute(DataConstants.OBJECT_ID);
        if (nill(obId)) {
            LOGGER.info("no participating object id defined. Not loading...");
        }
        String name = el.getAttribute(DataConstants.OBJECT_NAME);
        String type = el.getAttribute(DataConstants.OBJECT_TYPE_CODE);
        String role = el.getAttribute(DataConstants.OBJECT_TYPE_CODE_ROLE);
        String sens = el.getAttribute(DataConstants.OBJECT_SENSITIVITY);
        var objectEntity = new ObjectEntity();
        objectEntity.setObjectId(obId);
        objectEntity.setObjectName(nill(name) ? null : name);
        objectEntity.setObjectSensitivity(nill(sens) ? null : sens);
        objectEntity.setObjectTypeCode(nill(type) ? null : Short.valueOf(type));
        objectEntity.setObjectTypeCodeRole(nill(role) ? null : Short.valueOf(role));
        NodeList children = el.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                Element ele = (Element) n;

                if (ele.getLocalName().equals(DataConstants.OBJECT_ID_TYPE)) {
                    String ref = ele.getAttribute(DataConstants.CODE);
                    if (nill(ref)) {
                        LOGGER.info("no object id type defined. Not loading...");
                        return;
                    }
                    CodeEntity code = objTypes.get(ref);
                    if (code instanceof ObjectIdTypeCodeEntity) {
                        objectEntity.setObjectIdTypeCode((ObjectIdTypeCodeEntity) code);
                    } else {
                        LOGGER.info("no object id type defined. Not loading...");
                        return;
                    }
                } else if (ele.getLocalName().equals(DataConstants.OBJECT_DETAIL_KEY)) {
                    String key = ele.getAttribute(DataConstants.KEY);
                    if (key != null) {
                        objectEntity.addObjectDetailType(key);
                    }
                }
            }
        }
        if (objectEntity.getObjectIdTypeCode() == null) {
            LOGGER.info("no object id type defined. Not loading...");
            return;
        }
        objects.put(obId, objectEntity);
    }

    public void readMessage(Element el) {

        String action = el.getAttribute(DataConstants.EVT_ACTION);
        String outcome = el.getAttribute(DataConstants.EVT_OUTCOME);
        String time = el.getAttribute(DataConstants.EVT_TIME);
        Date ts = null;
        if (time != null) {
            ts = Timestamp.parseToDate(time);
        }
        if (ts == null) {
            ts = new Date();
        }
        if (nill(action) || nill(outcome)) {
            LOGGER.info("action or outcome of message is null. Not loading...");
        }
        var messageEntity = new MessageEntity();
        messageEntity.setEventActionCode(action);
        messageEntity.setEventDateTime(ts);
        messageEntity.setEventOutcome(Integer.parseInt(outcome));
        NodeList children = el.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                Element ele = (Element) n;
                if (ele.getLocalName().equals(DataConstants.EVT_ID)) {
                    String ref = ele.getAttribute(DataConstants.CODE);
                    if (nill(ref)) {
                        LOGGER.info("no event id type defined. Not loading...");
                        return;
                    }
                    CodeEntity code = evtIds.get(ref);
                    if (code instanceof EventIdCodeEntity) {
                        messageEntity.setEventId((EventIdCodeEntity) code);
                    } else {
                        LOGGER.info("no event id type defined. Not loading...");
                        return;
                    }
                } else if (ele.getLocalName().equals(DataConstants.EVT_TYPE)) {
                    String ref = ele.getAttribute(DataConstants.CODE);
                    if (!nill(ref)) {
                        CodeEntity code = evtTypes.get(ref);
                        if (code instanceof EventTypeCodeEntity) {
                            messageEntity.addEventTypeCode((EventTypeCodeEntity) code);
                        }
                    }
                } else if (ele.getLocalName().equals(DataConstants.PARTICIPANT)) {
                    String ref = ele.getAttribute(DataConstants.REF);
                    if (!nill(ref)) {
                        ParticipantEntity pe = parts.get(ref);
                        if (pe != null) {
                            var messageParticipantEntity = new MessageParticipantEntity(pe);
                            String requestor = ele.getAttribute("requestor");
                            if (requestor != null) {
                                messageParticipantEntity.setUserIsRequestor(Boolean.valueOf(requestor));
                            }
                            String nap = ele.getAttribute("nap");
                            if (nap != null) {
                                NetworkAccessPointEntity net = naps.get(nap);
                                if (net != null) {
                                    messageParticipantEntity.setNetworkAccessPoint(net);
                                }
                            }
                            messageEntity.addMessageParticipant(messageParticipantEntity);
                        }
                    }
                } else if (ele.getLocalName().equals(DataConstants.SOURCE)) {
                    String ref = ele.getAttribute(DataConstants.REF);
                    if (!nill(ref)) {
                        SourceEntity se = sources.get(ref);
                        if (se != null) {
                            var messageSourceEntity = new MessageSourceEntity(se);
                            messageEntity.addMessageSource(messageSourceEntity);
                        }
                    }
                } else if (ele.getLocalName().equals(DataConstants.OBJECT)) {
                    String ref = ele.getAttribute(DataConstants.REF);
                    if (!nill(ref)) {
                        ObjectEntity oe = objects.get(ref);
                        if (oe != null) {
                            var messageObjectEntity = new MessageObjectEntity(oe);
                            NodeList ch = ele.getChildNodes();
                            for (var j = 0; j < ch.getLength(); j++) {
                                Node node = ch.item(j);
                                if (node instanceof Element) {
                                    Element child = (Element) node;
                                    boolean enc = child.getAttribute("encoded") != null
                                            && StringUtils.equalsIgnoreCase(child.getAttribute("encoded"), "true");
                                    if (child.getLocalName().equals(DataConstants.QUERY)) {
                                        String q = child.getTextContent();
                                        if (q != null) {
                                            q = q.trim();
                                            if (!enc) {
                                                q = Base64.encodeString(q);
                                            }
                                            messageObjectEntity.setObjectQuery(q.getBytes(StandardCharsets.UTF_8));
                                        }
                                    } else if (child.getLocalName().equals(DataConstants.DETAIL)) {
                                        String type = child.getAttribute(DataConstants.TYPE);
                                        if (type != null) {
                                            String val = child.getTextContent();
                                            if (val != null) {
                                                val = val.trim();
                                                if (!enc) {
                                                    val = Base64.encodeString(val);
                                                }
                                                var objectDetailEntity = new ObjectDetailEntity(type, val.getBytes(StandardCharsets.UTF_8));
                                                messageObjectEntity.addObjectDetail(objectDetailEntity);
                                            }
                                        }
                                    }
                                }
                            }
                            messageEntity.addMessageObject(messageObjectEntity);
                        }
                    }
                }
            }
        }
        if (messageEntity.getMessageParticipants().isEmpty()) {
            LOGGER.info("message has no participants. Not loading...");
            return;
        }
        if (messageEntity.getMessageSources().isEmpty()) {
            LOGGER.info("message has no sources. Not loading...");
            return;
        }
        messages.add(messageEntity);
    }

    private String id(Element el) {
        String id = el.getAttribute(DataConstants.ID);
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    private boolean nill(String val) {
        return val == null || val.trim().length() == 0;
    }
}
