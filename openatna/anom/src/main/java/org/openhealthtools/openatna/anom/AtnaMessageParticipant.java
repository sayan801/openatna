/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.anom;

import java.io.Serializable;

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
public class AtnaMessageParticipant implements Serializable {

    private static final long serialVersionUID = -4687928894634995258L;

    private AtnaParticipant participant;
    private boolean userIsRequestor = true;
    private String networkAccessPointId;
    private NetworkAccessPoint networkAccessPointType;

    public AtnaMessageParticipant(AtnaParticipant participant) {
        this.participant = participant;
    }

    public AtnaParticipant getParticipant() {
        return participant;
    }

    public AtnaMessageParticipant setParticipant(AtnaParticipant participant) {
        this.participant = participant;
        return this;
    }

    public boolean isUserIsRequestor() {
        return userIsRequestor;
    }

    public AtnaMessageParticipant setUserIsRequestor(boolean userIsRequestor) {
        this.userIsRequestor = userIsRequestor;
        return this;
    }

    public String getNetworkAccessPointId() {
        return networkAccessPointId;
    }

    public AtnaMessageParticipant setNetworkAccessPointId(String networkAccessPointId) {
        this.networkAccessPointId = networkAccessPointId;
        return this;
    }

    public NetworkAccessPoint getNetworkAccessPointType() {
        return networkAccessPointType;
    }

    public AtnaMessageParticipant setNetworkAccessPointType(NetworkAccessPoint networkAccessPointType) {
        this.networkAccessPointType = networkAccessPointType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaMessageParticipant)) {
            return false;
        }

        AtnaMessageParticipant that = (AtnaMessageParticipant) o;

        if (userIsRequestor != that.userIsRequestor) {
            return false;
        }
        if (networkAccessPointId != null ? !networkAccessPointId.equals(that.networkAccessPointId) : that.networkAccessPointId != null) {
            return false;
        }
        if (networkAccessPointType != that.networkAccessPointType) {
            return false;
        }
        if (participant != null ? !participant.equals(that.participant) : that.participant != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = participant != null ? participant.hashCode() : 0;
        result = 31 * result + (userIsRequestor ? 1 : 0);
        result = 31 * result + (networkAccessPointId != null ? networkAccessPointId.hashCode() : 0);
        result = 31 * result + (networkAccessPointType != null ? networkAccessPointType.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder().append("[")
                .append(getClass().getName())
                .append(" participant=")
                .append(getParticipant())
                .append(" network access point id=")
                .append(getNetworkAccessPointId())
                .append(" network access point type=")
                .append(getNetworkAccessPointType())
                .append(" user is requestor=")
                .append(isUserIsRequestor())
                .append("]")
                .toString();
    }
}
