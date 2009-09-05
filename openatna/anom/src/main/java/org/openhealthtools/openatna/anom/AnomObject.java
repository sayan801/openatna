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
 * ParticipantObject
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AnomObject {

    public AnomCode getObjectIDTypeCode();

    public AnomObject setObjectIDTypeCode(AnomCode value);

    public String getObjectName();

    public AnomObject setObjectName(String value);

    public byte[] getObjectQuery();

    public AnomObject setObjectQuery(byte[] value);

    public List<AnomObjectDetail> getObjectDetails();

    public AnomObject addObjectDetail(AnomObjectDetail detail);

    public AnomObject removeObjectDetail(AnomObjectDetail detail);

    public String getObjectID();

    public AnomObject setObjectID(String value);

    public ObjectType getObjectTypeCode();

    public AnomObject setObjectTypeCode(ObjectType value);

    public ObjectTypeCodeRole getObjectTypeCodeRole();

    public AnomObject setObjectTypeCodeRole(ObjectTypeCodeRole value);

    public ObjectDataLifecycle getObjectDataLifeCycle();

    public AnomObject setObjectDataLifeCycle(ObjectDataLifecycle value);

    public String getObjectSensitivity();

    public AnomObject setObjectSensitivity(String value);


}
