/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */

package org.openhealthtools.openatna.anom;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Active Participant
 */
public class AtnaParticipant implements Serializable {

    private static final long serialVersionUID = -3946094452860332441L;

    private final Set<AtnaCode> roleIdCodes = new HashSet<>();
    private String userId;
    private String alternativeUserId;
    private String userName;

    public AtnaParticipant(String userId) {
        this.userId = userId;
    }

    public List<AtnaCode> getRoleIDCodes() {
        return new ArrayList<>(roleIdCodes);
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
        return userName != null ? userName.equals(that.userName) : that.userName == null;
    }

    @Override
    public int hashCode() {
        int result = roleIdCodes != null ? roleIdCodes.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (alternativeUserId != null ? alternativeUserId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("roleIdCodes", roleIdCodes)
                .append("userId", userId)
                .append("alternativeUserId", alternativeUserId)
                .append("userName", userName)
                .toString();
    }
}
