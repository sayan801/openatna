package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;

import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public interface CodeDao extends Dao {

    CodeEntity getById(Long id) throws AtnaPersistenceException;

    List<? extends CodeEntity> getByType(CodeEntity.CodeType type)
            throws AtnaPersistenceException;

    List<? extends CodeEntity> getByCode(String code) throws AtnaPersistenceException;

    List<? extends CodeEntity> getByCodeAndType(CodeEntity.CodeType type, String code) throws AtnaPersistenceException;

    List<? extends CodeEntity> getByCodeSystem(String codeSystem) throws AtnaPersistenceException;

    List<? extends CodeEntity> getByCodeSystemName(String codeSystemName) throws AtnaPersistenceException;

    CodeEntity getByCodeAndSystem(CodeEntity.CodeType type, String code, String codeSystem) throws AtnaPersistenceException;

    CodeEntity getByCodeAndSystemName(CodeEntity.CodeType type, String code, String codeSystemName) throws AtnaPersistenceException;

    CodeEntity getByCodeAndSystemAndSystemName(CodeEntity.CodeType type, String code, String codeSystem, String codeSystemName) throws AtnaPersistenceException;

    List<? extends CodeEntity> getBySystemAndType(String codeSystem, CodeEntity.CodeType type) throws AtnaPersistenceException;

    List<? extends CodeEntity> getBySystemNameAndType(String codeSystemName, CodeEntity.CodeType type) throws AtnaPersistenceException;

    List<? extends CodeEntity> getAll() throws AtnaPersistenceException;

    List<? extends CodeEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    boolean save(CodeEntity code, PersistencePolicies policies) throws AtnaPersistenceException;

    void delete(CodeEntity code) throws AtnaPersistenceException;

    CodeEntity get(CodeEntity code) throws AtnaPersistenceException;

    CodeEntity find(CodeEntity code) throws AtnaPersistenceException;
}
