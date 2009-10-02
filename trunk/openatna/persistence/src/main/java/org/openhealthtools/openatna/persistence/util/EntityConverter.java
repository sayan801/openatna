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

import java.util.List;
import java.util.Set;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;

/**
 * Converts between ANOM objects and persistable objects.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 6:16:59 PM
 * @date $Date:$ modified by $Author:$
 */

public class EntityConverter {


    public static MessageEntity createMessage(AtnaMessage message) {

        CodeEntity code = createCode(message.getEventCode(), CodeEntity.CodeType.EVENT_ID);
        MessageEntity ent = new MessageEntity((EventIdCodeEntity) code, new Integer(message.getEventOutcome().value()));
        ent.setEventDateTime(message.getEventDateTime());
        if (message.getEventActionCode() != null) {
            ent.setEventActionCode(message.getEventActionCode().value());
        }
        ent.setEventOutcome(new Integer(message.getEventOutcome().value()));
        List<AtnaCode> codes = message.getEventTypeCodes();
        for (AtnaCode atnaCode : codes) {
            EventTypeCodeEntity e = (EventTypeCodeEntity) createCode(atnaCode, CodeEntity.CodeType.EVENT_TYPE);
            ent.addEventTypeCode(e);
        }
        List<AtnaMessageObject> objects = message.getObjects();
        for (AtnaMessageObject object : objects) {
            ent.addMessageObject(createMessageObject(object));
        }
        List<AtnaSource> sources = message.getSources();
        for (AtnaSource source : sources) {
            ent.addMessageSource(createMessageSource(source));
        }
        List<AtnaMessageParticipant> participants = message.getParticipants();
        for (AtnaMessageParticipant participant : participants) {
            ent.addMessageParticipant(createMessageParticipant(participant));
        }

        return ent;
    }

    public static AtnaMessage createMessage(MessageEntity entity) {
        AtnaCode evtid = createCode(entity.getEventId());
        AtnaMessage msg = new AtnaMessage(evtid, EventOutcome.getOutcome(entity.getEventOutcome()));
        msg.setEventDateTime(entity.getEventDateTime());
        if (entity.getEventActionCode() != null) {
            msg.setEventActionCode(EventAction.getAction(entity.getEventActionCode()));
        }
        Set<EventTypeCodeEntity> codes = entity.getEventTypeCodes();
        for (EventTypeCodeEntity code : codes) {
            AtnaCode ac = createCode(code);
            msg.addEventTypeCode(ac);
        }
        Set<MessageParticipantEntity> ps = entity.getMessageParticipants();
        for (MessageParticipantEntity p : ps) {
            msg.addParticipant(createMessageParticipant(p));
        }
        Set<MessageObjectEntity> os = entity.getMessageObjects();
        for (MessageObjectEntity o : os) {
            msg.addObject(createMessageObject(o));
        }
        Set<MessageSourceEntity> ss = entity.getMessageSources();
        for (MessageSourceEntity s : ss) {
            msg.addSource(createMessageSource(s));
        }
        return msg;
    }

    public static MessageParticipantEntity createMessageParticipant(AtnaMessageParticipant participant) {
        MessageParticipantEntity e = new MessageParticipantEntity();
        e.setParticipant(createParticipant(participant.getParticipant()));
        e.setUserIsRequestor(participant.isUserIsRequestor());
        if (participant.getNetworkAccessPointId() != null &&
                participant.getNetworkAccessPointType() != null) {
            NetworkAccessPointEntity na = new NetworkAccessPointEntity();
            na.setIdentifier(participant.getNetworkAccessPointId());
            na.setType((short) participant.getNetworkAccessPointType().value());
            e.setNetworkAccessPoint(na);
        }
        return e;
    }

    public static AtnaMessageParticipant createMessageParticipant(MessageParticipantEntity entity) {
        AtnaParticipant ap = createParticipant(entity.getParticipant());
        AtnaMessageParticipant p = new AtnaMessageParticipant(ap);
        if (entity.isUserIsRequestor() != null) {
            p.setUserIsRequestor(entity.isUserIsRequestor());
        }
        if (entity.getNetworkAccessPoint() != null) {
            p.setNetworkAccessPointId(entity.getNetworkAccessPoint().getIdentifier());
            p.setNetworkAccessPointType(NetworkAccessPoint.getAccessPoint(
                    entity.getNetworkAccessPoint().getType()));
        }
        return p;
    }

    public static MessageObjectEntity createMessageObject(AtnaMessageObject object) {
        MessageObjectEntity e = new MessageObjectEntity();
        e.setObject(createObject(object.getObject()));
        if (object.getObjectQuery() != null && object.getObjectQuery().length > 0) {
            e.setObjectQuery(Base64.encode(object.getObjectQuery()));
        }
        if (object.getObjectDataLifeCycle() != null) {
            e.setObjectDataLifeCycle((short) object.getObjectDataLifeCycle().value());
        }
        List<AtnaObjectDetail> details = object.getObjectDetails();
        for (AtnaObjectDetail detail : details) {
            ObjectDetailEntity ent = new ObjectDetailEntity(detail.getType(), Base64.encode(detail.getValue()));
            e.addObjectDetail(ent);
        }
        return e;
    }

    public static AtnaMessageObject createMessageObject(MessageObjectEntity entity) {
        AtnaObject obj = createObject(entity.getObject());
        AtnaMessageObject o = new AtnaMessageObject(obj);
        if (entity.getObjectDataLifeCycle() != null) {
            o.setObjectDataLifeCycle(ObjectDataLifecycle.getLifecycle(entity.getObjectDataLifeCycle()));
        }
        if (entity.getObjectQuery() != null) {
            o.setObjectQuery(Base64.decode(entity.getObjectQuery()));
        }
        Set<ObjectDetailEntity> pairs = entity.getDetails();
        for (ObjectDetailEntity pair : pairs) {
            AtnaObjectDetail detail = new AtnaObjectDetail();
            detail.setType(pair.getType());
            detail.setValue(Base64.decode(pair.getValue()));
            o.addObjectDetail(detail);
        }
        return o;
    }

    public static MessageSourceEntity createMessageSource(AtnaSource source) {
        MessageSourceEntity e = new MessageSourceEntity();
        e.setSource(createSource(source));
        return e;
    }

    public static AtnaSource createMessageSource(MessageSourceEntity entity) {
        return createSource(entity.getSource());
    }


    public static ObjectEntity createObject(AtnaObject object) {
        ObjectEntity e = new ObjectEntity();
        e.setObjectId(object.getObjectId());
        e.setObjectName(object.getObjectName());
        e.setObjectSensitivity(object.getObjectSensitivity());
        ObjectIdTypeCodeEntity code = (ObjectIdTypeCodeEntity) createCode(object.getObjectIdTypeCode(),
                CodeEntity.CodeType.PARTICIPANT_OBJECT_ID_TYPE);
        e.setObjectIdTypeCode(code);
        if (object.getObjectTypeCode() != null) {
            e.setObjectTypeCode((short) object.getObjectTypeCode().value());
        }
        if (object.getObjectTypeCodeRole() != null) {
            e.setObjectTypeCodeRole((short) object.getObjectTypeCodeRole().value());
        }
        List<String> detailTypes = object.getObjectDetailTypes();
        for (String s : detailTypes) {
            e.addObjectDetailType(s);
        }
        return e;
    }

    public static AtnaObject createObject(ObjectEntity entity) {
        AtnaCode code = createCode(entity.getObjectIdTypeCode());
        AtnaObject ao = new AtnaObject(entity.getObjectId(), code);
        ao.setObjectName(entity.getObjectName());
        ao.setObjectSensitivity(entity.getObjectSensitivity());
        if (entity.getObjectTypeCode() != null) {
            ao.setObjectTypeCode(ObjectType.getType(entity.getObjectTypeCode()));
        }
        if (entity.getObjectTypeCodeRole() != null) {
            ao.setObjectTypeCodeRole(ObjectTypeCodeRole.getRole(entity.getObjectTypeCodeRole()));
        }
        Set<DetailTypeEntity> types = entity.getObjectDetailTypes();
        for (DetailTypeEntity type : types) {
            ao.addObjectDetailType(type.getType());
        }
        return ao;
    }


    public static SourceEntity createSource(AtnaSource source) {
        SourceEntity e = new SourceEntity();
        e.setSourceId(source.getSourceId());
        e.setEnterpriseSiteId(source.getEnterpriseSiteId());
        List<AtnaCode> codes = source.getSourceTypeCodes();
        for (AtnaCode code : codes) {
            SourceCodeEntity ent = (SourceCodeEntity) createCode(code,
                    CodeEntity.CodeType.AUDIT_SOURCE);
            e.addSourceTypeCode(ent);
        }
        return e;
    }

    public static AtnaSource createSource(SourceEntity entity) {
        AtnaSource as = new AtnaSource(entity.getSourceId());
        as.setEnterpriseSiteId(entity.getEnterpriseSiteId());
        Set<SourceCodeEntity> codes = entity.getSourceTypeCodes();
        for (SourceCodeEntity code : codes) {
            as.addSourceTypeCode(createCode(code));
        }
        return as;
    }

    public static ParticipantEntity createParticipant(AtnaParticipant participant) {
        ParticipantEntity e = new ParticipantEntity();
        e.setUserId(participant.getUserId());
        e.setUserName(participant.getUserName());
        e.setAlternativeUserId(participant.getAlternativeUserId());
        List<AtnaCode> codes = participant.getRoleIDCodes();
        for (AtnaCode code : codes) {
            ParticipantCodeEntity ent = (ParticipantCodeEntity) createCode(code,
                    CodeEntity.CodeType.ACTIVE_PARTICIPANT);
            e.addParticipantTypeCode(ent);
        }
        return e;
    }

    public static AtnaParticipant createParticipant(ParticipantEntity entity) {
        AtnaParticipant ap = new AtnaParticipant(entity.getUserId());
        ap.setUserName(entity.getUserName());
        ap.setAlternativeUserId(entity.getAlternativeUserId());
        Set<ParticipantCodeEntity> codes = entity.getParticipantTypeCodes();
        for (ParticipantCodeEntity code : codes) {
            ap.addRoleIDCode(createCode(code));
        }
        return ap;
    }

    public static CodeEntity createCode(AtnaCode code, CodeEntity.CodeType type) {
        switch (type) {
            case EVENT_TYPE:
                return new EventTypeCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case EVENT_ID:
                return new EventIdCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case ACTIVE_PARTICIPANT:
                return new ParticipantCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case AUDIT_SOURCE:
                return new SourceCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            case PARTICIPANT_OBJECT_ID_TYPE:
                return new ObjectIdTypeCodeEntity(code.getCode(), code.getCodeSystem(),
                        code.getCodeSystemName(), code.getDisplayName(), code.getOriginalText());
            default:
                return null;
        }
    }

    public static AtnaCode createCode(CodeEntity code) {
        AtnaCode ac = new AtnaCode(code.getCode(), code.getCodeSystem(), code.getCodeSystemName(), code.getDisplayName());
        ac.setOriginalText(code.getOriginalText());
        return ac;
    }
}
