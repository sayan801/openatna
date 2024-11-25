package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "message_objects")
public class MessageObjectEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private ObjectEntity object;
    private byte[] objectQuery;
    private Short objectDataLifeCycle;

    private Set<ObjectDetailEntity> details = new HashSet<>();

    public MessageObjectEntity() {
        super();
    }

    public MessageObjectEntity(ObjectEntity object) {
        setObject(object);
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

    @ManyToOne(fetch = FetchType.EAGER)
    public ObjectEntity getObject() {
        return object;
    }

    public void setObject(ObjectEntity object) {
        this.object = object;
    }

    @Lob
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
    @JoinTable(name = "mobjects_details")
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageObjectEntity)) {
            return false;
        }

        MessageObjectEntity that = (MessageObjectEntity) o;

        if (details != null ? !details.equals(that.details) : that.details != null) {
            return false;
        }
        if (object != null ? !object.equals(that.object) : that.object != null) {
            return false;
        }
        if (objectDataLifeCycle != null ? !objectDataLifeCycle.equals(that.objectDataLifeCycle)
                : that.objectDataLifeCycle != null) {
            return false;
        }
        if (objectQuery != null ? !Arrays.equals(objectQuery, that.objectQuery) : that.objectQuery != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (objectQuery != null ? Arrays.hashCode(objectQuery) : 0);
        result = 31 * result + (objectDataLifeCycle != null ? objectDataLifeCycle.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "[" + getClass().getName() +
                " id=" +
                getId() +
                ", data life cycle=" +
                getObjectDataLifeCycle() +
                ", query=" +
                String.valueOf(getObjectQuery()) +
                ", object=" +
                getObject() +
                ", details=" +
                getDetails() +
                "]";
    }
}
