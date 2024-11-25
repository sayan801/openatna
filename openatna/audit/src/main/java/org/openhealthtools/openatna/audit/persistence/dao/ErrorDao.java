package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;

import java.util.Date;
import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
public interface ErrorDao extends Dao {

    ErrorEntity getById(Long id) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getAll() throws AtnaPersistenceException;

    List<? extends ErrorEntity> getAll(int offset, int amount) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getBySourceIp(String ip) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getAfter(Date date) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getAfter(String ip, Date date) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getBefore(Date date) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getBefore(String ip, Date date) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getBetween(Date first, Date second) throws AtnaPersistenceException;

    List<? extends ErrorEntity> getBetween(String ip, Date first, Date second) throws AtnaPersistenceException;

    void save(ErrorEntity entity) throws AtnaPersistenceException;
}
