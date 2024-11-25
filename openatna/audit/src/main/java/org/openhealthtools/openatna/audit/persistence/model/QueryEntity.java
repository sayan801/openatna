package org.openhealthtools.openatna.audit.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 */
public class QueryEntity extends PersistentEntity {

    private Long id;
    private Integer version;
    private String name;
    private String query;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QueryEntity that = (QueryEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return query != null ? query.equals(that.query) : that.query == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "[" + getClass().getName() + " id=" + getId() + ", version=" + getVersion() + ", name=" + getName() + ", query=" + getQuery() + "]";
    }
}
