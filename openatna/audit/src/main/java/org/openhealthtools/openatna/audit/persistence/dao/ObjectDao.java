package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
public interface ObjectDao {

    ObjectEntity getById(Long id) throws AtnaPersistenceException;

    ObjectEntity getByObjectId(String id) throws AtnaPersistenceException;

    ObjectEntity get(ObjectEntity other) throws AtnaPersistenceException;

    List<? extends ObjectEntity> getByName(String name) throws AtnaPersistenceException;

    List<? extends ObjectEntity> getByTypeCode(Short type) throws AtnaPersistenceException;

    List<? extends ObjectEntity> getByTypeCodeRole(Short type) throws AtnaPersistenceException;

    List<? extends ObjectEntity> getBySensitivity(String sensitivity) throws AtnaPersistenceException;

    List<? extends ObjectEntity> getAll() throws AtnaPersistenceException;

    List<? extends ObjectEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    List<? extends ObjectEntity> getByObjectIdTypeCode(ObjectIdTypeCodeEntity code) throws AtnaPersistenceException;

    void save(ObjectEntity entity, PersistencePolicies policies) throws AtnaPersistenceException;

    void delete(ObjectEntity entity) throws AtnaPersistenceException;
}
