package org.openhealthtools.openatna.audit.persistence.dao;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.model.PersistentEntity;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
public interface EntityDao {

    List<? extends PersistentEntity> query(String query) throws AtnaPersistenceException;

    String[] getSupportedQueryDialects();
}
