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

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "atna_messages")
public class AtnaMessageEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;

    private AtnaEventEntity event;

    private Set<AtnaParticipantEntity> atnaParticipants = new HashSet<AtnaParticipantEntity>();

    private Set<AtnaSourceEntity> atnaSources = new HashSet<AtnaSourceEntity>();

    private Set<AtnaObjectEntity> atnaObjects = new HashSet<AtnaObjectEntity>();

    public AtnaMessageEntity() {
    }

    public AtnaMessageEntity(AtnaEventEntity event) {
        this.event = event;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public AtnaEventEntity getEvent() {
        return event;
    }

    public void setEvent(AtnaEventEntity event) {
        this.event = event;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<AtnaParticipantEntity> getAtnaParticipants() {
        return atnaParticipants;
    }

    public void setAtnaParticipants(Set<AtnaParticipantEntity> atnaParticipants) {
        this.atnaParticipants = atnaParticipants;
    }

    public void addAtnaParticipant(AtnaParticipantEntity entity) {
        getAtnaParticipants().add(entity);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<AtnaSourceEntity> getAtnaSources() {
        return atnaSources;
    }

    public void setAtnaSources(Set<AtnaSourceEntity> atnaSources) {
        this.atnaSources = atnaSources;
    }

    public void addAtnaSource(AtnaSourceEntity entity) {
        getAtnaSources().add(entity);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<AtnaObjectEntity> getAtnaObjects() {
        return atnaObjects;
    }

    public void setAtnaObjects(Set<AtnaObjectEntity> atnaObjects) {
        this.atnaObjects = atnaObjects;
    }

    public void addAtnaObject(AtnaObjectEntity entity) {
        getAtnaObjects().add(entity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtnaMessageEntity)) return false;

        AtnaMessageEntity that = (AtnaMessageEntity) o;

        if (atnaObjects != null ? !atnaObjects.equals(that.atnaObjects) : that.atnaObjects != null) return false;
        if (atnaParticipants != null ? !atnaParticipants.equals(that.atnaParticipants) : that.atnaParticipants != null) return false;
        if (atnaSources != null ? !atnaSources.equals(that.atnaSources) : that.atnaSources != null) return false;
        if (event != null ? !event.equals(that.event) : that.event != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = event != null ? event.hashCode() : 0;
        result = 31 * result + (atnaParticipants != null ? atnaParticipants.hashCode() : 0);
        result = 31 * result + (atnaSources != null ? atnaSources.hashCode() : 0);
        result = 31 * result + (atnaObjects != null ? atnaObjects.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", event id:")
                .append(getEvent())
                .append(", audit sources=")
                .append(getAtnaSources())
                .append(", active participants=")
                .append(getAtnaParticipants())
                .append(", participant objects=")
                .append(getAtnaObjects())
                .append("]")
                .toString();
    }


}
