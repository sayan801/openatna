package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Hibernate Dao class
 *
 * @author Andrew Harrison
 */
public abstract class AbstractHibernateDao<E> {

    private final Class<E> entityClass;
    private final SessionFactory sessionFactory;

    public AbstractHibernateDao(Class<E> entityClass, SessionFactory sessionFactory) {

        if (entityClass == null) {
            throw new IllegalArgumentException("entityClass must not be null");
        }
        if (sessionFactory == null) {
            throw new IllegalArgumentException("sessionFactory must not be null");
        }

        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    protected Criteria criteria() {
        return currentSession().createCriteria(entityClass).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }

    protected Criteria criteria(Class entityClass) {
        return currentSession().createCriteria(entityClass).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }

    protected Query createQuery(String hql) {
        return currentSession().createQuery(hql);
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected List<? extends E> all() {
        return list(criteria());
    }

    protected List<? extends E> all(int offset, int amount) {

        Criteria messageCriteria = criteria();
        Criteria idCriteria = criteria()
                .setProjection(Projections.id())
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.asc("id"))
                .setFirstResult(offset)
                .setMaxResults(amount);
        List<Long> ids = idCriteria.list();
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        if (ids.size() == 1) {
            messageCriteria.add(Restrictions.eq("id", ids.get(0)));
        } else {
            messageCriteria.add(Restrictions.in("id", ids));
        }
        return list(messageCriteria);
    }

    public Class<? extends E> getEntityClass() {
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    protected List<? extends E> list(Criteria criteria) {
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    protected List<? extends E> list(Query query) {
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected E uniqueResult(Criteria criteria) {
        return (E) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    protected E uniqueResult(Query query) {
        return (E) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    protected E get(Serializable id) {
        return (E) currentSession().get(entityClass, id);
    }
}
