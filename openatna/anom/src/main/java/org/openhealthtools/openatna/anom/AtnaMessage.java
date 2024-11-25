package org.openhealthtools.openatna.anom;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Audit message interface
 *
 * @author Andrew Harrison
 */
public class AtnaMessage implements Serializable {

    private static final long serialVersionUID = -5502378798460439820L;

    private Long messageId;
    private AtnaCode eventCode;
    private Set<AtnaCode> eventTypeCodes = new HashSet<>();
    private EventAction eventActionCode;
    private EventOutcome eventOutcome;
    private Date eventDateTime;
    private String sourceAddress;
    private Set<AtnaMessageParticipant> participants = new HashSet<>();
    private Set<AtnaSource> sources = new HashSet<>();
    private Set<AtnaMessageObject> objects = new HashSet<>();
    private byte[] messageContent;

    public AtnaMessage(AtnaCode eventCode, EventOutcome eventOutcome) {
        this.eventCode = eventCode;
        this.eventOutcome = eventOutcome;
    }

    /**
     * Returns unique ID for the message. This is assigned once a message has been successfully persisted.
     *
     * @return Atna message unique ID.
     */
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public List<AtnaCode> getEventTypeCodes() {
        return new ArrayList<>(eventTypeCodes);
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
        return new ArrayList<>(participants);
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
        return new ArrayList<>(sources);
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
        return new ArrayList<>(objects);
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

    public byte[] getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(byte[] messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaMessage)) {
            return false;
        }

        AtnaMessage that = (AtnaMessage) o;
        if (!Objects.equals(eventActionCode, that.eventActionCode)) {
            return false;
        }
        if (!Objects.equals(eventCode, that.eventCode)) {
            return false;
        }
        if (!Objects.equals(eventDateTime, that.eventDateTime)) {
            return false;
        }
        if (eventOutcome != that.eventOutcome) {
            return false;
        }
        if (!Objects.equals(eventTypeCodes, that.eventTypeCodes)) {
            return false;
        }
        if (!Objects.equals(objects, that.objects)) {
            return false;
        }
        if (!Objects.equals(participants, that.participants)) {
            return false;
        }
        return Objects.equals(sources, that.sources);
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

    @Override
    public String toString() {
        return "[" +
                getClass().getName() +
                " event code=" +
                getEventCode() +
                " event action=" +
                getEventActionCode() +
                " event outcome=" +
                getEventOutcome() +
                " timestamp=" +
                getEventDateTime() +
                " event type codes=" +
                getEventTypeCodes() +
                " sources=" +
                getSources() +
                " participants=" +
                getParticipants() +
                " objects=" +
                getObjects() +
                "]";
    }
}
