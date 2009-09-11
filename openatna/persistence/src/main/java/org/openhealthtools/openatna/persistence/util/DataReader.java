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

package org.openhealthtools.openatna.persistence.util;

import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.*;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Reads an XML file and loads entities into the DB.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 10, 2009: 8:37:36 AM
 * @date $Date:$ modified by $Author:$
 */

public class DataReader {

    public static final String ENTITIES = "entities";

    public static final String CODES = "codes";
    public static final String SOURCES = "sources";
    public static final String PARTICIPANTS = "participants";
    public static final String OBJECTS = "objects";
    public static final String NETWORK_ACCESS_POINTS = "netPoints";

    public static final String ID = "id";
    public static final String REF = "ref";

    public static final String CODE_EVENT_ID = "eventId";
    public static final String CODE_EVENT_TYPE = "eventType";
    public static final String CODE_OBJ_ID_TYPE = "objectIdType";
    public static final String CODE_PARTICIPANT_TYPE = "participantType";
    public static final String CODE_SOURCE = "sourceType";

    public static final String CODE = "code";
    public static final String CODE_SYSTEM = "system";
    public static final String CODE_SYSTEM_NAME = "systemName";
    public static final String DISPLAY_NAME = "displayName";
    public static final String ORIGINAL_TEXT = "originalText";

    public static final String NETWORK_ACCESS_POINT = "netPoint";
    public static final String NETWORK_ACCESS_POINT_ID = "netId";


    public static final String SOURCE_ID = "sourceId";
    public static final String SOURCE_TYPE = "sourceType";
    public static final String SOURCE = "source";
    public static final String OBJECT = "object";
    public static final String PARTICIPANT = "participant";

    public static final String USER_ID = "userId";
    public static final String ALT_USER_ID = "altUserId";
    public static final String USER_NAME = "userName";
    public static final String PARTICIPANT_TYPE = "participantType";

    public static final String ENT_SITE_ID = "enterpriseSiteId";

    public static final String OBJECT_ID = "objectId";
    public static final String OBJECT_TYPE_CODE = "objectTypeCode";
    public static final String OBJECT_TYPE_CODE_ROLE = "objectTypeCodeRole";
    public static final String OBJECT_SENSITIVITY = "sensitivity";
    public static final String OBJECT_ID_TYPE = "objectIdType";
    public static final String OBJECT_NAME = "objectName";
    public static final String OBJECT_DETAIL_KEY = "objectDetailKey";
    public static final String KEY = "key";

    public static final String TYPE = "type";
    public static final String VALUE = "value";


    private Document doc;
    private DaoFactory factory;

    private Map<String, CodeEntity> codes = new HashMap<String, CodeEntity>();
    private Map<String, NetworkAccessPointEntity> naps = new HashMap<String, NetworkAccessPointEntity>();
    private Map<String, SourceEntity> sources = new HashMap<String, SourceEntity>();
    private Map<String, ParticipantEntity> parts = new HashMap<String, ParticipantEntity>();
    private Map<String, ObjectEntity> objects = new HashMap<String, ObjectEntity>();
    private Map<String, ObjectDetailEntity> details = new HashMap<String, ObjectDetailEntity>();

    public DataReader(InputStream in) {
        try {
            doc = newDocument(in);
            in.close();
            factory = SpringDaoFactory.getFactory();
        } catch (IOException e) {
            throw new RuntimeException("Could not load data file");
        }
    }

    public void parse() throws AtnaPersistenceException {
        readDoc();
        load();
    }

    private void load() throws AtnaPersistenceException {
        if (codes.size() > 0) {
            CodeDao dao = factory.codeDao();
            for (CodeEntity code : codes.values()) {
                dao.save(code);
            }
        }
        if (naps.size() > 0) {
            NetworkAccessPointDao dao = factory.networkAccessPointDao();
            for (NetworkAccessPointEntity nap : naps.values()) {
                dao.save(nap);
            }
        }
        if (details.size() > 0) {
            ObjectDetailDao dao = factory.objectDetailDao();
            for (ObjectDetailEntity detail : details.values()) {
                dao.save(detail);
            }
        }
        if (sources.size() > 0) {
            SourceDao dao = factory.sourceDao();
            for (SourceEntity source : sources.values()) {
                dao.save(source);
            }
        }
        if (parts.size() > 0) {
            ParticipantDao dao = factory.participantDao();
            for (ParticipantEntity pe : parts.values()) {
                dao.save(pe);
            }
        }
        if (objects.size() > 0) {
            ObjectDao dao = factory.objectDao();
            for (ObjectEntity e : objects.values()) {
                dao.save(e);
            }
        }


    }

    private void readDoc() {
        Element el = doc.getDocumentElement();
        if (el.getLocalName().equals(ENTITIES)) {
            NodeList children = el.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node n = children.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    String name = (e).getLocalName();
                    if (name.equals(CODES)) {
                        readCodes(e);
                    } else if (name.equals(NETWORK_ACCESS_POINTS)) {
                        readNaps(e);
                    }
                }
            }
            for (int i = 0; i < children.getLength(); i++) {
                Node n = children.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    String name = (e).getLocalName();
                    if (name.equals(SOURCES)) {
                        readSources(e);
                    } else if (name.equals(PARTICIPANTS)) {
                        readParts(e);
                    } else if (name.equals(OBJECTS)) {
                        readObjects(e);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown XML format");
        }
    }

    private void readCodes(Element codes) {
        NodeList children = codes.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                readCode((Element) n);
            }
        }
    }

    private void readCode(Element el) {
        String type = el.getLocalName();
        CodeEntity entity = null;
        if (type.equals(CODE_EVENT_ID)) {
            entity = new EventIdCodeEntity();
        } else if (type.equals(CODE_EVENT_TYPE)) {
            entity = new EventTypeCodeEntity();
        } else if (type.equals(CODE_OBJ_ID_TYPE)) {
            entity = new ObjectIdTypeCodeEntity();
        } else if (type.equals(CODE_PARTICIPANT_TYPE)) {
            entity = new ParticipantCodeEntity();
        } else if (type.equals(CODE_SOURCE)) {
            entity = new SourceCodeEntity();
        }
        if (entity == null) {
            return;
        }
        String code = el.getAttribute(CODE);
        if (nill(code)) {
            System.out.println("no code defined in coded value. Not loading...");
            return;
        }
        entity.setCode(code);
        String sys = el.getAttribute(CODE_SYSTEM);
        String name = el.getAttribute(CODE_SYSTEM_NAME);
        String dis = el.getAttribute(DISPLAY_NAME);
        String orig = el.getAttribute(ORIGINAL_TEXT);
        entity.setCodeSystem(nill(sys) ? null : sys);
        entity.setCodeSystemName(nill(name) ? null : name);
        entity.setDisplayName(nill(dis) ? null : dis);
        entity.setOriginalText(nill(orig) ? null : orig);
        codes.put(id(el), entity);
    }


    private void readNaps(Element codes) {
        NodeList children = codes.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(NETWORK_ACCESS_POINT)) {
                readNap((Element) n);
            }
        }
    }

    private void readNap(Element el) {
        String netId = el.getAttribute(NETWORK_ACCESS_POINT_ID);
        String type = el.getAttribute(TYPE);
        if (nill(netId) || nill(type)) {
            System.out.println("no identifier or type defined in network access point. Not loading...");
            return;
        }
        NetworkAccessPointEntity e = new NetworkAccessPointEntity();
        e.setIdentifier(netId);
        e.setType(new Short(type));
        naps.put(id(el), e);
    }

    private void readSources(Element codes) {
        NodeList children = codes.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(SOURCE)) {
                readSource((Element) n);
            }
        }
    }

    private void readSource(Element el) {
        String sourceId = el.getAttribute(SOURCE_ID);
        if (nill(sourceId)) {
            System.out.println("No Source id set. Not loading...");
            return;
        }
        String ent = el.getAttribute(ENT_SITE_ID);
        SourceEntity e = new SourceEntity();
        e.setSourceId(sourceId);
        e.setEnterpriseSiteId(nill(ent) ? null : ent);
        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(SOURCE_TYPE)) {
                Element ch = (Element) n;
                String ref = ch.getAttribute(REF);
                if (nill(ref)) {
                    continue;
                }
                CodeEntity code = codes.get(ref);
                if (code != null && code instanceof SourceCodeEntity) {
                    e.getSourceTypeCodes().add((SourceCodeEntity) code);
                }
            }
        }
        sources.put(id(el), e);
    }

    private void readParts(Element codes) {
        NodeList children = codes.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(PARTICIPANT)) {
                readPart((Element) n);
            }
        }
    }

    private void readPart(Element el) {
        String partId = el.getAttribute(USER_ID);
        if (nill(partId)) {
            System.out.println("no active participant id defined. Not loading...");
        }
        String name = el.getAttribute(USER_NAME);
        String alt = el.getAttribute(ALT_USER_ID);
        ParticipantEntity e = new ParticipantEntity();
        e.setUserId(partId);
        e.setUserName(nill(name) ? null : name);
        e.setAlternativeUserId(nill(alt) ? null : alt);
        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(PARTICIPANT_TYPE)) {
                Element ch = (Element) n;
                String ref = ch.getAttribute(REF);
                if (nill(ref)) {
                    continue;
                }
                CodeEntity code = codes.get(ref);
                if (code != null && code instanceof ParticipantCodeEntity) {
                    e.getCodes().add((ParticipantCodeEntity) code);
                }
            }
        }
        parts.put(id(el), e);
    }

    private void readObjects(Element codes) {
        NodeList children = codes.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element && n.getLocalName().equals(OBJECT)) {
                readObject((Element) n);
            }
        }
    }

    private void readObject(Element el) {
        String obId = el.getAttribute(OBJECT_ID);
        if (nill(obId)) {
            System.out.println("no participating object id defined. Not loading...");
        }
        String name = el.getAttribute(OBJECT_NAME);
        String type = el.getAttribute(OBJECT_TYPE_CODE);
        String role = el.getAttribute(OBJECT_TYPE_CODE_ROLE);
        String sens = el.getAttribute(OBJECT_SENSITIVITY);
        ObjectEntity e = new ObjectEntity();
        e.setObjectId(obId);
        e.setObjectName(nill(name) ? null : name);
        e.setObjectSensitivity(nill(sens) ? null : sens);
        e.setObjectTypeCode(nill(type) ? null : Short.valueOf(type));
        e.setObjectTypeCodeRole(nill(role) ? null : Short.valueOf(role));
        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                Element ele = (Element) n;
                if (ele.getLocalName().equals(OBJECT_ID_TYPE)) {
                    String ref = ele.getAttribute(REF);
                    if (nill(ref)) {
                        System.out.println("no object id type defined. Not loading...");
                        return;
                    }
                    CodeEntity code = codes.get(ref);
                    if (code != null && code instanceof ObjectIdTypeCodeEntity) {
                        e.setObjectIdTypeCode((ObjectIdTypeCodeEntity) code);
                    } else {
                        System.out.println("no object id type defined. Not loading...");
                        return;
                    }
                } else if (ele.getLocalName().equals(OBJECT_DETAIL_KEY)) {
                    String key = ele.getAttribute(KEY);
                    if (key != null) {
                        e.addObjectDetailType(key);
                    }
                }
            }
        }
        if (e.getObjectIdTypeCode() == null) {
            System.out.println("no object id type defined. Not loading...");
            return;
        }
        objects.put(id(el), e);
    }


    private static Document newDocument(InputStream stream) throws IOException {
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

    private String id(Element el) {
        String id = el.getAttribute(ID);
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    private boolean nill(String val) {
        if (val == null || val.trim().length() == 0) {
            return true;
        }
        return false;
    }


}
