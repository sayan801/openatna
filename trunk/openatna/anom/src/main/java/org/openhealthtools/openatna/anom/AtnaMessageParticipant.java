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

/**
 * This class eraps an AtnaParticipant and provides message (Event)
 * specifics such as whether the user is a requestor and the network access
 * point being used by the participant.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 11, 2009: 11:32:37 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AtnaMessageParticipant {

    public AtnaParticipant getParticipant();

    public boolean isUserIsRequestor();

    public AtnaParticipant setUserIsRequestor(Boolean value);

    public String getNetworkAccessPointID();

    public AtnaParticipant setNetworkAccessPointID(String value);

    public NetworkAccessPoint getNetworkAccessPointType();

    public AtnaParticipant setNetworkAccessPointType(NetworkAccessPoint value);


}
