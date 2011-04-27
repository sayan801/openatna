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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;


/**
 * The sourceId and enterpriseSiteId only, are used to determine equality
 */
@Entity
@Table(name = "sources")
public class SourceEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private Integer version;

    private Set<SourceCodeEntity> sourceTypeCodes = new HashSet<SourceCodeEntity>();
    private String enterpriseSiteId;
    private String sourceId;

    public SourceEntity() {
    }

    public SourceEntity(String sourceId) {
        this.sourceId = sourceId;
    }

    public SourceEntity(String sourceId, SourceCodeEntity code) {
        this.sourceId = sourceId;
        addSourceTypeCode(code);
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sources_codes")
    public Set<SourceCodeEntity> getSourceTypeCodes() {
        return sourceTypeCodes;
    }

    public void setSourceTypeCodes(Set<SourceCodeEntity> sourceTypeCodeEntities) {
        this.sourceTypeCodes = sourceTypeCodeEntities;
    }

    public void addSourceTypeCode(SourceCodeEntity entity) {
        getSourceTypeCodes().add(entity);
    }

    /**
     * Gets the value of the auditEnterpriseSiteID property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEnterpriseSiteId() {
        return enterpriseSiteId;
    }

    /**
     * Sets the value of the auditEnterpriseSiteID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEnterpriseSiteId(String value) {
        this.enterpriseSiteId = value;
    }

    /**
     * Gets the value of the auditSourceID property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Sets the value of the auditSourceID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSourceId(String value) {
        this.sourceId = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceEntity)) {
            return false;
        }

        SourceEntity that = (SourceEntity) o;

        if (enterpriseSiteId != null ? !enterpriseSiteId.equals(that.enterpriseSiteId)
                : that.enterpriseSiteId != null) {
            return false;
        }
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (enterpriseSiteId != null ? enterpriseSiteId.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", version=")
                .append(getVersion())
                .append(", sourceId=")
                .append(getSourceId())
                .append(", enterprise site ID=")
                .append(getEnterpriseSiteId())
                .append(", source type code=")
                .append(getSourceTypeCodes())
                .append("]")
                .toString();
    }


}
