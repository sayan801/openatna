package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "object_details")
public class ObjectDetailEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private String type;
    private byte[] value;

    public ObjectDetailEntity() {
        super();
    }

    public ObjectDetailEntity(String type, byte[] value) {
        this.type = type;
        this.value = value;
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

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    @Lob
    public byte[] getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     */
    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectDetailEntity)) {
            return false;
        }

        ObjectDetailEntity that = (ObjectDetailEntity) o;

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        return value != null ? Arrays.equals(value, that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? Arrays.hashCode(value) : 0);
        return result;
    }

    public String toString() {
        return "[" + getClass().getName() + " id=" + getId() + ", type=" + getType() + ", value=" + new String(getValue()) + "]";
    }
}
