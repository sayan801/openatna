/*
 * Copyright (c) 2010 University of Cardiff and others.
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

package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.model.Query;

/**
 * Builds a hibernate Criteria from a query
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 7:10:40 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateQueryBuilder {

    private Criteria messageCriteria;

    public HibernateQueryBuilder(Criteria messageCriteria) {
        this.messageCriteria = messageCriteria;
    }

    private TargetPath createPath(Query.Target target) {
        List<String> path = new ArrayList<String>();
        String s = null;
        switch (target) {
            case RESULT:
                s = "result";
                break;
            case EVENT_ACTION:
                s = "eventActionCode";
                break;
            case EVENT_OUTCOME:
                s = "eventOutcome";
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

    private void createConditional(CriteriaNode node, Query.ConditionalValue value, String name) {
        Criteria c = node.getCriteria();
        Query.Conditional con = value.getConditional();
        Object val = value.getValue();

        switch (con) {
            case MAX_NUM:
                c.setMaxResults((Integer) val);
                break;
            case START_OFFSET:
                c.setFirstResult((Integer) val);
                break;
            case AFTER:
                c.add(Restrictions.ge(name, val));
                break;
            case BEFORE:
                c.add(Restrictions.le(name, val));
                break;
            case CASE_INSENSITIVE_LIKE:
                c.add(Restrictions.ilike(name, val));
                break;
            case EQUALS:
                c.add(Restrictions.eq(name, val));
                break;
            case GREATER_THAN:
                c.add(Restrictions.gt(name, val));
                break;
            case GREATER_THAN_OR_EQUAL:
                c.add(Restrictions.ge(name, val));
                break;
            case LESS_THAN:
                c.add(Restrictions.lt(name, val));
                break;
            case LESS_THAN_OR_EQUAL:
                c.add(Restrictions.le(name, val));
                break;
            case LIKE:
                c.add(Restrictions.like(name, val));
                break;
            case NOT_EQUAL:
                c.add(Restrictions.ne(name, val));
                break;
            case NULLITY:
                Boolean b = (Boolean) val;
                if (b) {
                    c.add(Restrictions.isNull(name));
                } else {
                    c.add(Restrictions.isNotNull(name));
                }
                break;
            case ORDER:
                b = (Boolean) val;
                if (b) {
                    c.addOrder(Order.asc(name));
                } else {
                    c.addOrder(Order.desc(name));
                }
                break;
            default:
                break;
        }

    }


    public Criteria build(Query query) {
        Map<Query.Target, Set<Query.ConditionalValue>> map = query.getConditionals();
        CriteriaNode root = new CriteriaNode(messageCriteria, "MESSAGE");

        Set<Query.Target> targets = map.keySet();
        for (Query.Target target : targets) {

            TargetPath tp = createPath(target);
            CriteriaNode node = getNode(root, tp);

            Set<Query.ConditionalValue> values = map.get(target);
            for (Query.ConditionalValue value : values) {
                createConditional(node, value, tp.getTarget());
            }
        }
        return root.getCriteria();
    }

    public CriteriaNode getNode(CriteriaNode parent, TargetPath tp) {
        List<String> paths = tp.getPaths();
        CriteriaNode dest = parent;
        for (String path : paths) {
            dest = getNode(dest, path);
        }
        return dest;
    }

    public CriteriaNode getNode(CriteriaNode parent, String path) {
        List<CriteriaNode> children = parent.getChildren();
        boolean create = true;
        CriteriaNode node = null;
        for (CriteriaNode child : children) {
            if (child.getCriteriaName().equals(path)) {
                create = false;
                node = child;
                break;
            }
        }
        if (create) {
            node = new CriteriaNode(parent.getCriteria().createCriteria(path), path);
            parent.addChild(node);
        }
        return node;
    }

    private static class TargetPath {
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
    }

    private static class CriteriaNode {

        private Criteria criteria;
        private String criteriaName;
        private List<CriteriaNode> children = new ArrayList<CriteriaNode>();


        private CriteriaNode(Criteria criteria, String criteriaName) {
            this.criteria = criteria;
            this.criteriaName = criteriaName;
        }

        public Criteria getCriteria() {
            return criteria;
        }

        public String getCriteriaName() {
            return criteriaName;
        }

        public List<CriteriaNode> getChildren() {
            return children;
        }

        public void addChild(CriteriaNode child) {
            this.children.add(child);
        }
    }


}
