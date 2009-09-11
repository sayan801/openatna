/*
 * Copyright (c) 2009 University of Cardiff.
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

package org.openhealthtools.openatna.persistence.model;

import org.openhealthtools.openatna.persistence.model.codes.ParticipantCodeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 4, 2009: 9:23:30 AM
 * @date $Date:$ modified by $Author:$
 */

@Entity
@Table(name = "participants")
public class ParticipantEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private Integer version;
    private String userId;
    private String alternativeUserId;
    private String userName;
    private Set<ParticipantCodeEntity> codes = new HashSet<ParticipantCodeEntity>();

    public ParticipantEntity() {
    }

    public ParticipantEntity(String userId) {
        this.userId = userId;
    }

    public ParticipantEntity(String userId, ParticipantCodeEntity code) {
        this.userId = userId;
        addCode(code);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlternativeUserId() {
        return alternativeUserId;
    }

    public void setAlternativeUserId(String alternativeUserId) {
        this.alternativeUserId = alternativeUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "participants_to_codes",
            joinColumns = {@JoinColumn(name = "participant")},
            inverseJoinColumns = @JoinColumn(name = "code")
    )
    public Set<ParticipantCodeEntity> getCodes() {
        return codes;
    }

    public void setCodes(Set<ParticipantCodeEntity> codes) {
        this.codes = codes;
    }

    public void addCode(ParticipantCodeEntity entity) {
        getCodes().add(entity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipantEntity)) return false;

        ParticipantEntity that = (ParticipantEntity) o;

        if (alternativeUserId != null ? !alternativeUserId.equals(that.alternativeUserId) : that.alternativeUserId != null) return false;
        if (codes != null ? !codes.equals(that.codes) : that.codes != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (alternativeUserId != null ? alternativeUserId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (codes != null ? codes.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", version=")
                .append(getVersion())
                .append(", userID=")
                .append(getUserId())
                .append(", user name=")
                .append(getUserName())
                .append(", alternative user id=")
                .append(getAlternativeUserId())
                .append(", codes=")
                .append(getCodes())
                .append("]")
                .toString();
    }
}
