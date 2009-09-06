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

import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaEvent;
import org.openhealthtools.openatna.anom.EventAction;
import org.openhealthtools.openatna.anom.EventOutcome;
import org.openhealthtools.openatna.anom.jaxb21.schema.CodedValueType;
import org.openhealthtools.openatna.anom.jaxb21.schema.EventIdentificationType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:15:49 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAtnaEvent implements AtnaEvent {

    private EventIdentificationType evt;

    public JaxbAtnaEvent(EventIdentificationType evt) {
        this.evt = evt;
    }

    public JaxbAtnaEvent(JaxbAtnaCode code, EventOutcome outcome) {
        evt = new EventIdentificationType();
        evt.setEventID(code.getCodedValueType());
        evt.setEventOutcome(outcome.value());
    }

    public AtnaCode getEventCode() {
        return new JaxbAtnaCode(evt.getEventID());
    }

    public List<AtnaCode> getEventTypeCodes() {
        List<AtnaCode> codes = new ArrayList<AtnaCode>();
        List<CodedValueType> l = evt.getEventTypeCode();
        for (CodedValueType codedValueType : l) {
            codes.add(new JaxbAtnaCode(codedValueType));
        }
        return codes;
    }

    public AtnaEvent addEventTypeCode(AtnaCode value) {
        if (value instanceof JaxbAtnaCode) {
            JaxbAtnaCode code = (JaxbAtnaCode) value;
            evt.getEventTypeCode().add(code.getCodedValueType());
        }
        return this;
    }

    public AtnaEvent removeEventTypeCode(AtnaCode value) {
        if (value instanceof JaxbAtnaCode) {
            JaxbAtnaCode code = (JaxbAtnaCode) value;
            evt.getEventTypeCode().remove(code.getCodedValueType());
        }
        return this;
    }

    public EventAction getEventActionCode() {
        return EventAction.getAction(evt.getEventActionCode());
    }

    public AtnaEvent setEventActionCode(EventAction value) {
        evt.setEventActionCode(value.value());
        return this;
    }

    public Date getEventDateTime() {
        return evt.getEventTime();
    }

    public AtnaEvent setEventDateTime(Date value) {
        evt.setEventTime(value);
        return this;
    }

    public EventOutcome getEventOutcome() {
        return EventOutcome.getOutcome(evt.getEventOutcome());
    }

    public AtnaEvent setEventOutcome(EventOutcome value) {
        evt.setEventOutcome(value.value());
        return this;
    }

    public EventIdentificationType getEvt() {
        return evt;
    }
}
