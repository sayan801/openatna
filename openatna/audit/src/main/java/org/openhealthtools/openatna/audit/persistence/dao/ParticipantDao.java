package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.ParticipantCodeEntity;

import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public interface ParticipantDao extends Dao {

    ParticipantEntity getById(Long id) throws AtnaPersistenceException;

    List<? extends ParticipantEntity> getByUserId(String userId) throws AtnaPersistenceException;

    ParticipantEntity getByAltUserId(String altUserId) throws AtnaPersistenceException;

    ParticipantEntity get(ParticipantEntity other) throws AtnaPersistenceException;

    List<? extends ParticipantEntity> getByCode(ParticipantCodeEntity codeEntity) throws AtnaPersistenceException;

    List<? extends ParticipantEntity> getByUserName(String userName) throws AtnaPersistenceException;

    List<? extends ParticipantEntity> getAll() throws AtnaPersistenceException;

    List<? extends ParticipantEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    void save(ParticipantEntity ap, PersistencePolicies policies) throws AtnaPersistenceException;

    void delete(ParticipantEntity ap) throws AtnaPersistenceException;
}
