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

package org.openhealthtools.openatna.persistence.util;

import java.util.*;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.persistence.model.Query;

/**
 * reads in a query in the form of a string and converts it to a Query.
 * <p/>
 * The query string takes the format:
 * <p/>
 * target:condition=value
 * <p/>
 * e.g.:
 * <p/>
 * EVENT_ACTION EQ="CREATE"
 * <p/>
 * meaning the constraint is on the event action which should equal the CREATE event action.
 * <p/>
 * Constraints are separated by space or nothing.
 * double quotes within a value must be escaped using a backslash.
 * Backslashes must be escaped using a double backslash.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 2, 2009: 5:06:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class QueryString {

    public static char[] escaped = {'"', '\\'};

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
                    for (char c1 : escaped) {
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
            for (char c1 : escaped) {
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
        if (value.equalsIgnoreCase("true") ||
                value.equalsIgnoreCase("false")) {
            return new Boolean(value.toLowerCase());
        }
        switch (target) {
            case EVENT_ACTION:
                return EventAction.getAction(value);
            case EVENT_OUTCOME:
                return EventOutcome.getOutcome(Integer.parseInt(value));
            case OBJECT_TYPE:
                return ObjectType.getType(Integer.parseInt(value));
            case OBJECT_TYPE_ROLE:
                return ObjectTypeCodeRole.getRole(Integer.parseInt(value));
            case NETWORK_ACCESS_POINT_TYPE:
                return NetworkAccessPoint.getAccessPoint(Integer.parseInt(value));
            case EVENT_TIME:
                return Timestamp.parse(value);
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

        Map<Query.Target, Set<Query.ConditionalValue>> map = query.getConditionals();
        for (Query.Target target : map.keySet()) {
            Set<Query.ConditionalValue> c = map.get(target);
            for (Query.ConditionalValue cv : c) {
                sb.append(target.toString())
                        .append(" ")
                        .append(cv.getConditional().toString())
                        .append(" ")
                        .append("\"")
                        .append(escape(getStringFromObject(cv.getValue())))
                        .append("\" ");
            }
        }
        return sb.toString();
    }

    /**
     * states:
     * 0 = starting
     * 1 = in target
     * 2 = after target, before condition ("=" or space char)
     * 3 = in condition
     * 4 - after condition, before value (":" or space char)
     * 5 - in value
     * 6 - after value (""" or space char)
     * <p/>
     * EVENT_ACTION EQ "CREATE"
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
        StringBuilder sb = new StringBuilder();
        boolean afterBackslash = false;
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
                        targets.add(sb.toString());
                        sb.delete(0, sb.length());
                        state = 2;
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
                        for (char c1 : escaped) {
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


        return buildQuery(targets, conditions, values);

    }

    private static Query buildQuery(List<String> targets, List<String> conditions, List<String> values) {
        if (targets.size() != conditions.size() &&
                conditions.size() != values.size()) {
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
        return query;
    }

    public static void main(String[] args) {
        Query query = new Query();
        query.notNull(Query.Target.EVENT_OUTCOME)
                .equals("ITI-8", Query.Target.EVENT_ID_CODE)
                .equals("IHE Transactions", Query.Target.EVENT_ID_CODE_SYSTEM_NAME)
                .equals(EventAction.CREATE, Query.Target.EVENT_ACTION)
                .equals("scmabh", Query.Target.PARTICIPANT_ID)
                .equals(new Date(), Query.Target.EVENT_TIME)
                .equals("bla \",,,^^^^  ble", Query.Target.OBJECT_SENSITIVITY)
                .orderDescending(Query.Target.EVENT_TIME);
        String s = QueryString.create(query);
        System.out.println(s);
        System.out.println("==========");
        query = QueryString.parse(s);
        System.out.println(query);

    }

    private static String q = "EVENT_ID_CODE EQUALS=\"ITI-8\"EVENT_OUTCOME NULLITY=\"false\"EVENT_TIME EQUALS=\"2009-10-03T18:57:33.150+01:00\"EVENT_ID_CODE_SYSTEM_NAME EQUALS=\"IHE Transactions\"OBJECT_SENSITIVITY EQUALS=\"bla \\\",,,^^^^  ble\"PARTICIPANT_ID EQUALS=\"scmabh\"EVENT_ACTION EQUALS=\"CREATE\"";

}
