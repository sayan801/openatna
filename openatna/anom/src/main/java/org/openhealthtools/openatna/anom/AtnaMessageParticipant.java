package org.openhealthtools.openatna.anom;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * This class wraps an AtnaParticipant and provides message (Event) specifics such as whether the user is a requestor
 * and the network access point being used by the participant.
 */
public class AtnaMessageParticipant implements Serializable {

    private static final long serialVersionUID = -4687928894634995258L;

    private AtnaParticipant participant;
    private boolean userIsRequestor = true;
    private String networkAccessPointId;
    private NetworkAccessPoint networkAccessPointType;

    public AtnaMessageParticipant(AtnaParticipant participant) {
        this.participant = participant;
    }

    public AtnaParticipant getParticipant() {
        return participant;
    }

    public AtnaMessageParticipant setParticipant(AtnaParticipant participant) {
        this.participant = participant;
        return this;
    }

    public boolean isUserIsRequestor() {
        return userIsRequestor;
    }

    public AtnaMessageParticipant setUserIsRequestor(boolean userIsRequestor) {
        this.userIsRequestor = userIsRequestor;
        return this;
    }

    public String getNetworkAccessPointId() {
        return networkAccessPointId;
    }

    public AtnaMessageParticipant setNetworkAccessPointId(String networkAccessPointId) {
        this.networkAccessPointId = networkAccessPointId;
        return this;
    }

    public NetworkAccessPoint getNetworkAccessPointType() {
        return networkAccessPointType;
    }

    public AtnaMessageParticipant setNetworkAccessPointType(NetworkAccessPoint networkAccessPointType) {
        this.networkAccessPointType = networkAccessPointType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaMessageParticipant)) {
            return false;
        }

        AtnaMessageParticipant that = (AtnaMessageParticipant) o;

        if (userIsRequestor != that.userIsRequestor) {
            return false;
        }
        if (networkAccessPointId != null ? !networkAccessPointId.equals(that.networkAccessPointId) : that.networkAccessPointId != null) {
            return false;
        }
        if (networkAccessPointType != that.networkAccessPointType) {
            return false;
        }
        return participant != null ? participant.equals(that.participant) : that.participant == null;
    }

    @Override
    public int hashCode() {
        int result = participant != null ? participant.hashCode() : 0;
        result = 31 * result + (userIsRequestor ? 1 : 0);
        result = 31 * result + (networkAccessPointId != null ? networkAccessPointId.hashCode() : 0);
        result = 31 * result + (networkAccessPointType != null ? networkAccessPointType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("participant", participant)
                .append("userIsRequestor", userIsRequestor)
                .append("networkAccessPointId", networkAccessPointId)
                .append("networkAccessPointType", networkAccessPointType)
                .toString();
    }
}
