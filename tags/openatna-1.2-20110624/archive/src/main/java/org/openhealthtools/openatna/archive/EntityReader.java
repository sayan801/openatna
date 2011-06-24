/**
 * Copyright (c) 2009-2011 University of Cardiff and others.
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

package org.openhealthtools.openatna.archive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectDescriptionEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.SopClassEntity;
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 27, 2010: 5:56:09 PM
 */

public class EntityReader {

    public void begin(XMLEventReader reader) throws XMLStreamException {
        ReadUtils.dig(reader, DataConstants.ENTITIES);
    }

    public SourceEntity readSource(XMLEventReader reader) throws XMLStreamException {
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.SOURCE);
        List<Attribute> attrs = ReadUtils.getAttributes(evt);
        SourceEntity se = new SourceEntity();
        for (Attribute a : attrs) {
            String attr = a.getName().getLocalPart();
            if (attr.equalsIgnoreCase(DataConstants.SOURCE_ID)) {
                se.setSourceId(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.ENT_SITE_ID)) {
                se.setEnterpriseSiteId(a.getValue());
            }
        }
        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                if (el.getName().getLocalPart().equals(DataConstants.SOURCE_TYPE)) {
                    code = reader.nextTag();
                    attrs = ReadUtils.getAttributes(code);
                    se.addSourceTypeCode(readCode(attrs, SourceCodeEntity.class));
                } else {
                    reader.nextTag();
                }
            } else if (code.isEndElement()) {
                EndElement el = code.asEndElement();
                if (el.getName().getLocalPart().equals(DataConstants.SOURCE)) {
                    break;
                }
                reader.nextEvent();
            } else {
                reader.nextEvent();

            }
        }
        return se;
    }

    public SopClassEntity readSopClass(XMLEventReader reader, XMLEvent start) throws XMLStreamException {
        SopClassEntity sc = new SopClassEntity();
        List<Attribute> as = ReadUtils.getAttributes(start);
        if (as.size() == 1 && as.get(0).getName().getLocalPart().equals(DataConstants.SOP_ID)) {
            sc.setSopId(as.get(0).getValue());
        }
        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement ste = code.asStartElement();

                if (ste.getName().getLocalPart().equals(DataConstants.INSTANCE_UIDS)) {
                    sc.setInstanceUids(reader.getElementText().trim());
                } else if (ste.getName().getLocalPart().equals(DataConstants.NUMBER_OF_INSTANCES)) {
                    sc.setNumberOfInstances(Integer.valueOf(reader.getElementText().trim()));
                }
            } else if (code.isEndElement()) {
                EndElement end = code.asEndElement();
                if (end.getName().getLocalPart().equals(DataConstants.SOP_CLASS)) {
                    break;
                }
                reader.nextEvent();
            } else {
                reader.nextEvent();
            }
        }
        return sc;
    }

    public ObjectDescriptionEntity readDescription(XMLEventReader reader) throws XMLStreamException {
        ObjectDescriptionEntity desc = new ObjectDescriptionEntity();

        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement start = code.asStartElement();
                if (start.getName().getLocalPart().equals(DataConstants.ACCESSION_NUMBERS)) {
                    String val = reader.getElementText().trim();
                    desc.setAccessionNumbers(val);
                } else if (start.getName().getLocalPart().equals(DataConstants.MPPS_UIDS)) {
                    String val = reader.getElementText().trim();
                    desc.setMppsUids(val);
                } else if (start.getName().getLocalPart().equals(DataConstants.SOP_CLASS)) {
                    code = reader.nextEvent();
                    desc.addSopClass(readSopClass(reader, code));
                } else {
                    reader.nextEvent();
                }
            } else if (code.isEndElement()) {
                EndElement end = code.asEndElement();
                if (end.getName().getLocalPart().equals(DataConstants.OBJECT_DESCRIPTION)) {
                    break;
                }
                reader.nextEvent();
            } else {
                reader.nextEvent();
            }
        }
        return desc;
    }

    public ObjectEntity readObject(XMLEventReader reader) throws XMLStreamException {
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.OBJECT);
        List<Attribute> attrs = ReadUtils.getAttributes(evt);
        ObjectEntity se = new ObjectEntity();
        for (Attribute a : attrs) {
            String attr = a.getName().getLocalPart();
            if (attr.equalsIgnoreCase(DataConstants.OBJECT_ID)) {
                se.setObjectId(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.OBJECT_TYPE_CODE)) {
                se.setObjectTypeCode(Short.valueOf(a.getValue()));
            } else if (attr.equalsIgnoreCase(DataConstants.OBJECT_TYPE_CODE_ROLE)) {
                se.setObjectTypeCodeRole(Short.valueOf(a.getValue()));
            } else if (attr.equalsIgnoreCase(DataConstants.OBJECT_NAME)) {
                se.setObjectName(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.OBJECT_SENSITIVITY)) {
                se.setObjectSensitivity(a.getValue());
            }
        }
        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                if (el.getName().getLocalPart().equals(DataConstants.OBJECT_ID_TYPE)) {
                    code = reader.nextTag();
                    attrs = ReadUtils.getAttributes(code);
                    se.setObjectIdTypeCode(readCode(attrs, ObjectIdTypeCodeEntity.class));
                } else if (el.getName().getLocalPart().equals(DataConstants.DETAIL)) {
                    code = reader.nextTag();
                    attrs = ReadUtils.getAttributes(code);
                    if (attrs.size() == 1 && attrs.get(0).getName().getLocalPart().equals(DataConstants.VALUE)) {
                        se.addObjectDetailType(attrs.get(0).getValue());
                    }
                    se.setObjectIdTypeCode(readCode(attrs, ObjectIdTypeCodeEntity.class));
                } else if (el.getName().getLocalPart().equals(DataConstants.OBJECT_DESCRIPTIONS)) {

                    code = ReadUtils.dig(reader, DataConstants.OBJECT_DESCRIPTION);
                    while (evt != null) {
                        se.addObjectDescription(readDescription(reader));
                        evt = ReadUtils.dig(reader, DataConstants.OBJECT_DESCRIPTION);
                    }
                } else {
                    reader.nextEvent();
                }
            } else if (code.isEndElement()) {
                EndElement el = code.asEndElement();
                if (el.getName().getLocalPart().equals(DataConstants.OBJECT)) {
                    break;
                }
                reader.nextEvent();
            } else {
                reader.nextEvent();
            }
        }
        return se;
    }

    public ParticipantEntity readParticipant(XMLEventReader reader) throws XMLStreamException {
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.PARTICIPANT);
        List<Attribute> attrs = ReadUtils.getAttributes(evt);
        ParticipantEntity se = new ParticipantEntity();
        for (Attribute a : attrs) {
            String attr = a.getName().getLocalPart();
            if (attr.equalsIgnoreCase(DataConstants.USER_ID)) {
                se.setUserId(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.ALT_USER_ID)) {
                se.setAlternativeUserId(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.USER_NAME)) {
                se.setUserName(a.getValue());
            }
        }
        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                if (el.getName().getLocalPart().equals(DataConstants.PARTICIPANT_TYPE)) {
                    code = reader.nextTag();
                    attrs = ReadUtils.getAttributes(code);
                    se.addParticipantTypeCode(readCode(attrs, ParticipantCodeEntity.class));
                } else {
                    reader.nextEvent();
                }
            } else if (code.isEndElement()) {
                EndElement el = code.asEndElement();
                if (el.getName().getLocalPart().equals(DataConstants.PARTICIPANT)) {
                    break;
                }
                reader.nextEvent();
            } else {
                reader.nextEvent();
            }
        }
        return se;
    }


    public <C extends CodeEntity> C readCode(List<Attribute> attr, Class<C> cls) throws XMLStreamException {
        try {
            C ret = cls.newInstance();
            for (Attribute attribute : attr) {
                String name = attribute.getName().getLocalPart();
                String val = attribute.getValue();
                if (name.equals(DataConstants.CODE)) {
                    ret.setCode(val);
                } else if (name.equals(DataConstants.CODE_SYSTEM)) {
                    ret.setCodeSystem(val);
                } else if (name.equals(DataConstants.CODE_SYSTEM_NAME)) {
                    ret.setCodeSystemName(val);
                } else if (name.equals(DataConstants.DISPLAY_NAME)) {
                    ret.setDisplayName(val);
                }
            }
            return ret;

        } catch (Exception e) {
            throw new XMLStreamException(e);
        }
    }

    public NetworkAccessPointEntity readNap(XMLEventReader reader) throws XMLStreamException {
        XMLEvent evt = reader.nextEvent();
        Map<String, String> attr = ReadUtils.getAttributeMap(evt);
        String type = attr.get(DataConstants.TYPE);
        String val = attr.get(DataConstants.NETWORK_ACCESS_POINT_ID);
        return new NetworkAccessPointEntity(Short.valueOf(type), val);
    }

    public CodeEntity readCode(XMLEventReader reader) throws XMLStreamException {
        XMLEvent evt = reader.nextEvent();
        List<Attribute> attr = ReadUtils.getAttributes(evt);
        if (evt.isStartElement()) {
            StartElement el = evt.asStartElement();
            Class<? extends CodeEntity> cls = getClassForTag(el.getName().getLocalPart());
            return readCode(attr, cls);
        }
        return null;
    }

    private Class<? extends CodeEntity> getClassForTag(String tag) {
        if (tag.equals(DataConstants.EVT_ID)) {
            return EventIdCodeEntity.class;
        } else if (tag.equals(DataConstants.EVT_TYPE)) {
            return EventTypeCodeEntity.class;
        } else if (tag.equals(DataConstants.SOURCE_TYPE)) {
            return SourceCodeEntity.class;
        } else if (tag.equals(DataConstants.PARTICIPANT_TYPE)) {
            return ParticipantCodeEntity.class;
        } else if (tag.equals(DataConstants.OBJECT_ID_TYPE)) {
            return ObjectIdTypeCodeEntity.class;
        }
        return null;
    }

    public List<NetworkAccessPointEntity> readNaps(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<NetworkAccessPointEntity> ret = new ArrayList<NetworkAccessPointEntity>();
        boolean is = ReadUtils.peek(reader, DataConstants.NETWORK_ACCESS_POINT);
        while (is && ret.size() < max) {
            ret.add(readNap(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.NETWORK_ACCESS_POINT);
        }
        return ret;
    }

    public List<CodeEntity> readCodes(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<CodeEntity> ret = new ArrayList<CodeEntity>();
        boolean is = ReadUtils.peek(reader, DataConstants.EVT_ID,
                DataConstants.EVT_TYPE, DataConstants.SOURCE_TYPE,
                DataConstants.PARTICIPANT_TYPE, DataConstants.OBJECT_ID_TYPE);
        while (is && ret.size() < max) {
            ret.add(readCode(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.EVT_ID,
                    DataConstants.EVT_TYPE, DataConstants.SOURCE_TYPE,
                    DataConstants.PARTICIPANT_TYPE, DataConstants.OBJECT_ID_TYPE);
        }
        return ret;
    }


    public List<SourceEntity> readSources(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<SourceEntity> ret = new ArrayList<SourceEntity>();
        boolean is = ReadUtils.peek(reader, DataConstants.SOURCE);
        while (is && ret.size() < max) {
            ret.add(readSource(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.SOURCE);
        }
        return ret;
    }

    public List<ObjectEntity> readObjects(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<ObjectEntity> ret = new ArrayList<ObjectEntity>();
        boolean is = ReadUtils.peek(reader, DataConstants.OBJECT);
        while (is && ret.size() < max) {
            ret.add(readObject(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.OBJECT);
        }
        return ret;
    }

    public XMLEvent beginType(XMLEventReader reader, String type) throws XMLStreamException {
        return ReadUtils.dig(reader, type);
    }

    public void endType(XMLEventReader reader) throws XMLStreamException {
        reader.nextTag();
    }


    public List<ParticipantEntity> readParticipants(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<ParticipantEntity> ret = new ArrayList<ParticipantEntity>();
        boolean is = ReadUtils.peek(reader, DataConstants.PARTICIPANT);
        while (is && ret.size() < max) {
            ret.add(readParticipant(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.PARTICIPANT);
        }
        return ret;
    }

}