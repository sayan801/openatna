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

@Entity
@Table(name="atna_participants")
public class AtnaParticipantEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;
    
    private Long id;

    private AtnaMessageEntity atnaMessage;
    private ParticipantEntity participant;
    private Boolean userIsRequestor;
    private NetworkAccessPointEntity networkAccessPoint;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "msg_id")
    public AtnaMessageEntity getAtnaMessage() {
        return atnaMessage;
    }

    public void setAtnaMessage(AtnaMessageEntity atnaMessageEntity) {
        this.atnaMessage = atnaMessageEntity;
    }

    public ParticipantEntity getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantEntity participant) {
        this.participant = participant;
    }

    public Boolean isUserIsRequestor() {
        return userIsRequestor;
    }

    public void setUserIsRequestor(Boolean userIsRequestor) {
        this.userIsRequestor = userIsRequestor;
    }

    public NetworkAccessPointEntity getNetworkAccessPoint() {
        return networkAccessPoint;
    }

    public void setNetworkAccessPoint(NetworkAccessPointEntity networkAccessPoint) {
        this.networkAccessPoint = networkAccessPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtnaParticipantEntity)) return false;

        AtnaParticipantEntity that = (AtnaParticipantEntity) o;

        if (atnaMessage != null ? !atnaMessage.equals(that.atnaMessage) : that.atnaMessage != null) return false;
        if (networkAccessPoint != null ? !networkAccessPoint.equals(that.networkAccessPoint) : that.networkAccessPoint != null) return false;
        if (participant != null ? !participant.equals(that.participant) : that.participant != null) return false;
        if (userIsRequestor != null ? !userIsRequestor.equals(that.userIsRequestor) : that.userIsRequestor != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = atnaMessage != null ? atnaMessage.hashCode() : 0;
        result = 31 * result + (participant != null ? participant.hashCode() : 0);
        result = 31 * result + (userIsRequestor != null ? userIsRequestor.hashCode() : 0);
        result = 31 * result + (networkAccessPoint != null ? networkAccessPoint.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "null";
    }
}
