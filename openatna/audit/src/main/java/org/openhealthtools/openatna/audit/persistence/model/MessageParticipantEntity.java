package org.openhealthtools.openatna.audit.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    //@GenericGenerator(name = "native", strategy = "native")
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
        return userIsRequestor != null ? userIsRequestor.equals(that.userIsRequestor) : that.userIsRequestor == null;
    }

    @Override
    public int hashCode() {
        int result = getParticipant() != null ? getParticipant().hashCode() : 0;
        result = 31 * result + (userIsRequestor != null ? userIsRequestor.hashCode() : 0);
        result = 31 * result + (getNetworkAccessPoint() != null ? getNetworkAccessPoint().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("participant", participant)
                .append("userIsRequestor", userIsRequestor)
                .append("networkAccessPoint", networkAccessPoint)
                .toString();
    }
}
