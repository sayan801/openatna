package org.openhealthtools.openatna.audit.persistence.model;

import java.io.Serializable;

/**
 * Force entities to implement these methods and be serializable
 */
public abstract class PersistentEntity implements Serializable {

    private static final long serialVersionUID = -4028037605412482144L;

    public abstract Long getId();

    public abstract int hashCode();

    public abstract boolean equals(Object other);

    public abstract String toString();
}
