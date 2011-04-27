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

import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */

public class MessageWriter {

    private EntityWriter entityWriter = new EntityWriter();

    public void begin(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument();
        writer.writeStartElement(DataConstants.MESSAGES);
    }

    public void writeMessages(List<? extends MessageEntity> msgs, XMLStreamWriter writer) throws XMLStreamException {
        for (MessageEntity msg : msgs) {
            writer.writeStartElement(DataConstants.MESSAGE);
            if (msg.getSourceAddress() != null) {
                writer.writeAttribute(DataConstants.SOURCE_IP, msg.getSourceAddress());
            }
            if (msg.getEventActionCode() != null) {
                writer.writeAttribute(DataConstants.EVT_ACTION, msg.getEventActionCode());
            }
            if (msg.getEventOutcome() != null) {
                writer.writeAttribute(DataConstants.EVT_OUTCOME, Integer.toString(msg.getEventOutcome()));
            }
            if (msg.getEventDateTime() != null) {
                writer.writeAttribute(DataConstants.EVT_TIME, Archiver.archiveFormat.format(msg.getEventDateTime()));
            }
            if (msg.getEventId() != null) {
                entityWriter.writeCode(msg.getEventId(), writer, DataConstants.EVT_ID);
            }
            if (msg.getEventTypeCodes().size() > 0) {
                List<? extends CodeEntity> l = new ArrayList<CodeEntity>(msg.getEventTypeCodes());
                for (CodeEntity codeEntity : l) {
                    entityWriter.writeCode(codeEntity, writer, DataConstants.EVT_TYPE);
                }
            }
            Set<MessageSourceEntity> sources = msg.getMessageSources();
            writer.writeStartElement(DataConstants.MESSAGE_SOURCES);
            for (MessageSourceEntity source : sources) {
                writer.writeStartElement(DataConstants.MESSAGE_SOURCE);
                entityWriter.writeSource(source.getSource(), writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();
            Set<MessageParticipantEntity> parts = msg.getMessageParticipants();
            writer.writeStartElement(DataConstants.MESSAGE_PARTICIPANTS);
            for (MessageParticipantEntity part : parts) {
                writer.writeStartElement(DataConstants.MESSAGE_PARTICIPANT);
                if (part.isUserIsRequestor()) {
                    writer.writeAttribute(DataConstants.USER_IS_REQUESTOR, Boolean.toString(part.isUserIsRequestor()));
                }
                NetworkAccessPointEntity nap = part.getNetworkAccessPoint();
                if (nap != null) {
                    entityWriter.writeNap(nap, writer);
                }
                ParticipantEntity pe = part.getParticipant();
                entityWriter.writeParticipant(pe, writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement(DataConstants.MESSAGE_OBJECTS);
            Set<MessageObjectEntity> objs = msg.getMessageObjects();
            for (MessageObjectEntity obj : objs) {
                writer.writeStartElement(DataConstants.MESSAGE_OBJECT);
                Short cyc = obj.getObjectDataLifeCycle();
                if (cyc != null) {
                    writer.writeAttribute(DataConstants.OBJECT_DATA_LIFECYCLE, Short.toString(cyc));
                }
                byte[] query = obj.getObjectQuery();
                if (query != null && query.length > 0) {
                    writer.writeStartElement(DataConstants.OBJECT_QUERY);
                    writer.writeCharacters(Base64.encode(query));
                    writer.writeEndElement();
                }
                Set<ObjectDetailEntity> details = obj.getDetails();
                for (ObjectDetailEntity detail : details) {
                    writer.writeStartElement(DataConstants.DETAIL);
                    writer.writeStartElement(DataConstants.TYPE);
                    writer.writeCharacters(detail.getType());
                    writer.writeEndElement();
                    writer.writeStartElement(DataConstants.VALUE);
                    writer.writeCharacters(Base64.encode(detail.getValue()));
                    writer.writeEndElement();
                    writer.writeEndElement();
                }
                ObjectEntity oe = obj.getObject();
                entityWriter.writeObject(oe, writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndElement();
        }
    }


    public void finish(XMLStreamWriter writer) throws IOException {
        try {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            throw new IOException(e.getMessage());
        }
    }
}
