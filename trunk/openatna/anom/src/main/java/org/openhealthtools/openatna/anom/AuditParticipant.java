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
 * Active Participant
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AuditParticipant {

    public List<CodedValue> getRoleIDCodes();

    public AuditParticipant addRoleIDCode(CodedValue value);

    public AuditParticipant removeRoleIDCode(CodedValue value);

    public String getUserID();

    public AuditParticipant setUserID(String value);

    public String getAlternativeUserID();

    public AuditParticipant setAlternativeUserID(String value);

    public String getUserName();

    public AuditParticipant setUserName(String value);

    public boolean isUserIsRequestor();

    public AuditParticipant setUserIsRequestor(Boolean value);

    public String getNetworkAccessPointID();

    public AuditParticipant setNetworkAccessPointID(String value);

    public NetworkAccessPoint getNetworkAccessPointTypeCode();

    public AuditParticipant setNetworkAccessPointTypeCode(NetworkAccessPoint value);

}
