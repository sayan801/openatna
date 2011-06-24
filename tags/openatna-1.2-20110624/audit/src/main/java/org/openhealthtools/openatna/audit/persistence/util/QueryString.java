/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
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

package org.openhealthtools.openatna.audit.persistence.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhealthtools.openatna.anom.EventAction;
import org.openhealthtools.openatna.anom.EventOutcome;
import org.openhealthtools.openatna.anom.NetworkAccessPoint;
import org.openhealthtools.openatna.anom.ObjectType;
import org.openhealthtools.openatna.anom.ObjectTypeCodeRole;
import org.openhealthtools.openatna.anom.Timestamp;
import org.openhealthtools.openatna.audit.persistence.model.Query;

/**
 * reads in a query in the form of a string and converts it to a Query.
 * <p/>
 * The query string takes the format:
 * <p/>
 * target condition "value"
 * <p/>
 * e.g.:
 * <p/>
 * EVENT_ACTION EQUALS "CREATE"
 * <p/>
 * meaning the constraint is on the event action which should equal the CREATE event action.
 * <p/>
 * <p/>
 * OR and AND are also supported, e.g.:
 * <p/>
 * EVENT_ACTION EQUALS "CREATE" SOURCE_ID EQUALS "123" OR PARTICIPANT_ID LIKE "12%"
 * <p/>
 * Constraints are separated by space or nothing.
 * double quotes within a value must be escaped using a backslash.
 * Backslashes must be escaped using a double backslash.
 * <p/>
 * Dates should be in ISO8601 format
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 2, 2009: 5:06:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class QueryString {

    public static final char[] ESCAPED = {'"', '\\'};

    private QueryString() {
    }

    public static String unescape(String param) {
        boolean afterBaskslash = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (c == '\\') {
                afterBaskslash = true;
            } else {
                if (afterBaskslash) {
                    boolean toescape = false;
                    for (char c1 : ESCAPED) {
                        if (c == c1) {
                            toescape = true;
                            break;
                        }
                    }
                    if (!toescape) {
                        sb.append('\\');
                    }
                    afterBaskslash = false;
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String escape(String param) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            for (char c1 : ESCAPED) {
                if (c == c1) {
                    sb.append('\\');
                    break;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static Object getObjectForType(Query.Target target, String value) {
        if (value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("false")) {
            return new Boolean(value.toLowerCase());
        }
        switch (target) {
            case RESULT:
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return value;
                }
            case EVENT_ACTION:
                return value;
            case EVENT_OUTCOME:
                return Integer.parseInt(value);
            case OBJECT_TYPE:
                return Integer.parseInt(value);
            case OBJECT_TYPE_ROLE:
                return Integer.parseInt(value);
            case NETWORK_ACCESS_POINT_TYPE:
                return Integer.parseInt(value);
            case EVENT_TIME:
                return Timestamp.parseToDate(value);
            default:
                return value;
        }
    }

    private static String getStringFromObject(Object value) {
        if (value instanceof Date) {
            return Timestamp.format((Date) value);
        } else if (value instanceof EventAction) {
            return ((EventAction) value).value().toString();
        } else if (value instanceof EventOutcome) {
            return Integer.toString(((EventOutcome) value).value());
        } else if (value instanceof ObjectType) {
            return Integer.toString(((ObjectType) value).value());
        } else if (value instanceof ObjectTypeCodeRole) {
            return Integer.toString(((ObjectTypeCodeRole) value).value());
        } else if (value instanceof NetworkAccessPoint) {
            return Integer.toString(((NetworkAccessPoint) value).value());
        }
        return value.toString();

    }

    public static String create(Query query) {
        StringBuilder sb = new StringBuilder();

        Map<Query.Target, Set<Query.ConditionalStatement>> map = query.getConditionals();
        for (Query.Target target : map.keySet()) {
            Set<Query.ConditionalStatement> c = map.get(target);
            for (Query.ConditionalStatement cv : c) {
                Object val = cv.getValue();
                if (val instanceof Query.ConditionalStatement[]) {
                    sb.append(serializeJoint((Query.ConditionalStatement[]) val, cv.getConditional()));
                } else {
                    sb.append(target.toString())
                            .append(" ")
                            .append(cv.getConditional().toString())
                            .append(" ")
                            .append("\"")
                            .append(escape(getStringFromObject(val)))
                            .append("\" ");
                }
            }
        }
        return sb.toString();
    }

    private static String serializeJoint(Query.ConditionalStatement[] values, Query.Conditional conditional) {
        StringBuilder sb = new StringBuilder(" ");
        Query.ConditionalStatement ct1 = values[0];
        Query.ConditionalStatement ct2 = values[1];
        sb.append(ct1.getTarget().toString())
                .append(" ")
                .append(ct1.getConditional().toString())
                .append(" ")
                .append("\"")
                .append(escape(getStringFromObject(ct1.getValue())))
                .append("\" ")
                .append(conditional.toString())
                .append(" ")
                .append(ct2.getTarget().toString())
                .append(" ")
                .append(ct2.getConditional().toString())
                .append(" ")
                .append("\"")
                .append(escape(getStringFromObject(ct2.getValue())))
                .append("\" ");
        return sb.toString();
    }

    /**
     * states:
     * 0 = starting
     * 1 = in target
     * 2 = after target, before condition (space char)
     * 3 = in condition
     * 4 - after condition, before value (""" or space char)
     * 5 - in value
     * <p/>
     *
     * @param queryString
     * @return
     */
    public static Query parse(String queryString) {
        int state = 0;
        char[] chars = queryString.toCharArray();
        List<String> targets = new ArrayList<String>();
        List<String> conditions = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        List<Joint> joints = new ArrayList<Joint>();
        StringBuilder sb = new StringBuilder();
        boolean afterBackslash = false;
        Joint currJoint = null;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (state) {
                case 0:
                    if (!Character.isSpaceChar(c)) {
                        sb.append(c);
                        state = 1;
                    }
                    break;
                case 1:
                    if (Character.isSpaceChar(c)) {
                        if (sb.length() == 0) {
                            throw new IllegalArgumentException("0 length target.");
                        }
                        String s = sb.toString();
                        if (s.equals("OR") || s.equals("AND")) {
                            // process join
                            currJoint = new Joint();
                            currJoint.setTarget1(targets.remove(targets.size() - 1));
                            currJoint.setConditional1(conditions.remove(conditions.size() - 1));
                            currJoint.setValue1(values.remove(values.size() - 1));
                            currJoint.setJoint(s);
                            state = 0;
                        } else {
                            targets.add(s);
                            state = 2;
                        }
                        sb.delete(0, sb.length());

                    } else {
                        sb.append(c);
                    }
                    break;
                case 2:
                    if (!Character.isSpaceChar(c)) {
                        sb.append(c);
                        state = 3;
                    }
                    break;
                case 3:
                    if (Character.isSpaceChar(c)) {
                        if (sb.length() == 0) {
                            throw new IllegalArgumentException("0 length condition.");
                        }
                        conditions.add(sb.toString());
                        sb.delete(0, sb.length());
                        state = 4;
                    } else {
                        sb.append(c);
                    }
                    break;
                case 4:
                    if (c == '"') {
                        state = 5;
                    }
                    break;
                case 5:
                    if (afterBackslash) {
                        afterBackslash = false;
                        boolean shouldBeEscaped = false;
                        for (char c1 : ESCAPED) {
                            if (c == c1) {
                                shouldBeEscaped = true;
                                break;
                            }
                        }
                        if (!shouldBeEscaped) {
                            throw new IllegalArgumentException("invalid charater:" + c);
                        }
                        sb.append(c);
                    } else {
                        if (c == '"') { // end of value
                            if (sb.length() == 0) {
                                throw new IllegalArgumentException("no value defined");
                            }
                            values.add(sb.toString());
                            sb.delete(0, sb.length());
                            state = 0;
                            if (currJoint != null) {
                                currJoint.setTarget2(targets.remove(targets.size() - 1));
                                currJoint.setConditional2(conditions.remove(conditions.size() - 1));
                                currJoint.setValue2(values.remove(values.size() - 1));
                                joints.add(currJoint);
                                currJoint = null;
                            }
                        } else if ((c == '\\')) {
                            afterBackslash = true;
                        } else {
                            sb.append(c);
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unknown state " + state);
            }
        }
        return buildQuery(targets, conditions, values, joints);
    }


    private static Query buildQuery(List<String> targets, List<String> conditions, List<String> values, List<Joint> joints) {
        if (targets.size() != conditions.size()
                && conditions.size() != values.size()) {
            throw new IllegalArgumentException("targets, conditions and values are not the same size");
        }
        Query query = new Query();
        for (int i = 0; i < targets.size(); i++) {
            String target = targets.get(i);
            Query.Target t = Query.Target.valueOf(target);
            if (t == null) {
                throw new IllegalArgumentException("unknown target:" + target);
            }
            Query.Conditional c = Query.Conditional.valueOf(conditions.get(i));
            if (c == null) {
                throw new IllegalArgumentException("unknown condition:" + conditions.get(i));
            }
            Object o = getObjectForType(t, values.get(i));
            query.addConditional(c, o, t);
        }
        for (Joint joint : joints) {
            Query.Target t1 = Query.Target.valueOf(joint.getTarget1());
            if (t1 == null) {
                throw new IllegalArgumentException("unknown target:" + joint.getTarget1());
            }
            Query.Conditional c1 = Query.Conditional.valueOf(joint.getConditional1());
            if (c1 == null) {
                throw new IllegalArgumentException("unknown condition:" + joint.getConditional1());
            }
            Object o1 = getObjectForType(t1, joint.getValue1());

            Query.Target t2 = Query.Target.valueOf(joint.getTarget2());
            if (t2 == null) {
                throw new IllegalArgumentException("unknown target:" + joint.getTarget2());
            }
            Query.Conditional c2 = Query.Conditional.valueOf(joint.getConditional2());
            if (c2 == null) {
                throw new IllegalArgumentException("unknown condition:" + joint.getConditional2());
            }
            Object o2 = getObjectForType(t2, joint.getValue2());

            Query.ConditionalStatement ct1 = new Query.ConditionalStatement(c1, o1, t1);
            Query.ConditionalStatement ct2 = new Query.ConditionalStatement(c2, o2, t2);
            Query.ConditionalStatement[] arr = new Query.ConditionalStatement[]{ct1, ct2};

            Query.Conditional c3 = Query.Conditional.valueOf(joint.getJoint());
            query.addConditional(c3, arr, Query.Target.MESSAGE);
        }
        return query;
    }

    private static class Joint {

        private String target1;
        private String target2;
        private String joint;
        private String conditional1;
        private String conditional2;
        private String value1;
        private String value2;

        public String getTarget1() {
            return target1;
        }

        public void setTarget1(String target1) {
            this.target1 = target1;
        }

        public String getTarget2() {
            return target2;
        }

        public void setTarget2(String target2) {
            this.target2 = target2;
        }

        public String getJoint() {
            return joint;
        }

        public void setJoint(String joint) {
            this.joint = joint;
        }

        public String getConditional1() {
            return conditional1;
        }

        public void setConditional1(String conditional1) {
            this.conditional1 = conditional1;
        }

        public String getConditional2() {
            return conditional2;
        }

        public void setConditional2(String conditional2) {
            this.conditional2 = conditional2;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }
    }

    public static void main(String[] args) {
        Query query = new Query();
        Query.ConditionalStatement t1 = new Query.ConditionalStatement(Query.Conditional.EQUALS, "1",
                Query.Target.PARTICIPANT_TYPE_CODE_SYSTEM_NAME);
        Query.ConditionalStatement t2 = new Query.ConditionalStatement(Query.Conditional.EQUALS, "1",
                Query.Target.PARTICIPANT_TYPE_CODE_SYSTEM);
        query.notNull(Query.Target.EVENT_OUTCOME)
                .equals("ITI-8", Query.Target.EVENT_ID_CODE)
                .equals("IHE Transactions", Query.Target.EVENT_ID_CODE_SYSTEM_NAME)
                .equals(EventAction.CREATE, Query.Target.EVENT_ACTION)
                .equals("scmabh", Query.Target.PARTICIPANT_ID)
                .equals(new Date(), Query.Target.EVENT_TIME)
                .equals("bla \",,,^^^^  ble", Query.Target.OBJECT_SENSITIVITY)
                .or(t1, t2)
                .orderDescending(Query.Target.EVENT_TIME);
        String s = QueryString.create(query);
        System.out.println(s);
        System.out.println("==========");
        //String s = "EVENT_ID_CODE EQUALS \"ITI-8\" PARTICIPANT_TYPE_CODE LIKE \"L%\" OR SOURCE_TYPE_CODE EQUALS \"1223\"";
        System.out.println("QueryString.main query:" + s);
        query = QueryString.parse(s);
        System.out.println(query);

    }


}
