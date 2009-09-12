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


package org.openhealthtools.openatna.persistence.model;

import org.openhealthtools.openatna.persistence.model.codes.ObjectIdTypeCodeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "objects")
public class ObjectEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private Integer version;

    private ObjectIdTypeCodeEntity objectIdTypeCode;
    private String objectName;

    private String objectId;
    private Short objectTypeCode;
    private Short objectTypeCodeRole;
    private String objectSensitivity;

    private Set<DetailTypeEntity> objectDetailTypes = new HashSet<DetailTypeEntity>();

    public ObjectEntity() {
    }

    public ObjectEntity(String objectId, ObjectIdTypeCodeEntity objectIdTypeCode) {
        this.objectIdTypeCode = objectIdTypeCode;
        this.objectId = objectId;
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

    @ManyToOne
    public ObjectIdTypeCodeEntity getObjectIdTypeCode() {
        return objectIdTypeCode;
    }

    public void setObjectIdTypeCode(ObjectIdTypeCodeEntity objectIdTypeCodeEntity) {
        this.objectIdTypeCode = objectIdTypeCodeEntity;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @OneToMany
    public Set<DetailTypeEntity> getObjectDetailTypes() {
        return objectDetailTypes;
    }

    public void setObjectDetailTypes(Set<DetailTypeEntity> objectDetailTypes) {
        this.objectDetailTypes = objectDetailTypes;
    }

    public void addObjectDetailType(String key) {
        getObjectDetailTypes().add(new DetailTypeEntity(key));
    }

    public boolean containsDetailType(String key) {
        DetailTypeEntity te = new DetailTypeEntity(key);
        return getObjectDetailTypes().contains(te);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Short getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(Short objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public Short getObjectTypeCodeRole() {
        return objectTypeCodeRole;
    }

    public void setObjectTypeCodeRole(Short objectTypeCodeRole) {
        this.objectTypeCodeRole = objectTypeCodeRole;
    }

    public String getObjectSensitivity() {
        return objectSensitivity;
    }

    public void setObjectSensitivity(String objectSensitivity) {
        this.objectSensitivity = objectSensitivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectEntity)) return false;

        ObjectEntity that = (ObjectEntity) o;

        if (objectDetailTypes != null ? !objectDetailTypes.equals(that.objectDetailTypes) : that.objectDetailTypes != null) return false;
        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) return false;
        if (objectIdTypeCode != null ? !objectIdTypeCode.equals(that.objectIdTypeCode) : that.objectIdTypeCode != null) return false;
        if (objectName != null ? !objectName.equals(that.objectName) : that.objectName != null) return false;
        if (objectSensitivity != null ? !objectSensitivity.equals(that.objectSensitivity) : that.objectSensitivity != null) return false;
        if (objectTypeCode != null ? !objectTypeCode.equals(that.objectTypeCode) : that.objectTypeCode != null) return false;
        if (objectTypeCodeRole != null ? !objectTypeCodeRole.equals(that.objectTypeCodeRole) : that.objectTypeCodeRole != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = objectIdTypeCode != null ? objectIdTypeCode.hashCode() : 0;
        result = 31 * result + (objectName != null ? objectName.hashCode() : 0);
        result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
        result = 31 * result + (objectTypeCode != null ? objectTypeCode.hashCode() : 0);
        result = 31 * result + (objectTypeCodeRole != null ? objectTypeCodeRole.hashCode() : 0);
        result = 31 * result + (objectSensitivity != null ? objectSensitivity.hashCode() : 0);
        result = 31 * result + (objectDetailTypes != null ? objectDetailTypes.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", version=")
                .append(getVersion())
                .append(", object id=")
                .append(getObjectId())
                .append(", object name=")
                .append(getObjectName())
                .append(", object type code=")
                .append(getObjectTypeCode())
                .append(", object type code role=")
                .append(getObjectTypeCodeRole())
                .append(", object id type code=")
                .append(getObjectIdTypeCode())
                .append(", sensitivity=")
                .append(getObjectSensitivity())
                .append(", object detail keys=")
                .append(getObjectDetailTypes())
                .append("]")
                .toString();
    }
}
