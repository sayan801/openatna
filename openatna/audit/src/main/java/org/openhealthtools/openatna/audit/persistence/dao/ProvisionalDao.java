package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.model.ProvisionalEntity;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
public interface ProvisionalDao {

    ProvisionalEntity getById(Long id) throws AtnaPersistenceException;

    void delete(ProvisionalEntity pe) throws AtnaPersistenceException;

    List<? extends ProvisionalEntity> getAll() throws AtnaPersistenceException;

    List<? extends ProvisionalEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    void save(ProvisionalEntity pe) throws AtnaPersistenceException;
}
