package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;
import org.openhealthtools.openatna.audit.persistence.model.codes.SourceCodeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The sourceId and enterpriseSiteId only, are used to determine equality
 */
@Entity
@Table(name = "sources")
public class SourceEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private Integer version;

    private Set<SourceCodeEntity> sourceTypeCodes = new HashSet<>();
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    //@GenericGenerator(name = "native", strategy = "native")
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
     * {@link String }
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
     * {@link String }
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
        return sourceId != null ? sourceId.equals(that.sourceId) : that.sourceId == null;
    }

    @Override
    public int hashCode() {
        int result = (enterpriseSiteId != null ? enterpriseSiteId.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "[" + getClass().getName() +
                " id=" +
                getId() +
                ", version=" +
                getVersion() +
                ", sourceId=" +
                getSourceId() +
                ", enterprise site ID=" +
                getEnterpriseSiteId() +
                ", source type code=" +
                getSourceTypeCodes() +
                "]";
    }
}
