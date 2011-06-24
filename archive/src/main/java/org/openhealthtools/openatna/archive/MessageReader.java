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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageSourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectDetailEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 27, 2010: 5:56:09 PM
 */

public class MessageReader {

    private EntityReader entityReader = new EntityReader();


    public void begin(XMLEventReader reader) throws XMLStreamException {
        ReadUtils.dig(reader, DataConstants.MESSAGES);
    }


    public List<MessageEntity> readMessages(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<MessageEntity> ret = new ArrayList<MessageEntity>();
        boolean is = ReadUtils.peek(reader, DataConstants.MESSAGE);
        while (is && ret.size() < max) {
            ret.add(readMessage(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.MESSAGE);
        }
        return ret;
    }


    public MessageEntity readMessage(XMLEventReader reader) throws XMLStreamException {
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.MESSAGE);
        List<Attribute> attrs = ReadUtils.getAttributes(evt);
        MessageEntity se = new MessageEntity();
        for (Attribute a : attrs) {
            String attr = a.getName().getLocalPart();
            if (attr.equalsIgnoreCase(DataConstants.SOURCE_IP)) {
                se.setSourceAddress(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.EVT_ACTION)) {
                se.setEventActionCode(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.EVT_OUTCOME)) {
                se.setEventOutcome(Integer.parseInt(a.getValue()));
            } else if (attr.equalsIgnoreCase(DataConstants.EVT_TIME)) {
                try {
                    se.setEventDateTime(Archiver.archiveFormat.parse(a.getValue()));
                } catch (ParseException e) {
                    throw new XMLStreamException(e);
                }
            }
        }
        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                if (el.getName().getLocalPart().equals(DataConstants.EVT_ID)) {
                    code = reader.nextTag();
                    attrs = ReadUtils.getAttributes(code);
                    se.setEventId(entityReader.readCode(attrs, EventIdCodeEntity.class));
                } else if (el.getName().getLocalPart().equals(DataConstants.EVT_TYPE)) {
                    code = reader.nextTag();
                    attrs = ReadUtils.getAttributes(code);
                    se.addEventTypeCode(entityReader.readCode(attrs, EventTypeCodeEntity.class));
                } else if (el.getName().getLocalPart().equals(DataConstants.MESSAGE_SOURCES)) {
                    List<MessageSourceEntity> sources = readSources(0, reader);
                    for (MessageSourceEntity source : sources) {
                        se.addMessageSource(source);
                    }
                } else if (el.getName().getLocalPart().equals(DataConstants.MESSAGE_PARTICIPANTS)) {
                    List<MessageParticipantEntity> pes = readParticipants(0, reader);
                    for (MessageParticipantEntity pe : pes) {
                        se.addMessageParticipant(pe);
                    }
                } else if (el.getName().getLocalPart().equals(DataConstants.MESSAGE_OBJECTS)) {
                    List<MessageObjectEntity> pes = readObjects(0, reader);
                    for (MessageObjectEntity pe : pes) {
                        se.addMessageObject(pe);
                    }
                } else {
                    reader.nextEvent();
                }
            } else if (code.isEndElement()) {
                EndElement el = code.asEndElement();
                if (el.getName().getLocalPart().equals(DataConstants.MESSAGE)) {
                    // got to end of entity
                    break;
                }
                // move on one event
                reader.nextEvent();
            } else {
                // move on - it's a comment or space or something
                reader.nextEvent();

            }
        }
        return se;
    }

    public List<MessageSourceEntity> readSources(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        ReadUtils.dig(reader, DataConstants.MESSAGE_SOURCES);
        List<MessageSourceEntity> ret = new ArrayList<MessageSourceEntity>();
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.MESSAGE_SOURCE);
        while (evt != null && ret.size() < max) {
            MessageSourceEntity mse = new MessageSourceEntity(entityReader.readSource(reader));
            ret.add(mse);
            reader.nextTag();
            evt = ReadUtils.dig(reader, DataConstants.MESSAGE_SOURCE);
        }
        return ret;
    }

    public List<MessageParticipantEntity> readParticipants(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        ReadUtils.dig(reader, DataConstants.MESSAGE_PARTICIPANTS);
        List<MessageParticipantEntity> ret = new ArrayList<MessageParticipantEntity>();
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.MESSAGE_PARTICIPANT);
        while (evt != null && ret.size() < max) {
            MessageParticipantEntity mpe = new MessageParticipantEntity();
            Map<String, String> attr = ReadUtils.getAttributeMap(evt);
            String requestor = attr.get(DataConstants.USER_IS_REQUESTOR);
            if (requestor != null) {
                mpe.setUserIsRequestor(Boolean.valueOf(requestor));
            }
            while (true) {
                evt = reader.peek();
                if (evt.isStartElement()) {
                    StartElement se = evt.asStartElement();
                    if (se.getName().getLocalPart().equals(DataConstants.NETWORK_ACCESS_POINT)) {
                        NetworkAccessPointEntity nap = entityReader.readNap(reader);
                        mpe.setNetworkAccessPoint(nap);
                    } else if (se.getName().getLocalPart().equals(DataConstants.PARTICIPANT)) {
                        mpe.setParticipant(entityReader.readParticipant(reader));

                    }
                } else if (evt.isEndElement()) {
                    EndElement end = evt.asEndElement();
                    if (end.getName().getLocalPart().equals(DataConstants.MESSAGE_PARTICIPANT)) {
                        break;
                    }
                    evt = reader.nextEvent();
                } else {
                    evt = reader.nextEvent();
                }
            }
            ret.add(mpe);
            evt = ReadUtils.dig(reader, DataConstants.MESSAGE_PARTICIPANT);
        }
        return ret;
    }

    public List<MessageObjectEntity> readObjects(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        ReadUtils.dig(reader, DataConstants.MESSAGE_OBJECTS);
        List<MessageObjectEntity> ret = new ArrayList<MessageObjectEntity>();
        XMLEvent evt = ReadUtils.dig(reader, DataConstants.MESSAGE_OBJECT);
        while (evt != null && ret.size() < max) {
            MessageObjectEntity moe = new MessageObjectEntity();
            Map<String, String> attr = ReadUtils.getAttributeMap(evt);
            String lifecycle = attr.get(DataConstants.OBJECT_DATA_LIFECYCLE);
            if (lifecycle != null) {
                moe.setObjectDataLifeCycle(Short.valueOf(lifecycle));
            }
            while (true) {
                evt = reader.peek();
                if (evt.isStartElement()) {
                    StartElement se = evt.asStartElement();
                    if (se.getName().getLocalPart().equals(DataConstants.OBJECT_QUERY)) {
                        byte[] val = Base64.decode(reader.getElementText());
                        moe.setObjectQuery(val);
                    } else if (se.getName().getLocalPart().equals(DataConstants.DETAIL)) {
                        ObjectDetailEntity detail = new ObjectDetailEntity();
                        evt = reader.nextTag();// detail
                        while (true) {
                            evt = reader.peek();
                            if (evt.isStartElement()) {
                                StartElement el = evt.asStartElement();
                                if (el.getName().getLocalPart().equals(DataConstants.TYPE)) {
                                    detail.setType(reader.getElementText());
                                } else if (el.getName().getLocalPart().equals(DataConstants.VALUE)) {
                                    detail.setValue(Base64.decode(reader.getElementText()));
                                }
                            } else if (evt.isEndElement()) {
                                EndElement end = evt.asEndElement();
                                if (end.getName().getLocalPart().equals(DataConstants.DETAIL)) {
                                    moe.addObjectDetail(detail);
                                    break;
                                }
                                evt = reader.nextEvent();
                            } else {
                                evt = reader.nextEvent();
                            }
                        }

                    } else if (se.getName().getLocalPart().equals(DataConstants.OBJECT)) {
                        moe.setObject(entityReader.readObject(reader));
                    }
                } else if (evt.isEndElement()) {
                    EndElement end = evt.asEndElement();
                    if (end.getName().getLocalPart().equals(DataConstants.MESSAGE_OBJECT)) {
                        break;
                    }
                    evt = reader.nextEvent();
                } else {
                    evt = reader.nextEvent();
                }
            }
            ret.add(moe);
            evt = ReadUtils.dig(reader, DataConstants.MESSAGE_OBJECT);
        }
        return ret;
    }


}
