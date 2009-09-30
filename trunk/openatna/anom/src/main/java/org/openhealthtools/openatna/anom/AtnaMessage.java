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

package org.openhealthtools.openatna.anom;

import java.io.Serializable;
import java.util.*;

/**
 * Audit message interface
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaMessage implements Serializable {

    private AtnaCode eventCode;
    private Set<AtnaCode> eventTypeCodes = new HashSet<AtnaCode>();
    private EventAction eventActionCode;
    private EventOutcome eventOutcome;
    private Date eventDateTime;

    private Set<AtnaMessageParticipant> participants = new HashSet<AtnaMessageParticipant>();
    private Set<AtnaSource> sources = new HashSet<AtnaSource>();
    private Set<AtnaMessageObject> objects = new HashSet<AtnaMessageObject>();

    public AtnaMessage(AtnaCode eventCode, EventOutcome eventOutcome) {
        this.eventCode = eventCode;
        this.eventOutcome = eventOutcome;
    }

    public List<AtnaCode> getEventTypeCodes() {
        return new ArrayList<AtnaCode>(eventTypeCodes);
    }

    public AtnaMessage addEventTypeCode(AtnaCode value) {
        this.eventTypeCodes.add(value);
        return this;
    }

    public AtnaMessage removeEventTypeCode(AtnaCode value) {
        this.eventTypeCodes.remove(value);
        return this;
    }

    public AtnaCode getEventCode() {
        return eventCode;
    }

    public AtnaMessage setEventCode(AtnaCode eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public EventAction getEventActionCode() {
        return eventActionCode;
    }

    public AtnaMessage setEventActionCode(EventAction eventActionCode) {
        this.eventActionCode = eventActionCode;
        return this;
    }

    public EventOutcome getEventOutcome() {
        return eventOutcome;
    }

    public AtnaMessage setEventOutcome(EventOutcome eventOutcome) {
        this.eventOutcome = eventOutcome;
        return this;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public AtnaMessage setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
        return this;
    }

    public List<AtnaMessageParticipant> getParticipants() {
        return new ArrayList<AtnaMessageParticipant>(participants);
    }

    public AtnaMessage addParticipant(AtnaMessageParticipant participant) {
        this.participants.add(participant);
        return this;
    }

    public AtnaMessage removeParticipant(AtnaMessageParticipant participant) {
        this.participants.remove(participant);
        return this;
    }

    public AtnaMessageParticipant getParticipant(String id) {
        for (AtnaMessageParticipant participant : participants) {
            if (participant.getParticipant().getUserId().equals(id)) {
                return participant;
            }
        }
        return null;
    }

    public List<AtnaSource> getSources() {
        return new ArrayList<AtnaSource>(sources);
    }

    public AtnaMessage addSource(AtnaSource atnaSource) {
        this.sources.add(atnaSource);
        return this;
    }

    public AtnaMessage removeSource(AtnaSource atnaSource) {
        this.sources.remove(atnaSource);
        return this;
    }

    public AtnaSource getSource(String id) {
        for (AtnaSource source : sources) {
            if (source.getSourceId().equals(id)) {
                return source;
            }
        }
        return null;
    }

    public List<AtnaMessageObject> getObjects() {
        return new ArrayList<AtnaMessageObject>(objects);
    }

    public AtnaMessage addObject(AtnaMessageObject object) {
        this.objects.add(object);
        return this;
    }

    public AtnaMessage removeObject(AtnaMessageObject object) {
        this.objects.remove(object);
        return this;
    }

    public AtnaMessageObject getObject(String id) {
        for (AtnaMessageObject object : objects) {
            if (object.getObject().getObjectId().equals(id)) {
                return object;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtnaMessage)) return false;

        AtnaMessage that = (AtnaMessage) o;

        if (eventActionCode != that.eventActionCode) return false;
        if (eventCode != null ? !eventCode.equals(that.eventCode) : that.eventCode != null) return false;
        if (eventDateTime != null ? !eventDateTime.equals(that.eventDateTime) : that.eventDateTime != null) return false;
        if (eventOutcome != that.eventOutcome) return false;
        if (eventTypeCodes != null ? !eventTypeCodes.equals(that.eventTypeCodes) : that.eventTypeCodes != null) return false;
        if (objects != null ? !objects.equals(that.objects) : that.objects != null) return false;
        if (participants != null ? !participants.equals(that.participants) : that.participants != null) return false;
        if (sources != null ? !sources.equals(that.sources) : that.sources != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventCode != null ? eventCode.hashCode() : 0;
        result = 31 * result + (eventTypeCodes != null ? eventTypeCodes.hashCode() : 0);
        result = 31 * result + (eventActionCode != null ? eventActionCode.hashCode() : 0);
        result = 31 * result + (eventOutcome != null ? eventOutcome.hashCode() : 0);
        result = 31 * result + (eventDateTime != null ? eventDateTime.hashCode() : 0);
        result = 31 * result + (participants != null ? participants.hashCode() : 0);
        result = 31 * result + (sources != null ? sources.hashCode() : 0);
        result = 31 * result + (objects != null ? objects.hashCode() : 0);
        return result;
    }
}
