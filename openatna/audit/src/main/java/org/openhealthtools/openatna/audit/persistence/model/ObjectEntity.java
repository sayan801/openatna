package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;
import org.openhealthtools.openatna.audit.persistence.model.codes.ObjectIdTypeCodeEntity;

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

    private Set<DetailTypeEntity> objectDetailTypes = new HashSet<>();

    private Set<ObjectDescriptionEntity> objectDescriptions = new HashSet<>();

    public ObjectEntity() {
    }

    public ObjectEntity(String objectId, ObjectIdTypeCodeEntity objectIdTypeCode) {
        this.objectIdTypeCode = objectIdTypeCode;
        this.objectId = objectId;
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

    @ManyToOne(fetch = FetchType.EAGER)
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "objects_descriptions")
    public Set<ObjectDescriptionEntity> getObjectDescriptions() {
        return objectDescriptions;
    }

    public void setObjectDescriptions(Set<ObjectDescriptionEntity> objectDescriptions) {
        this.objectDescriptions = objectDescriptions;
    }

    public void addObjectDescription(ObjectDescriptionEntity description) {
        getObjectDescriptions().add(description);
    }

    @Column(length = 4000)
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectEntity)) {
            return false;
        }

        ObjectEntity that = (ObjectEntity) o;

        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) {
            return false;
        }
        if (objectName != null ? !objectName.equals(that.objectName) : that.objectName != null) {
            return false;
        }
        if (objectSensitivity != null ? !objectSensitivity.equals(that.objectSensitivity)
                : that.objectSensitivity != null) {
            return false;
        }
        if (objectTypeCode != null ? !objectTypeCode.equals(that.objectTypeCode)
                : that.objectTypeCode != null) {
            return false;
        }
        return objectTypeCodeRole != null ? objectTypeCodeRole.equals(that.objectTypeCodeRole) : that.objectTypeCodeRole == null;
    }

    @Override
    public int hashCode() {
        int result = (objectName != null ? objectName.hashCode() : 0);
        result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
        result = 31 * result + (objectTypeCode != null ? objectTypeCode.hashCode() : 0);
        result = 31 * result + (objectTypeCodeRole != null ? objectTypeCodeRole.hashCode() : 0);
        result = 31 * result + (objectSensitivity != null ? objectSensitivity.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "[" + getClass().getName() + " id=" + getId() + ", version=" + getVersion() + ", object id=" + getObjectId() +
                ", object name=" + getObjectName() + ", object type code=" + getObjectTypeCode() + ", object type code role=" +
                getObjectTypeCodeRole() + ", object id type code=" + getObjectIdTypeCode() + ", sensitivity=" + getObjectSensitivity() +
                ", object detail keys=" + getObjectDetailTypes() + ", object descriptions=" + getObjectDescriptions() + "]";
    }
}
