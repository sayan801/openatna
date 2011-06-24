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

package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.model.Query;

/**
 * Builds a hibernate Criteria from a query
 * <p/>
 * NOTE: a current restriction is that the two restriction of OR and AND will only work on the same object.
 * This means for example, that
 * you cannot do an OR on, say a participant id and a source id, because these are properties of different objects,
 * but you can on participant id and participant name.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 7:10:40 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateQueryBuilder {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.persistence.dao.hibernate.HibernateQueryBuilder");


    private Criteria messageCriteria;
    private Criteria idCriteria;

    public HibernateQueryBuilder(HibernateMessageDao messageDao) {
        this.messageCriteria = messageDao.criteria();
        this.idCriteria = messageDao.criteria();
    }

    private Criterion createConditional(CriteriaNode root, CriteriaNode node, Query.ConditionalStatement value, String name) {
        Criteria c = node.getCriteria();
        Query.Conditional con = value.getConditional();
        Object val = value.getValue();
        Criterion cron = null;

        switch (con) {
            case MAX_NUM:
                idCriteria.setMaxResults((Integer) val);
                break;
            case START_OFFSET:
                idCriteria.setFirstResult((Integer) val);
                break;
            case AFTER:
                cron = Restrictions.ge(name, val);
                break;
            case BEFORE:
                cron = Restrictions.le(name, val);
                break;
            case CASE_INSENSITIVE_LIKE:
                cron = Restrictions.ilike(name, val);
                break;
            case EQUALS:
                cron = Restrictions.eq(name, val);
                break;
            case GREATER_THAN:
                cron = Restrictions.gt(name, val);
                break;
            case GREATER_THAN_OR_EQUAL:
                cron = Restrictions.ge(name, val);
                break;
            case LESS_THAN:
                cron = Restrictions.lt(name, val);
                break;
            case LESS_THAN_OR_EQUAL:
                cron = Restrictions.le(name, val);
                break;
            case LIKE:
                cron = Restrictions.like(name, val);
                break;
            case NOT_EQUAL:
                cron = Restrictions.ne(name, val);
                break;
            case OR:
                processJoint(root, value, false);
                break;
            case AND:
                processJoint(root, value, true);
                break;
            case NULLITY:
                Boolean b = (Boolean) val;
                cron = b ? Restrictions.isNull(name) : Restrictions.isNotNull(name);
                break;
            case ASC:
                messageCriteria.addOrder(Order.asc((String) val));
                break;
            case DESC:
                messageCriteria.addOrder(Order.desc((String) val));
                break;
            default:
                break;
        }
        if (cron != null && c != null) {
            c.add(cron);
        }
        return cron;
    }

    private void processJoint(CriteriaNode root, Query.ConditionalStatement value, boolean and) {
        try {
            Query.ConditionalStatement[] values = (Query.ConditionalStatement[]) value.getValue();
            if (values == null || values.length != 2) {
                log.info("Joint takes two conditional values");
                return;
            }
            Query.TargetPath tp = Query.createPath(values[0].getTarget());
            Query.TargetPath tp1 = Query.createPath(values[1].getTarget());
            if (!tp.getPaths().equals(tp1.getPaths())) {
                throw new IllegalArgumentException("cannot perform OR on two values with a different depth, i.e. relating to a different object");
            }
            CriteriaNode cn = getNode(root, tp);
            Criterion c0 = createConditional(root, cn, values[0], tp.getTarget());

            CriteriaNode cn1 = getNode(root, tp1);
            Criterion c1 = createConditional(root, cn1, values[1], tp1.getTarget());
            if (c0 != null && c1 != null) {
                if (and) {
                    cn1.getCriteria().add(Restrictions.and(c0, c1));
                } else {
                    cn1.getCriteria().add(Restrictions.or(c0, c1));
                }
            }
        } catch (Exception e) {
            log.info(e);
        }
    }

    /**
     * NOTE: HAVE TO USE TWO QUERIES HERE. There is NO way around that I can see to ensure getting
     * back both distinct, and correct max results restrictions.
     *
     * @param query
     * @return
     */
    public Criteria build(Query query) {

        Map<Query.Target, Set<Query.ConditionalStatement>> map = query.getConditionals();

        CriteriaNode root = new CriteriaNode(idCriteria, "MESSAGE");
        idCriteria.setProjection(Projections.id());
        Set<Query.Target> targets = map.keySet();
        for (Query.Target target : targets) {
            Query.TargetPath tp = Query.createPath(target);
            CriteriaNode node = getNode(root, tp);
            Set<Query.ConditionalStatement> values = map.get(target);
            for (Query.ConditionalStatement value : values) {
                createConditional(root, node, value, tp.getTarget());
            }
        }
        idCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        idCriteria.addOrder(Order.asc("id"));
        List<Long> ids = idCriteria.list();
        if (ids == null || ids.size() == 0) {
            return null;
        }
        if (ids.size() == 1) {
            messageCriteria.add(Restrictions.eq("id", ids.get(0)));
        } else {
            messageCriteria.add(Restrictions.in("id", ids));
        }
        return messageCriteria;
    }

    public CriteriaNode getNode(CriteriaNode parent, Query.TargetPath tp) {
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
