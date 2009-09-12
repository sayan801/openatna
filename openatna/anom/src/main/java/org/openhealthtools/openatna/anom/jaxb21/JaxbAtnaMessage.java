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

package org.openhealthtools.openatna.anom.jaxb21;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.anom.jaxb21.schema.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:15:10 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAtnaMessage implements AtnaMessage {

    private AuditMessage msg;

    public JaxbAtnaMessage(AuditMessage msg) {
        if (msg.getEventIdentification() == null) {
            throw new IllegalArgumentException("message has no Event Identification");
        }
        this.msg = msg;
    }

    public JaxbAtnaMessage(JaxbAtnaCode code, EventOutcome outcome) {
        EventIdentificationType evt = new EventIdentificationType();
        evt.setEventID(code.getCodedValueType());
        evt.setEventOutcome(outcome.value());
        msg = new AuditMessage();
        msg.setEventIdentification(evt);
    }


    public AtnaCode getEventCode() {
        return new JaxbAtnaCode(msg.getEventIdentification().getEventID());
    }

    public List<AtnaCode> getEventTypeCodes() {
        List<AtnaCode> codes = new ArrayList<AtnaCode>();
        List<CodedValueType> l = msg.getEventIdentification().getEventTypeCode();
        for (CodedValueType codedValueType : l) {
            codes.add(new JaxbAtnaCode(codedValueType));
        }
        return codes;
    }

    public AtnaMessage addEventTypeCode(AtnaCode value) {
        if (value instanceof JaxbAtnaCode) {
            JaxbAtnaCode code = (JaxbAtnaCode) value;
            msg.getEventIdentification().getEventTypeCode().add(code.getCodedValueType());
        }
        return this;
    }

    public AtnaMessage removeEventTypeCode(AtnaCode value) {
        if (value instanceof JaxbAtnaCode) {
            JaxbAtnaCode code = (JaxbAtnaCode) value;
            msg.getEventIdentification().getEventTypeCode().remove(code.getCodedValueType());
        }
        return this;
    }

    public EventAction getEventActionCode() {
        return EventAction.getAction(msg.getEventIdentification().getEventActionCode());
    }

    public AtnaMessage setEventActionCode(EventAction value) {
        msg.getEventIdentification().setEventActionCode(value.value());
        return this;
    }

    public Date getEventDateTime() {
        return msg.getEventIdentification().getEventTime();
    }

    public AtnaMessage setEventDateTime(Date value) {
        msg.getEventIdentification().setEventTime(value);
        return this;
    }

    public EventOutcome getEventOutcome() {
        return EventOutcome.getOutcome(msg.getEventIdentification().getEventOutcome());
    }

    public AtnaMessage setEventOutcome(EventOutcome value) {
        msg.getEventIdentification().setEventOutcome(value.value());
        return this;
    }

    public List<AtnaMessageParticipant> getParticipants() {
        List<ActiveParticipantType> l = msg.getActiveParticipant();
        List<AtnaMessageParticipant> ret = new ArrayList<AtnaMessageParticipant>();
        for (ActiveParticipantType a : l) {
            ret.add(new JaxbAtnaParticipant(a));
        }
        return ret;
    }

    public AtnaMessage addParticipant(AtnaMessageParticipant participant) {
        if (participant instanceof JaxbAtnaParticipant) {
            JaxbAtnaParticipant jp = (JaxbAtnaParticipant) participant;
            msg.getActiveParticipant().add(jp.getActiveParticipant());
        }
        return this;
    }

    public AtnaMessage removeParticipant(AtnaMessageParticipant participant) {
        if (participant instanceof JaxbAtnaParticipant) {
            JaxbAtnaParticipant jp = (JaxbAtnaParticipant) participant;
            msg.getActiveParticipant().remove(jp.getActiveParticipant());
        }
        return this;
    }

    public AtnaMessageParticipant getParticipant(String id) {
        List<ActiveParticipantType> l = msg.getActiveParticipant();
        for (ActiveParticipantType type : l) {
            if (type.getUserID().equals(id)) {
                return new JaxbAtnaParticipant(type);
            }
        }
        return null;
    }

    public List<AtnaSource> getSources() {
        List<AuditSourceIdentificationType> l = msg.getAuditSourceIdentification();
        List<AtnaSource> ret = new ArrayList<AtnaSource>();
        for (AuditSourceIdentificationType a : l) {
            ret.add(new JaxbAtnaSource(a));
        }
        return ret;
    }

    public AtnaMessage addSource(AtnaSource atnaSource) {
        if (atnaSource instanceof JaxbAtnaSource) {
            JaxbAtnaSource js = (JaxbAtnaSource) atnaSource;
            msg.getAuditSourceIdentification().add(js.getSource());
        }
        return this;
    }

    public AtnaMessage removeSource(AtnaSource atnaSource) {
        if (atnaSource instanceof JaxbAtnaSource) {
            JaxbAtnaSource js = (JaxbAtnaSource) atnaSource;
            msg.getAuditSourceIdentification().remove(js.getSource());
        }
        return this;
    }

    public AtnaSource getSource(String id) {
        List<AuditSourceIdentificationType> l = msg.getAuditSourceIdentification();
        for (AuditSourceIdentificationType type : l) {
            if (type.getAuditSourceID().equals(id)) {
                return new JaxbAtnaSource(type);
            }
        }
        return null;
    }

    public List<AtnaMessageObject> getObjects() {
        List<ParticipantObjectIdentificationType> l = msg.getParticipantObjectIdentification();
        List<AtnaMessageObject> ret = new ArrayList<AtnaMessageObject>();
        for (ParticipantObjectIdentificationType a : l) {
            ret.add(new JaxbAtnaObject(a));
        }
        return ret;
    }

    public AtnaMessage addObject(AtnaMessageObject object) {
        if (object instanceof JaxbAtnaObject) {
            JaxbAtnaObject jo = (JaxbAtnaObject) object;
            msg.getParticipantObjectIdentification().add(jo.getParticipantObject());
        }
        return this;
    }

    public AtnaMessage removeObject(AtnaMessageObject object) {
        if (object instanceof JaxbAtnaObject) {
            JaxbAtnaObject jo = (JaxbAtnaObject) object;
            msg.getParticipantObjectIdentification().remove(jo.getParticipantObject());
        }
        return this;
    }

    public AtnaMessageObject getObject(String id) {
        List<ParticipantObjectIdentificationType> l = msg.getParticipantObjectIdentification();
        for (ParticipantObjectIdentificationType type : l) {
            if (type.getParticipantObjectID().equals(id)) {
                return new JaxbAtnaObject(type);
            }
        }
        return null;
    }

    public AuditMessage getMessage() {
        return msg;
    }
}
