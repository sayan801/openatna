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

package org.openhealthtools.openatna.anom;

import java.util.List;

/**
 * This class wraps an AtnaObject and provides message specific
 * details, specifically an object query, lifecycle, and a list of object details
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 11, 2009: 11:32:22 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AtnaMessageObject {

    public AtnaObject getObject();

    public byte[] getObjectQuery();

    public AtnaObject setObjectQuery(byte[] value);

    public List<AtnaObjectDetail> getObjectDetails();

    /**
     * object details are not mapped uniquely to their type
     *
     * @param type
     * @return
     */
    public List<AtnaObjectDetail> getObjectDetails(String type);

    public AtnaObject addObjectDetail(AtnaObjectDetail detail);

    public AtnaObject removeObjectDetail(AtnaObjectDetail detail);

    public ObjectDataLifecycle getObjectDataLifeCycle();

    public AtnaObject setObjectDataLifeCycle(ObjectDataLifecycle value);

}
