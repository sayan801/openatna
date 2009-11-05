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

package org.openhealthtools.openatna.audit.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "message_objects")
public class MessageObjectEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private ObjectEntity object;
    private byte[] objectQuery;
    private Short objectDataLifeCycle;

    private Set<ObjectDetailEntity> details = new HashSet<ObjectDetailEntity>();

    public MessageObjectEntity() {
    }

    public MessageObjectEntity(ObjectEntity object) {
        setObject(object);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public ObjectEntity getObject() {
        return object;
    }

    public void setObject(ObjectEntity object) {
        this.object = object;
    }


    public byte[] getObjectQuery() {
        return objectQuery;
    }

    public void setObjectQuery(byte[] objectQuery) {
        this.objectQuery = objectQuery;
    }

    public Short getObjectDataLifeCycle() {
        return objectDataLifeCycle;
    }

    public void setObjectDataLifeCycle(Short objectDataLifeCycle) {
        this.objectDataLifeCycle = objectDataLifeCycle;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public Set<ObjectDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(Set<ObjectDetailEntity> details) {
        this.details = details;
    }

    public void addObjectDetail(ObjectDetailEntity detail) {
        getDetails().add(detail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageObjectEntity)) return false;

        MessageObjectEntity that = (MessageObjectEntity) o;

        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        if (objectDataLifeCycle != null ? !objectDataLifeCycle.equals(that.objectDataLifeCycle) : that.objectDataLifeCycle != null) return false;
        if (objectQuery != null ? !objectQuery.equals(that.objectQuery) : that.objectQuery != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (objectQuery != null ? objectQuery.hashCode() : 0);
        result = 31 * result + (objectDataLifeCycle != null ? objectDataLifeCycle.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", data life cycle=")
                .append(getObjectDataLifeCycle())
                .append(", query=")
                .append(getObjectQuery())
                .append(", object=")
                .append(getObject())
                .append(", details=")
                .append(getDetails())
                .append("]")
                .toString();
    }
}