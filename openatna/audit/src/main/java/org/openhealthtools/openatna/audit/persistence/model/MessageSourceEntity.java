package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
@Entity
@Table(name = "message_sources")
public class MessageSourceEntity extends PersistentEntity {

    private static final long serialVersionUID = -1L;

    private Long id;
    private SourceEntity source;

    public MessageSourceEntity() {
    }

    public MessageSourceEntity(String sourceId) {
        setSource(new SourceEntity(sourceId));
    }

    public MessageSourceEntity(SourceEntity source) {
        setSource(source);
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
    public SourceEntity getSource() {
        return source;
    }

    public void setSource(SourceEntity source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageSourceEntity)) {
            return false;
        }

        MessageSourceEntity that = (MessageSourceEntity) o;

        return getSource() != null ? getSource().equals(that.getSource()) : that.getSource() == null;
    }

    @Override
    public int hashCode() {
        return getSource() != null ? getSource().hashCode() : 0;
    }

    public String toString() {
        return "[" + getClass().getName() +
                " id=" +
                getId() +
                ", source=" +
                getSource() +
                "]";
    }
}
