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

package org.openhealthtools.openatna.audit.persistence.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import org.w3c.dom.Element;
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

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 25, 2010: 4:13:15 PM
 */

public class DataWriter {

    private Document doc;
    private Element root;

    public DataWriter() {
        try {
            this.doc = newDocument();
            root = doc.createElement(DataConstants.ENTITIES);
            doc.appendChild(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(File f) throws IOException {
        FileOutputStream fout = new FileOutputStream(f);
        transform(doc, fout, false);
        fout.flush();
        fout.close();
    }


    private static StreamResult transform(Document doc, OutputStream out, boolean indent) throws IOException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            if (indent) {
                t.setOutputProperty(OutputKeys.INDENT, "yes");
            } else {
                t.setOutputProperty(OutputKeys.INDENT, "no");
            }
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

    public void writeSources(List<SourceEntity> sources) {
        if (sources.size() > 0) {
            Element els = doc.createElement(DataConstants.SOURCES);
            for (SourceEntity source : sources) {
                Element el = doc.createElement(DataConstants.SOURCE);
                el.setAttribute(DataConstants.SOURCE_ID, source.getSourceId());
                if (source.getEnterpriseSiteId() != null) {
                    el.setAttribute(DataConstants.ENT_SITE_ID, source.getEnterpriseSiteId());
                }
                Set<SourceCodeEntity> codes = source.getSourceTypeCodes();
                for (SourceCodeEntity codeEntity : codes) {
                    Element codeEl = doc.createElement(DataConstants.SOURCE_TYPE);
                    codeEl.setAttribute(DataConstants.CODE, codeEntity.getCode());
                    if (codeEntity.getCodeSystem() != null) {
                        codeEl.setAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
                    }
                    if (codeEntity.getCodeSystemName() != null) {
                        codeEl.setAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
                    }
                    if (codeEntity.getDisplayName() != null) {
                        codeEl.setAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
                    }
                    el.appendChild(codeEl);
                }
                els.appendChild(el);
            }
            root.appendChild(els);
        }
    }

    public void writeParticipants(List<ParticipantEntity> entities) {
        if (entities.size() > 0) {
            Element els = doc.createElement(DataConstants.PARTICIPANTS);
            for (ParticipantEntity entity : entities) {
                Element el = doc.createElement(DataConstants.PARTICIPANT);
                el.setAttribute(DataConstants.USER_ID, entity.getUserId());
                if (entity.getUserName() != null) {
                    el.setAttribute(DataConstants.USER_NAME, entity.getUserName());
                }
                if (entity.getAlternativeUserId() != null) {
                    el.setAttribute(DataConstants.ALT_USER_ID, entity.getAlternativeUserId());
                }
                Set<ParticipantCodeEntity> codes = entity.getParticipantTypeCodes();
                for (ParticipantCodeEntity codeEntity : codes) {
                    Element codeEl = doc.createElement(DataConstants.PARTICIPANT_TYPE);
                    codeEl.setAttribute(DataConstants.CODE, codeEntity.getCode());
                    if (codeEntity.getCodeSystem() != null) {
                        codeEl.setAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
                    }
                    if (codeEntity.getCodeSystemName() != null) {
                        codeEl.setAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
                    }
                    if (codeEntity.getDisplayName() != null) {
                        codeEl.setAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
                    }
                    el.appendChild(codeEl);
                }
                els.appendChild(el);
            }
            root.appendChild(els);
        }
    }

    public void writeObjects(List<ObjectEntity> entities) {
        if (entities.size() > 0) {
            Element els = doc.createElement(DataConstants.OBJECTS);
            for (ObjectEntity entity : entities) {
                Element el = doc.createElement(DataConstants.OBJECT);
                el.setAttribute(DataConstants.OBJECT_ID, entity.getObjectId());
                if (entity.getObjectName() != null) {
                    el.setAttribute(DataConstants.OBJECT_NAME, entity.getObjectName());
                }
                if (entity.getObjectTypeCode() != null) {
                    el.setAttribute(DataConstants.OBJECT_TYPE_CODE, Short.toString(entity.getObjectTypeCode()));
                }
                if (entity.getObjectTypeCodeRole() != null) {
                    el.setAttribute(DataConstants.OBJECT_TYPE_CODE_ROLE, Short.toString(entity.getObjectTypeCodeRole()));
                }
                if (entity.getObjectSensitivity() != null) {
                    el.setAttribute(DataConstants.OBJECT_SENSITIVITY, entity.getObjectSensitivity());
                }
                ObjectIdTypeCodeEntity codeEntity = entity.getObjectIdTypeCode();
                if (codeEntity != null) {
                    Element codeEl = doc.createElement(DataConstants.OBJECT_ID_TYPE);
                    codeEl.setAttribute(DataConstants.CODE, codeEntity.getCode());
                    if (codeEntity.getCodeSystem() != null) {
                        codeEl.setAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
                    }
                    if (codeEntity.getCodeSystemName() != null) {
                        codeEl.setAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
                    }
                    if (codeEntity.getDisplayName() != null) {
                        codeEl.setAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
                    }
                    el.appendChild(codeEl);
                }
                Set<DetailTypeEntity> details = entity.getObjectDetailTypes();
                for (DetailTypeEntity detail : details) {
                    Element detailEl = doc.createElement(DataConstants.DETAIL);
                    detailEl.setAttribute(DataConstants.TYPE, detail.getType());
                    el.appendChild(detailEl);
                }
                Set<ObjectDescriptionEntity> descs = entity.getObjectDescriptions();
                for (ObjectDescriptionEntity desc : descs) {
                    Element descEl = doc.createElement(DataConstants.OBJECT_DESCRIPTION);
                    if (desc.getAccessionNumbers() != null) {
                        descEl.setAttribute(DataConstants.ACCESSION_NUMBERS, desc.getAccessionNumbers());
                    }
                    if (desc.getMppsUids() != null) {
                        descEl.setAttribute(DataConstants.MPPS_UIDS, desc.getMppsUids());
                    }
                    el.appendChild(descEl);
                    Set<SopClassEntity> sops = desc.getSopClasses();
                    for (SopClassEntity sop : sops) {
                        Element sopEl = doc.createElement(DataConstants.SOP_CLASS);
                        if (sop.getInstanceUids() != null) {
                            sopEl.setAttribute(DataConstants.INSTANCE_UIDS, sop.getInstanceUids());
                        }
                        if (sop.getNumberOfInstances() != null) {
                            sopEl.setAttribute(DataConstants.NUMBER_OF_INSTANCES, Integer.toString(sop.getNumberOfInstances()));
                        }
                        if (sop.getSopId() != null) {
                            sopEl.setAttribute(DataConstants.SOP_ID, sop.getSopId());
                        }
                        descEl.appendChild(sopEl);
                    }
                }
                els.appendChild(el);
            }
            root.appendChild(els);
        }
    }

    public void writeNaps(List<NetworkAccessPointEntity> naps) {
        if (naps.size() > 0) {
            Element els = doc.createElement(DataConstants.NETWORK_ACCESS_POINTS);
            for (NetworkAccessPointEntity nap : naps) {
                Element el = doc.createElement(DataConstants.NETWORK_ACCESS_POINT);
                el.setAttribute(DataConstants.NETWORK_ACCESS_POINT_ID, nap.getIdentifier());
                el.setAttribute(DataConstants.TYPE, Short.toString(nap.getType()));
                els.appendChild(el);
            }
            root.appendChild(els);
        }
    }

    public void writeCodes(List<CodeEntity> ents) {
        if (ents.size() > 0) {
            Element els = doc.createElement(DataConstants.CODES);
            List<List<CodeEntity>> lists = sort(ents);

            for (List<CodeEntity> list : lists) {
                Element typeEl = doc.createElement(DataConstants.CODE_TYPE);
                if (list.size() > 0) {
                    CodeEntity ce = list.get(0);
                    CodeEntity.CodeType type = ce.getType();
                    switch (type) {
                        case ACTIVE_PARTICIPANT:
                            typeEl.setAttribute(DataConstants.NAME, DataConstants.CODE_PARTICIPANT_TYPE);
                            break;
                        case AUDIT_SOURCE:
                            typeEl.setAttribute(DataConstants.NAME, DataConstants.CODE_SOURCE);
                            break;
                        case EVENT_ID:
                            typeEl.setAttribute(DataConstants.NAME, DataConstants.CODE_EVENT_ID);
                            break;
                        case EVENT_TYPE:
                            typeEl.setAttribute(DataConstants.NAME, DataConstants.CODE_EVENT_TYPE);
                            break;
                        case PARTICIPANT_OBJECT_ID_TYPE:
                            typeEl.setAttribute(DataConstants.NAME, DataConstants.CODE_OBJ_ID_TYPE);
                            break;
                        default:
                            break;
                    }
                    if (typeEl.getAttribute(DataConstants.NAME) != null && typeEl.getAttribute(DataConstants.NAME).length() > 0) {
                        for (CodeEntity codeEntity : list) {
                            Element codeEl = doc.createElement(DataConstants.CODE);
                            codeEl.setAttribute(DataConstants.CODE, codeEntity.getCode());
                            if (codeEntity.getCodeSystem() != null) {
                                codeEl.setAttribute(DataConstants.CODE_SYSTEM, codeEntity.getCodeSystem());
                            }
                            if (codeEntity.getCodeSystemName() != null) {
                                codeEl.setAttribute(DataConstants.CODE_SYSTEM_NAME, codeEntity.getCodeSystemName());
                            }
                            if (codeEntity.getDisplayName() != null) {
                                codeEl.setAttribute(DataConstants.DISPLAY_NAME, codeEntity.getDisplayName());
                            }
                            typeEl.appendChild(codeEl);
                        }
                        els.appendChild(typeEl);
                    }
                }
            }
            root.appendChild(els);
        }
    }

    private List<List<CodeEntity>> sort(List<CodeEntity> ents) {
        List<CodeEntity> eventIds = new ArrayList<CodeEntity>();
        List<CodeEntity> eventTypes = new ArrayList<CodeEntity>();
        List<CodeEntity> objTypeCodes = new ArrayList<CodeEntity>();
        List<CodeEntity> participantTypes = new ArrayList<CodeEntity>();
        List<CodeEntity> sourceTypes = new ArrayList<CodeEntity>();
        for (CodeEntity ent : ents) {
            CodeEntity.CodeType type = ent.getType();
            switch (type) {
                case ACTIVE_PARTICIPANT:
                    participantTypes.add(ent);
                    break;
                case AUDIT_SOURCE:
                    sourceTypes.add(ent);
                    break;
                case EVENT_ID:
                    eventIds.add(ent);
                    break;
                case EVENT_TYPE:
                    eventTypes.add(ent);
                    break;
                case PARTICIPANT_OBJECT_ID_TYPE:
                    objTypeCodes.add(ent);
                    break;
                default:
                    break;
            }
        }
        List<List<CodeEntity>> ret = new ArrayList<List<CodeEntity>>();
        ret.add(eventIds);
        ret.add(eventTypes);
        ret.add(objTypeCodes);
        ret.add(participantTypes);
        ret.add(sourceTypes);
        return ret;
    }

    private static Document newDocument() throws IOException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

}
