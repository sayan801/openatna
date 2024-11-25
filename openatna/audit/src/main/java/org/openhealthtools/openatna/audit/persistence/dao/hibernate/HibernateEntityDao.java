package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.SessionFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.EntityDao;
import org.openhealthtools.openatna.audit.persistence.model.PersistentEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Andrew Harrison
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateEntityDao extends AbstractHibernateDao<PersistentEntity> implements EntityDao {

    public HibernateEntityDao(SessionFactory sessionFactory) {
        super(PersistentEntity.class, sessionFactory);
    }

    public List<? extends PersistentEntity> query(String query) {
        return list(createQuery(query));
    }

    public String[] getSupportedQueryDialects() {
        return new String[]{"HQL"};
    }
}
