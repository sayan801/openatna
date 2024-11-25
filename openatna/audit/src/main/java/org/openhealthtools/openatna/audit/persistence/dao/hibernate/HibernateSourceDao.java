package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.dao.SourceDao;
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.CodesUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
@Transactional(rollbackFor = AtnaPersistenceException.class)
public class HibernateSourceDao extends AbstractHibernateDao<SourceEntity> implements SourceDao {

    public HibernateSourceDao(SessionFactory sessionFactory) {
        super(SourceEntity.class, sessionFactory);
    }

    public SourceEntity getById(Long id) {
        return get(id);
    }

    public List<? extends SourceEntity> getBySourceId(String id) {
        return list(criteria().add(Restrictions.eq("sourceId", id)));
    }

    public SourceEntity getByEnterpriseSiteId(String id) {
        return uniqueResult(criteria().add(Restrictions.eq("enterpriseSiteId", id)));
    }

    public SourceEntity get(SourceEntity other) {

        Criteria c = criteria();
        c.add(Restrictions.eq("sourceId", other.getSourceId()));
        if (other.getEnterpriseSiteId() != null) {
            c.add(Restrictions.eq("enterpriseSiteId", other.getEnterpriseSiteId()));
        } else {
            c.add(Restrictions.isNull("enterpriseSiteId"));
        }
        List<? extends SourceEntity> ret = list(c);
        if (ret == null || ret.isEmpty()) {
            return null;
        }

        for (SourceEntity sourceEntity : ret) {
            if (CodesUtils.equivalent(sourceEntity.getSourceTypeCodes(), other.getSourceTypeCodes())) {
                return sourceEntity;
            }
        }
        return null;
    }

    public List<? extends SourceEntity> getByCode(SourceCodeEntity codeEntity) {

        return list(criteria().createCriteria("sourceTypeCodes")
                .add(Restrictions.eq("code", codeEntity.getCode()))
                .add(Restrictions.eq("codeSystem", codeEntity.getCodeSystem()))
                .add(Restrictions.eq("codeSystemName", codeEntity.getCodeSystemName())));
    }

    public List<? extends SourceEntity> getAll() throws AtnaPersistenceException {
        return all();
    }

    public List<? extends SourceEntity> getAll(int offset, int amount) throws AtnaPersistenceException {
        return all(offset, amount);
    }

    public void save(SourceEntity entity, PersistencePolicies policies) throws AtnaPersistenceException {

        Set<SourceCodeEntity> codes = entity.getSourceTypeCodes();
        if (!codes.isEmpty()) {
            CodeDao dao = AtnaFactory.codeDao();
            SourceCodeEntity[] arr = codes.toArray(new SourceCodeEntity[codes.size()]);
            for (int i = 0; i < arr.length; i++) {
                SourceCodeEntity code = arr[i];
                CodeEntity codeEnt = dao.get(code);
                if (codeEnt == null) {
                    if (policies.isAllowNewCodes()) {
                        dao.save(code, policies);
                    } else {
                        throw new AtnaPersistenceException(code.toString(),
                                AtnaPersistenceException.PersistenceError.NON_EXISTENT_CODE);
                    }
                } else {
                    if (codeEnt instanceof SourceCodeEntity) {
                        arr[i] = ((SourceCodeEntity) codeEnt);
                    } else {
                        throw new AtnaPersistenceException("code is defined but is of a different type.",
                                AtnaPersistenceException.PersistenceError.WRONG_CODE_TYPE);
                    }
                }
            }
            entity.setSourceTypeCodes(new HashSet<>(Arrays.asList(arr)));
        }

        if (entity.getVersion() == null) {
            // new one.
            SourceEntity existing = get(entity);
            if (existing != null) {
                if (policies.isErrorOnDuplicateInsert()) {
                    throw new AtnaPersistenceException(entity.toString(),
                            AtnaPersistenceException.PersistenceError.DUPLICATE_SOURCE);
                } else {
                    return;
                }
            }
        }
        currentSession().saveOrUpdate(entity);
    }

    public void delete(SourceEntity entity) {
        currentSession().delete(entity);
    }
}
