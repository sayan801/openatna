package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.model.QueryEntity;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
public interface QueryDao {

    QueryEntity getById(Long id) throws AtnaPersistenceException;

    void delete(QueryEntity pe) throws AtnaPersistenceException;

    List<? extends QueryEntity> getAll() throws AtnaPersistenceException;

    List<? extends QueryEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    void save(QueryEntity pe) throws AtnaPersistenceException;
}
