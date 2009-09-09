/*
 * Copyright (c) 2009 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.persistence.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openhealthtools.openatna.persistence.dao.ObjectDetailDao;
import org.openhealthtools.openatna.persistence.model.ObjectDetailEntity;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 12:39:10 PM
 * @date $Date:$ modified by $Author:$
 */

public class HibernateObjectDetailDao extends AbstractHibernateDao<ObjectDetailEntity> implements ObjectDetailDao {

    public HibernateObjectDetailDao(SessionFactory sessionFactory) {
        super(ObjectDetailEntity.class, sessionFactory);
    }

    public ObjectDetailEntity getById(Long id) {
        return get(id);
    }

    public List<? extends ObjectDetailEntity> getByType(String type) {
        return list(criteria().add(Restrictions.eq("type", type)));
    }

    public void save(ObjectDetailEntity detail) {
        currentSession().saveOrUpdate(detail);
    }

    public void delete(ObjectDetailEntity detail) {
        currentSession().delete(detail);
    }
}
