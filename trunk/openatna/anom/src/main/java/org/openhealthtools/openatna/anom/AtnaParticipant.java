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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Active Participant
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public class AtnaParticipant implements Serializable {

    private static final long serialVersionUID = -3946094452860332441L;

    private Set<AtnaCode> roleIdCodes = new HashSet<AtnaCode>();
    private String userId;
    private String alternativeUserId;
    private String userName;

    public AtnaParticipant(String userId) {
        this.userId = userId;
    }

    public List<AtnaCode> getRoleIDCodes() {
        return new ArrayList<AtnaCode>(roleIdCodes);
    }

    public AtnaParticipant addRoleIDCode(AtnaCode value) {
        roleIdCodes.add(value);
        return this;
    }

    public AtnaParticipant removeRoleIDCode(AtnaCode value) {
        roleIdCodes.remove(value);
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public AtnaParticipant setUserId(String value) {
        this.userId = value;
        return this;
    }

    public String getAlternativeUserId() {
        return alternativeUserId;
    }

    public AtnaParticipant setAlternativeUserId(String value) {
        this.alternativeUserId = value;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public AtnaParticipant setUserName(String value) {
        this.userName = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaParticipant)) {
            return false;
        }

        AtnaParticipant that = (AtnaParticipant) o;

        if (alternativeUserId != null ? !alternativeUserId.equals(that.alternativeUserId) : that.alternativeUserId != null) {
            return false;
        }
        if (roleIdCodes != null ? !roleIdCodes.equals(that.roleIdCodes) : that.roleIdCodes != null) {
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
            return false;
        }
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleIdCodes != null ? roleIdCodes.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (alternativeUserId != null ? alternativeUserId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder().append("[")
                .append(getClass().getName())
                .append(" user id=")
                .append(getUserId())
                .append(" user name=")
                .append(getUserName())
                .append(" alt user id=")
                .append(getAlternativeUserId())
                .append(" role id codes=")
                .append(getRoleIDCodes())
                .append("]")
                .toString();
    }
}
