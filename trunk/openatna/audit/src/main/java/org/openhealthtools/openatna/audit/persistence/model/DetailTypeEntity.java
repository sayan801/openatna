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

package org.openhealthtools.openatna.audit.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 11, 2009: 8:11:28 PM
 * @date $Date:$ modified by $Author:$
 */


@Entity
@Table(name = "detail_types")
public class DetailTypeEntity extends PersistentEntity {

    private Long id;
    private Integer version;
    private String type;

    public DetailTypeEntity() {
    }

    public DetailTypeEntity(String type) {
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailTypeEntity)) {
            return false;
        }

        DetailTypeEntity that = (DetailTypeEntity) o;

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public String toString() {
        return new StringBuilder(getClass().getName())
                .append("[")
                .append("id=")
                .append(getId())
                .append(", version=")
                .append(getVersion())
                .append(", type=")
                .append(getType())
                .append("]")
                .toString();
    }
}
