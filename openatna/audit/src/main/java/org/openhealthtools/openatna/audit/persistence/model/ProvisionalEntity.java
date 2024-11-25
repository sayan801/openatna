package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
@Entity
@Table(name = "provisional_messages")
public class ProvisionalEntity extends PersistentEntity {

    private Long id;
    private Integer version;
    private byte[] content;


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

    @Lob
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProvisionalEntity that = (ProvisionalEntity) o;

        return Arrays.equals(content, that.content);
    }

    public String toString() {
        try {
            return new String(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            assert (false);
        }
        return getClass().getName();
    }

    @Override
    public int hashCode() {
        return content != null ? Arrays.hashCode(content) : 0;
    }
}
