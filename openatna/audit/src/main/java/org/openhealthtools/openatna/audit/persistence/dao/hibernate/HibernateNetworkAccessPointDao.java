package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.NetworkAccessPointDao;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Andrew Harrison
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateNetworkAccessPointDao extends AbstractHibernateDao<NetworkAccessPointEntity> implements NetworkAccessPointDao {

    public HibernateNetworkAccessPointDao(SessionFactory sessionFactory) {
        super(NetworkAccessPointEntity.class, sessionFactory);
    }

    public NetworkAccessPointEntity getById(Long id) {
        return get(id);
    }

    public NetworkAccessPointEntity getByTypeAndIdentifier(Short type, String identifier) {
        return uniqueResult(criteria().add(Restrictions.eq("type", type))
                .add(Restrictions.eq("identifier", identifier)));
    }

    public List<? extends NetworkAccessPointEntity> getByType(Short type) {
        return list(criteria().add(Restrictions.eq("type", type)));
    }

    public List<? extends NetworkAccessPointEntity> getByIdentifier(String identifier) {
        return list(criteria().add(Restrictions.eq("identifier", identifier)));
    }

    public List<? extends NetworkAccessPointEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends NetworkAccessPointEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public void save(NetworkAccessPointEntity nap, PersistencePolicies policies) throws AtnaPersistenceException {
        if (worthSaving(nap) && !isDuplicate(nap, policies)) {
            currentSession().saveOrUpdate(nap);
        }
    }

    public void delete(NetworkAccessPointEntity nap) {
        currentSession().delete(nap);
    }


    private boolean worthSaving(NetworkAccessPointEntity nap) {
        // both are null - don't persist it
        return nap.getIdentifier() != null || (nap.getType() >= 1 || nap.getType() <= 3);
    }

    private boolean isDuplicate(NetworkAccessPointEntity nap, PersistencePolicies policies) throws AtnaPersistenceException {

        NetworkAccessPointEntity entity = getByTypeAndIdentifier(nap.getType(), nap.getIdentifier());
        if (entity != null) {
            if (policies.isErrorOnDuplicateInsert()) {
                throw new AtnaPersistenceException("Attempt to load duplicate network access point.",
                        AtnaPersistenceException.PersistenceError.DUPLICATE_NETWORK_ACCESS_POINT);
            }
            return true;
        }
        return false;
    }
}
