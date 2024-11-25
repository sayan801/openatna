package org.openhealthtools.openatna.audit.persistence.model;

import javax.persistence.*;

/**
 * @author Andrew Harrison
 * @version $Revision:$
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // @GenericGenerator(name = "native", strategy = "native")
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

        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public String toString() {
        return getClass().getName() +
                "[" +
                "id=" +
                getId() +
                ", version=" +
                getVersion() +
                ", type=" +
                getType() +
                "]";
    }
}
