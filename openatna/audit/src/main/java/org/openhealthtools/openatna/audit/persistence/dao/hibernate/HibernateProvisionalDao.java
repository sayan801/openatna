package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.SessionFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.ProvisionalDao;
import org.openhealthtools.openatna.audit.persistence.model.ProvisionalEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateProvisionalDao extends AbstractHibernateDao<ProvisionalEntity> implements ProvisionalDao {
    
    public HibernateProvisionalDao(SessionFactory sessionFactory) {
        super(ProvisionalEntity.class, sessionFactory);
    }

    public ProvisionalEntity getById(Long id) {
        return get(id);
    }

    public List<? extends ProvisionalEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends ProvisionalEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public void save(ProvisionalEntity pe) {
        currentSession().saveOrUpdate(pe);
    }

    public void delete(ProvisionalEntity pe) {
        currentSession().delete(pe);
    }
}
