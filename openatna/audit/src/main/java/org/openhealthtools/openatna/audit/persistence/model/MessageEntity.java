package org.openhealthtools.openatna.audit.persistence.model;

import org.openhealthtools.openatna.audit.persistence.model.codes.EventIdCodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.EventTypeCodeEntity;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "messages")
public class MessageEntity extends PersistentEntity {

    private static final long serialVersionUID = -4989747917448824812L;
    private Long id;
    private EventIdCodeEntity eventId;
    private String eventActionCode;
    private Date eventDateTime;
    private Integer eventOutcome;
    private String sourceAddress;
    private byte[] messageContent = new byte[0];
    private Set<EventTypeCodeEntity> eventTypeCodes = new HashSet<>();
    private Set<MessageParticipantEntity> messageParticipants = new HashSet<>();
    private Set<MessageObjectEntity> messageObjects = new HashSet<>();
    private Set<MessageSourceEntity> messageSources = new HashSet<>();

    public MessageEntity() {
    }

    public MessageEntity(EventIdCodeEntity code, Integer eventOutcome) {
        this.eventId = code;
        this.eventOutcome = eventOutcome;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_types_codes")
    public Set<EventTypeCodeEntity> getEventTypeCodes() {
        return eventTypeCodes;
    }

    public void setEventTypeCodes(Set<EventTypeCodeEntity> eventTypeCodeEntities) {
        this.eventTypeCodes = eventTypeCodeEntities;
    }

    public void addEventTypeCode(EventTypeCodeEntity code) {
        getEventTypeCodes().add(code);
    }

    @ManyToOne(fetch = FetchType.EAGER)
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

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "messages_mparticipants")
    public Set<MessageParticipantEntity> getMessageParticipants() {
        return messageParticipants;
    }

    public void setMessageParticipants(Set<MessageParticipantEntity> messageParticipants) {
        this.messageParticipants = messageParticipants;
    }

    public void addMessageParticipant(MessageParticipantEntity entity) {
        getMessageParticipants().add(entity);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "messages_msources")
    public Set<MessageSourceEntity> getMessageSources() {
        return messageSources;
    }

    public void setMessageSources(Set<MessageSourceEntity> messageSources) {
        this.messageSources = messageSources;
    }

    public void addMessageSource(MessageSourceEntity entity) {
        getMessageSources().add(entity);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "messages_mobjects")
    public Set<MessageObjectEntity> getMessageObjects() {
        return messageObjects;
    }

    public void setMessageObjects(Set<MessageObjectEntity> messageObjects) {
        this.messageObjects = messageObjects;
    }

    public void addMessageObject(MessageObjectEntity entity) {
        getMessageObjects().add(entity);
    }

    @Lob
    public byte[] getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(byte[] messageContent) {
        this.messageContent = messageContent;
    }

    private String formatDate() {
        DateFormat format = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:SS");
        return format.format(getEventDateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageEntity)) {
            return false;
        }

        MessageEntity that = (MessageEntity) o;
        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!Objects.equals(eventActionCode, that.eventActionCode)) {
            return false;
        }
        if (!Objects.equals(eventDateTime, that.eventDateTime)) {
            return false;
        }
        if (!Objects.equals(eventId, that.eventId)) {
            return false;
        }
        if (!Objects.equals(eventOutcome, that.eventOutcome)) {
            return false;
        }
        if (getEventTypeCodes() != null ? !getEventTypeCodes().equals(that.getEventTypeCodes())
                : that.getEventTypeCodes() != null) {
            return false;
        }
        if (getMessageObjects() != null ? !getMessageObjects().equals(that.getMessageObjects())
                : that.getMessageObjects() != null) {
            return false;
        }
        if (getMessageParticipants() != null ? !getMessageParticipants().equals(that.getMessageParticipants())
                : that.getMessageParticipants() != null) {
            return false;
        }
        return getMessageSources() != null ? getMessageSources().equals(that.getMessageSources()) : that.getMessageSources() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessageParticipants() != null ? getMessageParticipants().hashCode() : 0;
        result = 31 * result + (getMessageSources() != null ? getMessageSources().hashCode() : 0);
        result = 31 * result + (getMessageObjects() != null ? getMessageObjects().hashCode() : 0);
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (getEventTypeCodes() != null ? getEventTypeCodes().hashCode() : 0);
        result = 31 * result + (eventActionCode != null ? eventActionCode.hashCode() : 0);
        result = 31 * result + (eventDateTime != null ? eventDateTime.hashCode() : 0);
        result = 31 * result + (eventOutcome != null ? eventOutcome.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "[" + getClass().getName() +
                " id=" +
                getId() +
                ", event id:" +
                getEventId() +
                ", action=" +
                getEventActionCode() +
                ", outcome=" +
                getEventOutcome() +
                ", time stamp=" +
                formatDate() +
                ", event types=" +
                getEventTypeCodes() +
                ", audit sources=" +
                getMessageSources() +
                ", active participants=" +
                getMessageParticipants() +
                ", participant objects=" +
                getMessageObjects() +
                "]";
    }
}
