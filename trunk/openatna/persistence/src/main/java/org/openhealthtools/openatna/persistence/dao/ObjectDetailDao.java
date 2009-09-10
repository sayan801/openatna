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

package org.openhealthtools.openatna.persistence.dao;

import org.openhealthtools.openatna.persistence.model.ObjectDetailEntity;

import java.util.List;

/**
 * NOTE: how this is handled is still under consideration.
 * Currently details are defined by the domain and must be matched
 * by the audit message. This may be a wrong interpretation. It could
 * be that details are a way for messages to add arbitrary context data
 * to a message, and therefore these should not be mapped (and validated against)
 * when storing the message.
 * TBD...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 12:38:00 PM
 * @date $Date:$ modified by $Author:$
 */
public interface ObjectDetailDao {

    public ObjectDetailEntity getById(Long id);

    public List<? extends ObjectDetailEntity> getByType(String type);

    public void save(ObjectDetailEntity detail);

    public void delete(ObjectDetailEntity detail);
}
