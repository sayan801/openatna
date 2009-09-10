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


package org.openhealthtools.openatna.persistence.model;

import org.openhealthtools.openatna.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.persistence.model.codes.EventTypeCodeEntity;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "atna_events")
public class AtnaEventEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private static DateFormat format = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:SS");

    private Long id;

    private EventIdCodeEntity eventId;
    private Set<EventTypeCodeEntity> eventTypeCodes = new HashSet<EventTypeCodeEntity>();
    private String eventActionCode;
    private Date eventDateTime;
    private Integer eventOutcome;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_types_to_codes",
            joinColumns = {@JoinColumn(name = "event_type")},
            inverseJoinColumns = @JoinColumn(name = "code")
    )
    public Set<EventTypeCodeEntity> getEventTypeCodes() {
        return eventTypeCodes;
    }

    public void addEventTypeCode(EventTypeCodeEntity code) {
        getEventTypeCodes().add(code);
    }

    public void setEventTypeCodes(Set<EventTypeCodeEntity> eventTypeCodeEntities) {
        this.eventTypeCodes = eventTypeCodeEntities;
    }

    @ManyToOne
    public EventIdCodeEntity getEventId() {
        return eventId;
    }

    public void setEventId(EventIdCodeEntity eventId) {
        this.eventId = eventId;
    }

    public String getEventActionCode() {
        return eventActionCode;
    }

    public void setEventActionCode(String eventActionCode) {
        this.eventActionCode = eventActionCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Integer getEventOutcome() {
        return eventOutcome;
    }

    public void setEventOutcome(Integer eventOutcome) {
        this.eventOutcome = eventOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtnaEventEntity)) return false;

        AtnaEventEntity that = (AtnaEventEntity) o;

        if (eventActionCode != null ? !eventActionCode.equals(that.eventActionCode) : that.eventActionCode != null) return false;
        if (eventDateTime != null ? !eventDateTime.equals(that.eventDateTime) : that.eventDateTime != null) return false;
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (eventOutcome != null ? !eventOutcome.equals(that.eventOutcome) : that.eventOutcome != null) return false;
        if (eventTypeCodes != null ? !eventTypeCodes.equals(that.eventTypeCodes) : that.eventTypeCodes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (eventTypeCodes != null ? eventTypeCodes.hashCode() : 0);
        result = 31 * result + (eventActionCode != null ? eventActionCode.hashCode() : 0);
        result = 31 * result + (eventDateTime != null ? eventDateTime.hashCode() : 0);
        result = 31 * result + (eventOutcome != null ? eventOutcome.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", event id:")
                .append(getEventId())
                .append(", action=")
                .append(getEventActionCode())
                .append(", outcome=")
                .append(getEventOutcome())
                .append(", time stamp=")
                .append(format.format(getEventDateTime()))
                .append(", event types=")
                .append(getEventTypeCodes())
                .append("]")
                .toString();
    }


}
