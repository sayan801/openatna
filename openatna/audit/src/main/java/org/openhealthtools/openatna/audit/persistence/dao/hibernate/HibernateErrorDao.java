package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateErrorDao extends AbstractHibernateDao<ErrorEntity> implements ErrorDao {

    public HibernateErrorDao(SessionFactory sessionFactory) {
        super(ErrorEntity.class, sessionFactory);
    }

    public ErrorEntity getById(Long id) {
        return get(id);
    }

    public List<? extends ErrorEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends ErrorEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public List<? extends ErrorEntity> getBySourceIp(String ip) {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)));
    }

    public List<? extends ErrorEntity> getAfter(Date date) {
        return list(criteria().add(Restrictions.ge("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getAfter(String ip, Date date) {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)).add(Restrictions.ge("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getBefore(Date date) {
        return list(criteria().add(Restrictions.le("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getBefore(String ip, Date date) {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)).add(Restrictions.le("errorTimestamp", date)));
    }

    public List<? extends ErrorEntity> getBetween(Date first, Date second) {
        return list(criteria().add(Restrictions.ge("errorTimestamp", first)).add(Restrictions.le("errorTimestamp", second)));
    }

    public List<? extends ErrorEntity> getBetween(String ip, Date first, Date second) {
        return list(criteria().add(Restrictions.eq("sourceIp", ip)).add(Restrictions.ge("errorTimestamp", first))
                .add(Restrictions.le("errorTimestamp", second)));
    }

    public void save(ErrorEntity entity) {
        currentSession().saveOrUpdate(entity);
    }
}
