package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Does some basic validation on message contents
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class ValidationProcessor implements AtnaProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationProcessor.class);

    private static ObjectTypeCodeRole[] persons = {
            ObjectTypeCodeRole.CUSTOMER,
            ObjectTypeCodeRole.PATIENT,
            ObjectTypeCodeRole.DOCTOR,
            ObjectTypeCodeRole.RESOURCE,
            ObjectTypeCodeRole.USER,
            ObjectTypeCodeRole.GUARANTOR,
            ObjectTypeCodeRole.SECURITY_USER_ENTITY,
            ObjectTypeCodeRole.PROVIDER
    };

    private static ObjectTypeCodeRole[] systemObjects = {
            ObjectTypeCodeRole.REPORT,
            ObjectTypeCodeRole.MASTER_FILE,
            ObjectTypeCodeRole.USER,
            ObjectTypeCodeRole.LIST,
            ObjectTypeCodeRole.SECURITY_USER_ENTITY,
            ObjectTypeCodeRole.SECURITY_GRANULARITY_DEFINITION,
            ObjectTypeCodeRole.SECURITY_RESOURCE,
            ObjectTypeCodeRole.SECURITY_USER_GROUP,
            ObjectTypeCodeRole.DATA_DESTINATION,
            ObjectTypeCodeRole.DATA_REPOSITORY,
            ObjectTypeCodeRole.SCHEDULE,
            ObjectTypeCodeRole.JOB,
            ObjectTypeCodeRole.JOB_STREAM,
            ObjectTypeCodeRole.TABLE,
            ObjectTypeCodeRole.ROUTING_CRITERIA,
            ObjectTypeCodeRole.QUERY
    };

    private static ObjectTypeCodeRole[] organisations = {
            ObjectTypeCodeRole.LOCATION,
            ObjectTypeCodeRole.RESOURCE,
            ObjectTypeCodeRole.SUBSCRIBER,
            ObjectTypeCodeRole.GUARANTOR,
            ObjectTypeCodeRole.PROVIDER,
            ObjectTypeCodeRole.CUSTOMER,
    };

    private static String[] personIds = {"1", "2", "3", "4", "5", "6", "7", "11"};

    private static String[] systemObjectIds = {"8", "9", "10", "11", "12"};

    private static String[] organisationIds = {"6", "7"};

    public void process(ProcessContext context) throws Exception {

        validate(context);
        context.setState(ProcessContext.State.VALIDATED);
    }

    public void error(ProcessContext context) {
        // Processing Error messages not implemented
    }


    protected void validate(ProcessContext context) throws AtnaException {

        AtnaMessage message = context.getMessage();
        if (message == null) {
            throw new AtnaException("Null message", AtnaException.AtnaError.NO_MESSAGE);
        }
        AtnaCode evt = message.getEventCode();
        if (evt == null || evt.getCode() == null) {
            throw new AtnaException("Invalid event code", AtnaException.AtnaError.NO_EVENT_CODE);
        }
        if (message.getEventOutcome() == null) {
            throw new AtnaException("Invalid event outcome", AtnaException.AtnaError.NO_EVENT_OUTCOME);
        }
        if (message.getEventDateTime() == null) {
            throw new AtnaException("Invalid time stamp", AtnaException.AtnaError.INVALID_EVENT_TIMESTAMP);
        }
        List<AtnaCode> codes = message.getEventTypeCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("No code defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
        List<AtnaSource> sources = message.getSources();
        if (sources.isEmpty()) {
            throw new AtnaException("No audit source defined", AtnaException.AtnaError.NO_AUDIT_SOURCE);
        }
        for (AtnaSource source : sources) {
            validateSource(source, context.getPolicies());
        }
        List<AtnaMessageParticipant> participants = message.getParticipants();
        if (participants.isEmpty()) {
            throw new AtnaException("No participants defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT);
        }
        for (AtnaMessageParticipant participant : participants) {
            validateParticipant(participant, context.getPolicies());
        }
        List<AtnaMessageObject> objects = message.getObjects();
        for (AtnaMessageObject object : objects) {
            validateObject(object, context.getPolicies());
        }
    }

    private void validateParticipant(AtnaMessageParticipant participant, PersistencePolicies policies) throws AtnaException {

        if (participant.getParticipant() == null) {
            throw new AtnaException("No active participant defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT);
        }
        if (participant.getParticipant().getUserId() == null) {
            throw new AtnaException("No active participant user id defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT_ID);
        }
        List<AtnaCode> codes = participant.getParticipant().getRoleIDCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("No code defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
    }

    private void validateSource(AtnaSource source, PersistencePolicies policies) throws AtnaException {

        if (source.getSourceId() == null) {
            throw new AtnaException("No audit source id defined", AtnaException.AtnaError.NO_AUDIT_SOURCE_ID);
        }
        List<AtnaCode> codes = source.getSourceTypeCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("No code defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
    }

    private void validateObject(AtnaMessageObject object, PersistencePolicies policies) throws AtnaException {

        if (object.getObject() == null) {
            throw new AtnaException("No participant object defined", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT);
        }
        AtnaObject obj = object.getObject();
        if (obj.getObjectId() == null) {
            LOGGER.error("ATNA Error: ATNAObject does not contain ID - TOTO: Review implementation");
            //TODO: Review this Error management
            //throw new AtnaException("no participant object id defined",AtnaException.AtnaError.NO_PARTICIPANT_OBJECT_ID);
        }
        if (obj.getObjectIdTypeCode() == null || obj.getObjectIdTypeCode().getCode() == null) {
            throw new AtnaException("No object id type code", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT_ID_TYPE_CODE);
        }
        if (obj.getObjectTypeCode() != null) {
            validateObjectIdTypeCode(obj.getObjectIdTypeCode(), obj.getObjectTypeCode());
            if (obj.getObjectTypeCodeRole() != null) {
                validateObjectTypeCodeRole(obj.getObjectTypeCodeRole(), obj.getObjectTypeCode());
            }
        }

        List<AtnaObjectDetail> details = object.getObjectDetails();
        for (AtnaObjectDetail detail : details) {

            if (detail.getType() == null || detail.getValue() == null || detail.getValue().length == 0) {
                throw new AtnaException("Invalid object detail", AtnaException.AtnaError.INVALID_OBJECT_DETAIL);
            }
        }
    }

    private boolean isInArray(ObjectTypeCodeRole role, ObjectTypeCodeRole[] arr) {

        for (ObjectTypeCodeRole codeRole : arr) {
            if (codeRole == role) {
                return true;
            }
        }
        return false;
    }

    private boolean isInArray(String role, String[] arr) {

        for (String codeRole : arr) {
            if (codeRole.equals(role)) {
                return true;
            }
        }
        return false;
    }


    private void validateObjectTypeCodeRole(ObjectTypeCodeRole role, ObjectType type) throws AtnaException {

        switch (type) {
            case PERSON:
                if (!isInArray(role, persons)) {
                    throw new AtnaException("Invalid combination of role and type. Role:" + role + " type:" + type);
                }
                break;
            case ORGANIZATION:
                if (!isInArray(role, organisations)) {
                    throw new AtnaException("Invalid combination of role and type. Role:" + role + " type:" + type);
                }
                break;
            case SYSTEM_OBJECT:
                if (!isInArray(role, systemObjects)) {
                    //TODO: We had to remove this control, since epSOS specs need role: resource and type: system object together
                    LOGGER.warn("Invalid combination of role and type. Role: '{}' type: '{}'. But we had to skip to be compliant with the specifications.", role, type);
                    //throw new AtnaException("Invalid combination of role and type. Role:" + role + " type:" + type);
                }
                break;
            case OTHER:

                break;
            default:
                throw new AtnaException("Unknown Object type.");
        }

    }

    private void validateObjectIdTypeCode(AtnaCode code, ObjectType type) throws AtnaException {

        if (code.getCodeType().equals(AtnaCode.OBJECT_ID_TYPE) && code.getCodeSystemName() != null
                && code.getCodeSystemName().equalsIgnoreCase("RFC-3881")) {

            String s = code.getCode();
            switch (type) {
                case PERSON:
                    if (!isInArray(s, personIds)) {
                        throw new AtnaException("Invalid combination of id type and Object type. Code:"
                                + s + " type:" + type);
                    }
                    break;
                case ORGANIZATION:
                    if (!isInArray(s, organisationIds)) {
                        throw new AtnaException("Invalid combination of id type and Object type. Code:"
                                + s + " type:" + type);
                    }
                    break;
                case SYSTEM_OBJECT:
                    if (!isInArray(s, systemObjectIds)) {
                        throw new AtnaException("Invalid combination of id type and Object type. Code:"
                                + s + " type:" + type);
                    }
                    break;
                case OTHER:

                    break;
                default:
                    throw new AtnaException("Unknown Object type.");
            }
        }
    }
}
