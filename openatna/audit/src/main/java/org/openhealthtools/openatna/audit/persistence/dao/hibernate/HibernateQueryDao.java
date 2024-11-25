package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.SessionFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.QueryDao;
import org.openhealthtools.openatna.audit.persistence.model.QueryEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateQueryDao extends AbstractHibernateDao<QueryEntity> implements QueryDao {

    public HibernateQueryDao(SessionFactory sessionFactory) {
        super(QueryEntity.class, sessionFactory);
    }

    public QueryEntity getById(Long id) {
        return get(id);
    }

    public List<? extends QueryEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends QueryEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public void save(QueryEntity pe) {
        currentSession().saveOrUpdate(pe);
    }

    public void delete(QueryEntity pe) {
        currentSession().delete(pe);
    }
}
