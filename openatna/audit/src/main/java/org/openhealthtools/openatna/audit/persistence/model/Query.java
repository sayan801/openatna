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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Allows construction of queries based on a few operators.
 * Conditionals are restrictions of some sort. The object value
 * is the value required by the conditional.
 * <p/>
 * NULLITY takes a Boolean value.
 * true = the value must be null and false = the value must not be null.
 * <p/>
 * ORDER takes a boolean value. True = ordering will be ascending, false = descending.
 * <p/>
 * and the target represents an element in an AtnaMessage.
 * <p/>
 * Two conditionals of the same type cannot be applied to the same target.
 * <p/>
 * The Object values required to query the targets are all pretty simple types:
 * basically strings, ints and dates, so this will work over the wire as well (i.e. in XmlSchema
 * or something similar)
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 1:17:54 PM
 * @date $Date:$ modified by $Author:$
 */

public class Query {

    private Map<Target, Set<ConditionalValue>> map = new HashMap<Target, Set<ConditionalValue>>();

    public static TargetPath createPath(Target target) {
        List<String> path = new ArrayList<String>();
        String s = null;
        switch (target) {
            case ID:
                s = "id";
                break;
            case RESULT:
                s = "result";
                break;
            case EVENT_ACTION:
                s = "eventActionCode";
                break;
            case EVENT_OUTCOME:
                s = "eventOutcome";
                break;
            case SOURCE_ADDRESS:
                s = "sourceAddress";
                break;
            case EVENT_TIME:
                s = "eventDateTime";
                break;
            case EVENT_ID_CODE:
                path.add("eventId");
                s = "code";
                break;
            case EVENT_ID_CODE_SYSTEM:
                path.add("eventId");
                s = "codeSystem";
                break;
            case EVENT_ID_CODE_SYSTEM_NAME:
                path.add("eventId");
                s = "codeSystemName";
                break;
            case EVENT_TYPE_CODE:
                path.add("eventTypeCodes");
                s = "code";
                break;
            case EVENT_TYPE_CODE_SYSTEM:
                path.add("eventTypeCodes");
                s = "codeSystem";
                break;
            case EVENT_TYPE_CODE_SYSTEM_NAME:
                path.add("eventTypeCodes");
                s = "codeSystemName";
                break;
            case NETWORK_ACCESS_POINT_ID:
                path.add("messageParticipants");
                path.add("networkAccessPoint");
                s = "identifier";
                break;
            case NETWORK_ACCESS_POINT_TYPE:
                path.add("messageParticipants");
                path.add("networkAccessPoint");
                s = "type";
                break;
            case OBJECT_ID:
                path.add("messageObjects");
                path.add("object");
                s = "objectId";
                break;
            case OBJECT_NAME:
                path.add("messageObjects");
                path.add("object");
                s = "objectName";
                break;
            case OBJECT_TYPE_CODE:
                path.add("messageObjects");
                path.add("object");
                path.add("objectIdTypeCode");
                s = "code";
                break;
            case OBJECT_TYPE_CODE_SYSTEM:
                path.add("messageObjects");
                path.add("object");
                path.add("objectIdTypeCode");
                s = "codeSystem";
                break;
            case OBJECT_TYPE_CODE_SYSTEM_NAME:
                path.add("messageObjects");
                path.add("object");
                path.add("objectIdTypeCode");
                s = "codeSystemName";
                break;
            case OBJECT_TYPE_ROLE:
                path.add("messageObjects");
                path.add("object");
                s = "objectTypeCodeRole";
                break;
            case OBJECT_TYPE:
                path.add("messageObjects");
                path.add("object");
                s = "objectTypeCode";
                break;
            case OBJECT_SENSITIVITY:
                path.add("messageObjects");
                path.add("object");
                s = "objectSensitivity";
                break;
            case PARTICIPANT_ID:
                path.add("messageParticipants");
                path.add("participant");
                s = "userId";
                break;
            case PARTICIPANT_NAME:
                path.add("messageParticipants");
                path.add("participant");
                s = "userName";
                break;
            case PARTICIPANT_TYPE_CODE:
                path.add("messageParticipants");
                path.add("participant");
                path.add("participantTypeCodes");
                s = "code";
                break;
            case PARTICIPANT_TYPE_CODE_SYSTEM:
                path.add("messageParticipants");
                path.add("participant");
                path.add("participantTypeCodes");
                s = "codeSystem";
                break;
            case PARTICIPANT_TYPE_CODE_SYSTEM_NAME:
                path.add("messageParticipants");
                path.add("participant");
                path.add("participantTypeCodes");
                s = "codeSystemName";
                break;
            case SOURCE_ID:
                path.add("messageSources");
                path.add("source");
                s = "sourceId";
                break;
            case SOURCE_ENTERPRISE_ID:
                path.add("messageSources");
                path.add("source");
                s = "enterpriseSiteId";
                break;
            case SOURCE_TYPE_CODE:
                path.add("messageSources");
                path.add("source");
                path.add("sourceTypeCodes");
                s = "code";
                break;
            case SOURCE_TYPE_CODE_SYSTEM:
                path.add("messageSources");
                path.add("source");
                path.add("sourceTypeCodes");
                s = "codeSystem";
                break;
            case SOURCE_TYPE_CODE_SYSTEM_NAME:
                path.add("messageSources");
                path.add("source");
                path.add("sourceTypeCodes");
                s = "codeSystemName";
                break;
            default:
                break;
        }
        if (s != null) {
            return new TargetPath(path, s);
        }
        return null;
    }

    public static enum Target {
        ID,
        SOURCE_ADDRESS,
        EVENT_TIME,
        EVENT_ID_CODE,
        EVENT_ID_CODE_SYSTEM,
        EVENT_ID_CODE_SYSTEM_NAME,
        EVENT_TYPE_CODE,
        EVENT_TYPE_CODE_SYSTEM,
        EVENT_TYPE_CODE_SYSTEM_NAME,
        EVENT_ACTION,
        EVENT_OUTCOME,
        SOURCE_ID,
        SOURCE_ENTERPRISE_ID,
        SOURCE_TYPE_CODE,
        SOURCE_TYPE_CODE_SYSTEM,
        SOURCE_TYPE_CODE_SYSTEM_NAME,
        PARTICIPANT_ID,
        PARTICIPANT_NAME,
        PARTICIPANT_TYPE_CODE,
        PARTICIPANT_TYPE_CODE_SYSTEM,
        PARTICIPANT_TYPE_CODE_SYSTEM_NAME,
        OBJECT_ID,
        OBJECT_NAME,
        OBJECT_TYPE,
        OBJECT_TYPE_CODE,
        OBJECT_TYPE_CODE_SYSTEM,
        OBJECT_TYPE_CODE_SYSTEM_NAME,
        OBJECT_TYPE_ROLE,
        OBJECT_SENSITIVITY,
        NETWORK_ACCESS_POINT_ID,
        NETWORK_ACCESS_POINT_TYPE,

        // result target
        // currently this accepts MAX_NUM and START_OFFSET as conditionals
        RESULT;
    }

    public static enum Conditional {
        BEFORE,
        AFTER,
        LIKE,
        CASE_INSENSITIVE_LIKE,
        EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL,
        NOT_EQUAL,
        NULLITY,
        ASC,
        DESC,

        // query result parameters - both ints
        MAX_NUM,
        START_OFFSET
    }

    public boolean hasConditionals() {
        if (map.keySet().size() == 0) {
            return false;
        }
        if (map.keySet().size() == 1 && map.keySet().contains(Target.RESULT)) {
            return false;
        }
        return true;
    }

    public Query addConditional(Conditional c, Object value, Target target) {
        if (c == null || value == null || target == null) {
            throw new IllegalArgumentException("parameters cannot be null");
        }
        Set<ConditionalValue> existing = map.get(target);
        if (existing == null) {
            existing = new HashSet<ConditionalValue>();
        }
        existing.add(new ConditionalValue(c, value));
        map.put(target, existing);
        return this;
    }

    public Query between(Date start, Date end) {
        addConditional(Conditional.AFTER, start, Target.EVENT_TIME);
        addConditional(Conditional.BEFORE, end, Target.EVENT_TIME);
        return this;
    }

    public Query before(Date end) {
        addConditional(Conditional.BEFORE, end, Target.EVENT_TIME);
        return this;
    }

    public Query after(Date start) {
        addConditional(Conditional.AFTER, start, Target.EVENT_TIME);
        return this;
    }

    public Query like(Object value, Target target, boolean caseSensitive) {
        Conditional c = caseSensitive ? Conditional.CASE_INSENSITIVE_LIKE : Conditional.LIKE;
        addConditional(c, value, target);
        return this;
    }

    public Query equals(Object value, Target target) {
        addConditional(Conditional.EQUALS, value, target);
        return this;
    }

    public Query greaterThan(Object value, Target target) {
        addConditional(Conditional.GREATER_THAN, value, target);
        return this;
    }

    public Query lessThan(Object value, Target target) {
        addConditional(Conditional.LESS_THAN, value, target);
        return this;
    }

    public Query greaterThanOrEqual(Object value, Target target) {
        addConditional(Conditional.GREATER_THAN_OR_EQUAL, value, target);
        return this;
    }

    public Query lessThanOrEqual(Object value, Target target) {
        addConditional(Conditional.LESS_THAN_OR_EQUAL, value, target);
        return this;
    }

    public Query notEquals(Object value, Target target) {
        addConditional(Conditional.NOT_EQUAL, value, target);
        return this;
    }

    public Query notNull(Target target) {
        addConditional(Conditional.NULLITY, Boolean.FALSE, target);
        return this;
    }

    public Map<Target, Set<ConditionalValue>> getConditionals() {
        return Collections.unmodifiableMap(map);
    }

    public Query orderAscending(Target value) {
        addConditional(Conditional.ASC, createPath(value).toString(), Target.RESULT);
        return this;
    }

    public Query orderDescending(Target value) {
        addConditional(Conditional.DESC, createPath(value).toString(), Target.RESULT);
        return this;
    }

    public Query setMaxResults(Integer results) {
        addConditional(Conditional.MAX_NUM, results, Target.RESULT);
        return this;
    }

    public Query setStartOffset(Integer results) {
        addConditional(Conditional.START_OFFSET, results, Target.RESULT);
        return this;
    }

    public static class ConditionalValue {
        private Conditional conditional;
        private Object value;

        public ConditionalValue(Conditional conditional, Object value) {
            this.conditional = conditional;
            this.value = value;
        }

        public Conditional getConditional() {
            return conditional;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ConditionalValue that = (ConditionalValue) o;

            if (conditional != that.conditional) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return conditional != null ? conditional.hashCode() : 0;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getClass().getName());
        for (Target target : map.keySet()) {
            sb.append("[target=").append(target.toString());
            Set<ConditionalValue> l = map.get(target);
            for (ConditionalValue value : l) {
                sb.append(" conditional=")
                        .append(value.getConditional())
                        .append(" value=")
                        .append(value.getValue().toString());
            }
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }


    public static class TargetPath {
        private List<String> paths;
        private String target;

        private TargetPath(List<String> paths, String target) {
            this.paths = paths;
            this.target = target;
        }

        public List<String> getPaths() {
            return paths;
        }

        public String getTarget() {
            return target;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (String path : paths) {
                sb.append(path).append(".");
            }
            sb.append(target);
            return sb.toString();
        }
    }
}
