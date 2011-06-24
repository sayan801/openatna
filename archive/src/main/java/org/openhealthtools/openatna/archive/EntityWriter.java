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

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.openhealthtools.openatna.audit.persistence.model.DetailTypeEntity;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectDescriptionEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.SopClassEntity;
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 26, 2010: 1:54:27 PM
 */

public class EntityWriter {

    public void begin(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument();
        writer.writeStartElement(DataConstants.ENTITIES);
    }

    public void beginType(XMLStreamWriter writer, String type) throws XMLStreamException {
        writer.writeStartElement(type);
    }

    public void finishType(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeEndElement();
    }

    public void writeSources(List<? extends SourceEntity> sources, XMLStreamWriter writer) throws XMLStreamException {
        for (SourceEntity source : sources) {
            writeSource(source, writer);
        }
    }

    public void writeSource(SourceEntity source, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(DataConstants.SOURCE);
        writer.writeAttribute(DataConstants.SOURCE_ID, source.getSourceId());
        if (source.getEnterpriseSiteId() != null) {
            writer.writeAttribute(DataConstants.ENT_SITE_ID, source.getEnterpriseSiteId());
        }
        Set<SourceCodeEntity> codes = source.getSourceTypeCodes();
        for (SourceCodeEntity codeEntity : codes) {
            writer.writeStartElement(DataConstants.SOURCE_TYPE);
            writer.writeAttribute(DataConstants.CODE, codeEntity.getCode());
            if (codeEntity.getCodeSystem() != null) {
                writer.writeAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
            }
            if (codeEntity.getCodeSystemName() != null) {
                writer.writeAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
            }
            if (codeEntity.getDisplayName() != null) {
                writer.writeAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }


    public void writeParticipants(List<? extends ParticipantEntity> entities, XMLStreamWriter writer) throws XMLStreamException {
        for (ParticipantEntity entity : entities) {
            writeParticipant(entity, writer);
        }
    }

    public void writeParticipant(ParticipantEntity entity, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(DataConstants.PARTICIPANT);
        writer.writeAttribute(DataConstants.USER_ID, entity.getUserId());
        if (entity.getUserName() != null) {
            writer.writeAttribute(DataConstants.USER_NAME, entity.getUserName());
        }
        if (entity.getAlternativeUserId() != null) {
            writer.writeAttribute(DataConstants.ALT_USER_ID, entity.getAlternativeUserId());
        }
        Set<ParticipantCodeEntity> codes = entity.getParticipantTypeCodes();
        for (ParticipantCodeEntity codeEntity : codes) {
            writer.writeStartElement(DataConstants.PARTICIPANT_TYPE);
            writer.writeAttribute(DataConstants.CODE, codeEntity.getCode());
            if (codeEntity.getCodeSystem() != null) {
                writer.writeAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
            }
            if (codeEntity.getCodeSystemName() != null) {
                writer.writeAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
            }
            if (codeEntity.getDisplayName() != null) {
                writer.writeAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public void writeObjects(List<? extends ObjectEntity> entities, XMLStreamWriter writer) throws XMLStreamException {
        for (ObjectEntity entity : entities) {
            writeObject(entity, writer);
        }
    }

    public void writeObject(ObjectEntity entity, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(DataConstants.OBJECT);
        writer.writeAttribute(DataConstants.OBJECT_ID, entity.getObjectId());
        if (entity.getObjectName() != null) {
            writer.writeAttribute(DataConstants.OBJECT_NAME, entity.getObjectName());
        }
        if (entity.getObjectTypeCode() != null) {
            writer.writeAttribute(DataConstants.OBJECT_TYPE_CODE, Short.toString(entity.getObjectTypeCode()));
        }
        if (entity.getObjectTypeCodeRole() != null) {
            writer.writeAttribute(DataConstants.OBJECT_TYPE_CODE_ROLE, Short.toString(entity.getObjectTypeCodeRole()));
        }
        if (entity.getObjectSensitivity() != null) {
            writer.writeAttribute(DataConstants.OBJECT_SENSITIVITY, entity.getObjectSensitivity());
        }

        ObjectIdTypeCodeEntity codeEntity = entity.getObjectIdTypeCode();
        if (codeEntity != null) {
            writer.writeStartElement(DataConstants.OBJECT_ID_TYPE);
            writer.writeAttribute(DataConstants.CODE, codeEntity.getCode());
            if (codeEntity.getCodeSystem() != null) {
                writer.writeAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
            }
            if (codeEntity.getCodeSystemName() != null) {
                writer.writeAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
            }
            if (codeEntity.getDisplayName() != null) {
                writer.writeAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
            }
            writer.writeEndElement();
        }
        Set<DetailTypeEntity> details = entity.getObjectDetailTypes();
        for (DetailTypeEntity detail : details) {
            writer.writeStartElement(DataConstants.DETAIL);
            writer.writeAttribute(DataConstants.TYPE, detail.getType());
            writer.writeEndElement();
        }
        Set<ObjectDescriptionEntity> descs = entity.getObjectDescriptions();
        if (descs.size() > 0) {
            writer.writeStartElement(DataConstants.OBJECT_DESCRIPTIONS);
            for (ObjectDescriptionEntity desc : descs) {
                writer.writeStartElement(DataConstants.OBJECT_DESCRIPTION);
                if (desc.getAccessionNumbers() != null) {
                    writer.writeStartElement(DataConstants.ACCESSION_NUMBERS);
                    writer.writeCharacters(desc.getAccessionNumbers());
                    writer.writeEndElement();
                }
                if (desc.getMppsUids() != null) {
                    writer.writeStartElement(DataConstants.MPPS_UIDS);
                    writer.writeCharacters(desc.getMppsUids());
                    writer.writeEndElement();
                }
                Set<SopClassEntity> sops = desc.getSopClasses();
                for (SopClassEntity sop : sops) {
                    writer.writeStartElement(DataConstants.SOP_CLASS);
                    writer.writeAttribute(DataConstants.SOP_ID, sop.getSopId());
                    if (sop.getInstanceUids() != null) {
                        writer.writeStartElement(DataConstants.INSTANCE_UIDS);
                        writer.writeCharacters(sop.getInstanceUids());
                        writer.writeEndElement();
                    }
                    if (sop.getNumberOfInstances() != null) {
                        writer.writeStartElement(DataConstants.NUMBER_OF_INSTANCES);
                        writer.writeCharacters(Integer.toString(sop.getNumberOfInstances()));
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public void writeNaps(List<? extends NetworkAccessPointEntity> naps, XMLStreamWriter writer) throws XMLStreamException {
        for (NetworkAccessPointEntity nap : naps) {
            writeNap(nap, writer);
        }
    }

    public void writeNap(NetworkAccessPointEntity nap, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(DataConstants.NETWORK_ACCESS_POINT);
        writer.writeAttribute(DataConstants.NETWORK_ACCESS_POINT_ID, nap.getIdentifier());
        writer.writeAttribute(DataConstants.TYPE, Short.toString(nap.getType()));
        writer.writeEndElement();
    }

    public void writeCodes(List<? extends CodeEntity> ents, XMLStreamWriter writer) throws XMLStreamException {
        for (CodeEntity ent : ents) {
            String type = getTagForCode(ent);
            writeCode(ent, writer, type);
        }
    }

    private String getTagForCode(CodeEntity code) {
        CodeEntity.CodeType type = code.getType();
        switch (type) {
            case ACTIVE_PARTICIPANT:
                return DataConstants.PARTICIPANT_TYPE;
            case AUDIT_SOURCE:
                return DataConstants.SOURCE_TYPE;
            case EVENT_ID:
                return DataConstants.EVT_ID;
            case EVENT_TYPE:
                return DataConstants.EVT_TYPE;
            case PARTICIPANT_OBJECT_ID_TYPE:
                return DataConstants.OBJECT_ID_TYPE;
            default:
                return null;
        }
    }

    public void writeCode(CodeEntity codeEntity, XMLStreamWriter writer) throws XMLStreamException {
        writeCode(codeEntity, writer, DataConstants.CODE);
    }

    public void writeCode(CodeEntity codeEntity, XMLStreamWriter writer, String type) throws XMLStreamException {
        writer.writeStartElement(type);
        writer.writeAttribute(DataConstants.CODE, codeEntity.getCode());
        if (codeEntity.getCodeSystem() != null) {
            writer.writeAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
        }
        if (codeEntity.getCodeSystemName() != null) {
            writer.writeAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
        }
        if (codeEntity.getDisplayName() != null) {
            writer.writeAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
        }
        writer.writeEndElement();
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
