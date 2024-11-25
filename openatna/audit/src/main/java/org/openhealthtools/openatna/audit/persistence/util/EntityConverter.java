package org.openhealthtools.openatna.audit.persistence.util;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.*;

import java.util.List;
import java.util.Set;

/**
 * Converts between ANOM objects and persistable objects.
 *
 * @author Andrew Harrison
 */
public class EntityConverter {

    private EntityConverter() {
    }

    public static MessageEntity createMessage(AtnaMessage message) {

        CodeEntity code = createCode(message.getEventCode(), CodeEntity.CodeType.EVENT_ID);
        MessageEntity messageEntity = new MessageEntity((EventIdCodeEntity) code, message.getEventOutcome().value());
        messageEntity.setEventDateTime(message.getEventDateTime());
        if (message.getSourceAddress() != null) {
            messageEntity.setSourceAddress(message.getSourceAddress());
        }
        if (message.getEventActionCode() != null) {
            messageEntity.setEventActionCode(message.getEventActionCode().value());
        }
        messageEntity.setEventOutcome(message.getEventOutcome().value());
        List<AtnaCode> codes = message.getEventTypeCodes();
        for (AtnaCode atnaCode : codes) {
            EventTypeCodeEntity e = (EventTypeCodeEntity) createCode(atnaCode, CodeEntity.CodeType.EVENT_TYPE);
            messageEntity.addEventTypeCode(e);
        }
        List<AtnaMessageObject> objects = message.getObjects();
        for (AtnaMessageObject object : objects) {
            messageEntity.addMessageObject(createMessageObject(object));
        }
        List<AtnaSource> sources = message.getSources();
        for (AtnaSource source : sources) {
            messageEntity.addMessageSource(createMessageSource(source));
        }
        List<AtnaMessageParticipant> participants = message.getParticipants();
        for (AtnaMessageParticipant participant : participants) {
            messageEntity.addMessageParticipant(createMessageParticipant(participant));
        }
        if (message.getMessageContent() != null) {
            messageEntity.setMessageContent(message.getMessageContent());
        }
        return messageEntity;
    }

    public static AtnaMessage createMessage(MessageEntity entity) {

        AtnaCode evtid = createCode(entity.getEventId());
        AtnaMessage msg = new AtnaMessage(evtid, EventOutcome.getOutcome(entity.getEventOutcome()));
        msg.setMessageId(entity.getId());
        msg.setEventDateTime(entity.getEventDateTime());
        if (entity.getSourceAddress() != null) {
            msg.setSourceAddress(entity.getSourceAddress());
        }
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
        if (participant.getNetworkAccessPointId() != null
                && participant.getNetworkAccessPointType() != null) {
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
            e.setObjectQuery(object.getObjectQuery());
        }
        if (object.getObjectDataLifeCycle() != null) {
            e.setObjectDataLifeCycle((short) object.getObjectDataLifeCycle().value());
        }
        List<AtnaObjectDetail> details = object.getObjectDetails();
        for (AtnaObjectDetail detail : details) {
            ObjectDetailEntity ent = new ObjectDetailEntity(detail.getType(), detail.getValue());
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
            o.setObjectQuery(entity.getObjectQuery());
        }
        Set<ObjectDetailEntity> pairs = entity.getDetails();
        for (ObjectDetailEntity pair : pairs) {
            AtnaObjectDetail detail = new AtnaObjectDetail();
            detail.setType(pair.getType());
            detail.setValue(pair.getValue());
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
        List<ObjectDescription> descs = object.getDescriptions();
        for (ObjectDescription desc : descs) {
            ObjectDescriptionEntity ode = new ObjectDescriptionEntity();
            List<String> uids = desc.getMppsUids();
            for (String uid : uids) {
                ode.addMppsUid(uid);
            }
            List<String> accNums = desc.getAccessionNumbers();
            for (String accNum : accNums) {
                ode.addAccessionNumber(accNum);
            }
            List<SopClass> sc = desc.getSopClasses();
            for (SopClass sopClass : sc) {
                SopClassEntity sce = new SopClassEntity();
                sce.setSopId(sopClass.getUid());
                sce.setNumberOfInstances(sopClass.getNumberOfInstances());
                List<String> instances = sopClass.getInstanceUids();
                for (String instance : instances) {
                    sce.addInstanceUid(instance);
                }
                ode.getSopClasses().add(sce);
            }
            e.addObjectDescription(ode);
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
        Set<ObjectDescriptionEntity> descs = entity.getObjectDescriptions();
        for (ObjectDescriptionEntity desc : descs) {
            ObjectDescription od = new ObjectDescription();
            List<String> uids = desc.mppsUidsAsList();
            for (String uid : uids) {
                od.addMppsUid(uid);
            }
            List<String> accNums = desc.accessionNumbersAsList();
            for (String accNum : accNums) {
                od.addAccessionNumber(accNum);
            }
            Set<SopClassEntity> sops = desc.getSopClasses();
            for (SopClassEntity sop : sops) {
                SopClass sc = new SopClass();
                sc.setNumberOfInstances(sop.getNumberOfInstances());
                sc.setUid(sop.getSopId());
                List<String> instances = sop.instanceUidsAsList();
                for (String instance : instances) {
                    sc.addInstanceUid(instance);
                }
                od.addSopClass(sc);
            }
            ao.addObjectDescription(od);
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

        String type = AtnaCode.EVENT_ID;
        if (code instanceof EventTypeCodeEntity) {
            type = AtnaCode.EVENT_TYPE;
        } else if (code instanceof ParticipantCodeEntity) {
            type = AtnaCode.PARTICIPANT_ROLE_TYPE;
        } else if (code instanceof SourceCodeEntity) {
            type = AtnaCode.SOURCE_TYPE;
        } else if (code instanceof ObjectIdTypeCodeEntity) {
            type = AtnaCode.OBJECT_ID_TYPE;
        }
        return new AtnaCode(type, code.getCode(), code.getCodeSystem(), code.getCodeSystemName(), code.getDisplayName(),
                code.getOriginalText());
    }

    public static CodeEntity.CodeType getCodeType(AtnaCode code) {

        String type = code.getCodeType();
        switch (type) {
            case AtnaCode.EVENT_ID:
                return CodeEntity.CodeType.EVENT_ID;
            case AtnaCode.EVENT_TYPE:
                return CodeEntity.CodeType.EVENT_TYPE;
            case AtnaCode.OBJECT_ID_TYPE:
                return CodeEntity.CodeType.PARTICIPANT_OBJECT_ID_TYPE;
            case AtnaCode.PARTICIPANT_ROLE_TYPE:
                return CodeEntity.CodeType.ACTIVE_PARTICIPANT;
            case AtnaCode.SOURCE_TYPE:
                return CodeEntity.CodeType.AUDIT_SOURCE;
            default:
                return null;
        }
    }
}
