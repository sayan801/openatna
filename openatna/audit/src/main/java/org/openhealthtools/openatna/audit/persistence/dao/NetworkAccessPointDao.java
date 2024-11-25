package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;

import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public interface NetworkAccessPointDao extends Dao {

    NetworkAccessPointEntity getById(Long id) throws AtnaPersistenceException;

    NetworkAccessPointEntity getByTypeAndIdentifier(Short type, String identifier) throws AtnaPersistenceException;

    List<? extends NetworkAccessPointEntity> getByType(Short type) throws AtnaPersistenceException;

    List<? extends NetworkAccessPointEntity> getByIdentifier(String identifier) throws AtnaPersistenceException;

    List<? extends NetworkAccessPointEntity> getAll() throws AtnaPersistenceException;

    List<? extends NetworkAccessPointEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    void save(NetworkAccessPointEntity nap, PersistencePolicies policies) throws AtnaPersistenceException;

    void delete(NetworkAccessPointEntity nap) throws AtnaPersistenceException;
}
