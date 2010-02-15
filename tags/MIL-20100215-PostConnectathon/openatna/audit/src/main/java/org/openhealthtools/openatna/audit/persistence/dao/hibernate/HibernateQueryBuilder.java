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

package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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
    private Criteria idCriteria;

    public HibernateQueryBuilder(HibernateMessageDao messageDao) {
        this.messageCriteria = messageDao.criteria();
        this.idCriteria = messageDao.criteria();
    }

    private void createConditional(CriteriaNode node, Query.ConditionalValue value, String name) {
        Criteria c = node.getCriteria();
        Query.Conditional con = value.getConditional();
        Object val = value.getValue();

        switch (con) {
            case MAX_NUM:
                idCriteria.setMaxResults((Integer) val);
                break;
            case START_OFFSET:
                idCriteria.setFirstResult((Integer) val);
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
            case ASC:
                messageCriteria.addOrder(Order.asc((String) val));
                break;
            case DESC:
                messageCriteria.addOrder(Order.desc((String) val));
                break;
            default:
                break;
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

        Map<Query.Target, Set<Query.ConditionalValue>> map = query.getConditionals();

        CriteriaNode root = new CriteriaNode(idCriteria, "MESSAGE");
        idCriteria.setProjection(Projections.id());
        Set<Query.Target> targets = map.keySet();
        for (Query.Target target : targets) {
            Query.TargetPath tp = Query.createPath(target);
            CriteriaNode node = getNode(root, tp);
            Set<Query.ConditionalValue> values = map.get(target);
            for (Query.ConditionalValue value : values) {
                createConditional(node, value, tp.getTarget());
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
