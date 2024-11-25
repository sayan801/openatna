package org.openhealthtools.openatna.archive;

import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageReader {

    private final EntityReader entityReader = new EntityReader();

    public void begin(XMLEventReader reader) throws XMLStreamException {

        ReadUtils.dig(reader, DataConstants.MESSAGES);
    }

    public List<MessageEntity> readMessages(int max, XMLEventReader reader) throws XMLStreamException {

        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<MessageEntity> ret = new ArrayList<>();
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
                    se.setEventDateTime(Archiver.parseDate(a.getValue()));
                } catch (ParseException e) {
                    throw new XMLStreamException(e);
                }
            }
        }
        while (true) {

            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                switch (el.getName().getLocalPart()) {
                    case DataConstants.EVT_ID:
                        code = reader.nextTag();
                        attrs = ReadUtils.getAttributes(code);
                        se.setEventId(entityReader.readCode(attrs, EventIdCodeEntity.class));
                        break;
                    case DataConstants.EVT_TYPE:
                        code = reader.nextTag();
                        attrs = ReadUtils.getAttributes(code);
                        se.addEventTypeCode(entityReader.readCode(attrs, EventTypeCodeEntity.class));
                        break;
                    case DataConstants.MESSAGE_SOURCES:
                        List<MessageSourceEntity> sources = readSources(0, reader);
                        for (MessageSourceEntity source : sources) {
                            se.addMessageSource(source);
                        }
                        break;
                    case DataConstants.MESSAGE_PARTICIPANTS: {
                        List<MessageParticipantEntity> pes = readParticipants(0, reader);
                        for (MessageParticipantEntity pe : pes) {
                            se.addMessageParticipant(pe);
                        }
                        break;
                    }
                    case DataConstants.MESSAGE_OBJECTS: {
                        List<MessageObjectEntity> pes = readObjects(0, reader);
                        for (MessageObjectEntity pe : pes) {
                            se.addMessageObject(pe);
                        }
                        break;
                    }
                    default:
                        reader.nextEvent();
                        break;
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
        List<MessageSourceEntity> ret = new ArrayList<>();
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
        List<MessageParticipantEntity> ret = new ArrayList<>();
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
        List<MessageObjectEntity> ret = new ArrayList<>();
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
