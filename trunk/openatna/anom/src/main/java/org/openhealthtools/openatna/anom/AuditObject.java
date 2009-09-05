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
 *
 * ParticipantObject
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AuditObject {

    public CodedValue getObjectIDTypeCode();

    public AuditObject setObjectIDTypeCode(CodedValue value);

    public String getObjectName();

    public AuditObject setObjectName(String value);

    public byte[] getObjectQuery();

    public AuditObject setObjectQuery(byte[] value);

    public List<AuditObjectDetail> getObjectDetails();

    public AuditObject addObjectDetail(AuditObjectDetail detail);

    public AuditObject removeObjectDetail(AuditObjectDetail detail);

    public String getObjectID();

    public AuditObject setObjectID(String value);

    public ObjectType gettObjectTypeCode();

    public AuditObject setObjectTypeCode(ObjectType value);

    public ObjectTypeCodeRole getObjectTypeCodeRole();

    public AuditObject setObjectTypeCodeRole(ObjectTypeCodeRole value);

    public ObjectLifecycle getObjectDataLifeCycle();

    public AuditObject setObjectDataLifeCycle(ObjectLifecycle value);

    public String getObjectSensitivity();

    public AuditObject setObjectSensitivity(String value);


}
