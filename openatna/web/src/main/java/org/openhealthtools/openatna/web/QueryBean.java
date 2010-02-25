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

package org.openhealthtools.openatna.web;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 19, 2010: 11:48:17 AM
 */

public class QueryBean {

    private String sourceId;
    private String objectId;
    private String participantId;
    private String eventOutcome;
    private String eventIdCode;
    private String eventTypeCode;
    private String eventAction;
    private String eventTime;
    private String startDate;
    private String startHour;
    private String startMin;

    private String participantTypeCode;
    private String sourceTypeCode;
    private String objectTypeCode;

    private String endDate;
    private String endHour;
    private String endMin;
    private String sourceAddress;

    private int maxResults = 30;
    private int startOffset = 0;

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMin() {
        return startMin;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMin() {
        return endMin;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getEventOutcome() {
        return eventOutcome;
    }

    public void setEventOutcome(String eventOutcome) {
        this.eventOutcome = eventOutcome;
    }

    public String getEventIdCode() {
        return eventIdCode;
    }

    public void setEventIdCode(String eventIdCode) {
        this.eventIdCode = eventIdCode;
    }

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public String getParticipantTypeCode() {
        return participantTypeCode;
    }

    public void setParticipantTypeCode(String participantTypeCode) {
        this.participantTypeCode = participantTypeCode;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public QueryBean copy() {
        QueryBean other = new QueryBean();
        other.setEndDate(getEndDate());
        other.setEndHour(getEndHour());
        other.setEndMin(getEndMin());
        other.setStartDate(getStartDate());
        other.setStartHour(getStartHour());
        other.setStartMin(getStartMin());
        other.setStartOffset(getStartOffset());
        other.setSourceAddress(getSourceAddress());
        other.setEventAction(getEventAction());
        other.setEventIdCode(getEventIdCode());
        other.setEventTypeCode(getEventTypeCode());
        other.setEventOutcome(getEventOutcome());
        other.setEventTime(getEventTime());
        other.setMaxResults(getMaxResults());
        other.setObjectId(getObjectId());
        other.setParticipantId(getParticipantId());
        other.setSourceId(getSourceId());
        other.setParticipantTypeCode(getParticipantTypeCode());
        other.setSourceTypeCode(getSourceTypeCode());
        other.setObjectTypeCode(getObjectTypeCode());
        return other;
    }

}
