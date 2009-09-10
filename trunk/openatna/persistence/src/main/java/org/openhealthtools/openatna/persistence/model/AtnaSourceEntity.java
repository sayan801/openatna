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

import javax.persistence.*;


@Entity
@Table(name = "atna_sources")
public class AtnaSourceEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private SourceEntity source;

    public AtnaSourceEntity() {
    }

    public AtnaSourceEntity(String sourceId) {
        this.source = new SourceEntity(sourceId);
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
    public SourceEntity getSource() {
        return source;
    }

    public void setSource(SourceEntity source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtnaSourceEntity)) return false;

        AtnaSourceEntity that = (AtnaSourceEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", source=")
                .append(getSource())
                .append("]")
                .toString();
    }

}