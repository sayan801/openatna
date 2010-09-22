/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */


package org.openhealthtools.openatna.audit.persistence.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "message_participants")
public class MessageParticipantEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;

    private ParticipantEntity participant;
    private Boolean userIsRequestor = Boolean.TRUE;
    private NetworkAccessPointEntity networkAccessPoint;

    public MessageParticipantEntity() {
    }

    public MessageParticipantEntity(ParticipantEntity participant) {
        setParticipant(participant);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
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

    @ManyToOne
    public NetworkAccessPointEntity getNetworkAccessPoint() {
        return networkAccessPoint;
    }

    public void setNetworkAccessPoint(NetworkAccessPointEntity networkAccessPoint) {
        this.networkAccessPoint = networkAccessPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageParticipantEntity)) {
            return false;
        }

        MessageParticipantEntity that = (MessageParticipantEntity) o;

        if (getNetworkAccessPoint() != null ? !getNetworkAccessPoint().equals(that.getNetworkAccessPoint())
                : that.getNetworkAccessPoint() != null) {
            return false;
        }
        if (getParticipant() != null ? !getParticipant().equals(that.getParticipant())
                : that.getParticipant() != null) {
            return false;
        }
        if (userIsRequestor != null ? !userIsRequestor.equals(that.userIsRequestor)
                : that.userIsRequestor != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getParticipant() != null ? getParticipant().hashCode() : 0;
        result = 31 * result + (userIsRequestor != null ? userIsRequestor.hashCode() : 0);
        result = 31 * result + (getNetworkAccessPoint() != null ? getNetworkAccessPoint().hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", network access point=")
                .append(getNetworkAccessPoint())
                .append(", is user requestor=")
                .append(isUserIsRequestor())
                .append(", participant=")
                .append(getParticipant())
                .append("]")
                .toString();
    }
}
