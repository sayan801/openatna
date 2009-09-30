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

package org.openhealthtools.openatna.audit.process;

import java.util.List;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.audit.AuditException;

/**
 * Does some basic validation on message contents.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 8:38:08 PM
 * @date $Date:$ modified by $Author:$
 */

public class ValidationProcessor implements AtnaProcessor {


    public void process(ProcessContext context) throws AuditException {
        try {
            validate(context.getMessage());
            context.setState(ProcessContext.State.VALIDATED);
        } catch (AtnaException e) {
            throw new AuditException(e, context.getMessage(), AuditException.AuditError.INVALID_MESSAGE);
        }
    }


    protected void validate(AtnaMessage message) throws AtnaException {
        if (message == null) {
            throw new AtnaException("null message", AtnaException.AtnaError.NO_MESSAGE);
        }
        AtnaCode evt = message.getEventCode();
        if (evt == null || evt.getCode() == null) {
            throw new AtnaException("invalid event code", AtnaException.AtnaError.NO_EVENT_CODE);
        }
        if (message.getEventOutcome() == null) {
            throw new AtnaException("invalid event outcome", AtnaException.AtnaError.NO_EVENT_OUTCOME);
        }
        if (message.getEventDateTime() == null) {
            throw new AtnaException("invalid time stamp", AtnaException.AtnaError.INVALID_EVENT_TIMESTAMP);
        }
        List<AtnaCode> codes = message.getEventTypeCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("no code defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
        List<AtnaSource> sources = message.getSources();
        if (sources.size() == 0) {
            throw new AtnaException("no audit source defined", AtnaException.AtnaError.NO_AUDIT_SOURCE);
        }
        for (AtnaSource source : sources) {
            validateSource(source);
        }
        List<AtnaMessageParticipant> participants = message.getParticipants();
        if (participants.size() == 0) {
            throw new AtnaException("no participants defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT);
        }
        for (AtnaMessageParticipant participant : participants) {
            validateParticipant(participant);
        }
        List<AtnaMessageObject> objects = message.getObjects();
        for (AtnaMessageObject object : objects) {
            validateObject(object);
        }
    }

    private void validateParticipant(AtnaMessageParticipant participant) throws AtnaException {
        if (participant.getParticipant() == null) {
            throw new AtnaException("no active participant defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT);
        }
        if (participant.getParticipant().getUserId() == null) {
            throw new AtnaException("no active participant user id defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT_ID);
        }
        List<AtnaCode> codes = participant.getParticipant().getRoleIDCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("no code defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
        NetworkAccessPoint nap = participant.getNetworkAccessPointType();
        String napId = participant.getNetworkAccessPointId();
        if (nap != null && napId == null) {
            throw new AtnaException("no network access point id defined", AtnaException.AtnaError.NO_NETWORK_ACCESS_POINT_ID);
        }
        if (nap == null && napId != null) {
            throw new AtnaException("no network access point type defined", AtnaException.AtnaError.NO_NETWORK_ACCESS_POINT_TYPE);
        }
    }

    private void validateSource(AtnaSource source) throws AtnaException {
        if (source.getSourceId() == null) {
            throw new AtnaException("no audit source id defined", AtnaException.AtnaError.NO_AUDIT_SOURCE_ID);
        }
        List<AtnaCode> codes = source.getSourceTypeCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("no code defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
    }

    private void validateObject(AtnaMessageObject object) throws AtnaException {
        if (object.getObject() == null) {
            throw new AtnaException("no participant object defined", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT);
        }
        AtnaObject obj = object.getObject();
        if (obj.getObjectId() == null) {
            throw new AtnaException("no participant object id defined", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT_ID);
        }
        if (obj.getObjectIdTypeCode() == null || obj.getObjectIdTypeCode().getCode() == null) {
            throw new AtnaException("invalid object id type code", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT_ID_TYPE_CODE);
        }
        List<AtnaObjectDetail> details = object.getObjectDetails();
        for (AtnaObjectDetail detail : details) {
            if (detail.getType() == null || detail.getValue() == null || detail.getValue().length == 0) {
                throw new AtnaException("invalid object id type code", AtnaException.AtnaError.INVALID_OBJECT_DETAIL);
            }
        }
    }
}
