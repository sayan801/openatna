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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 12, 2009: 1:25:16 PM
 * @date $Date:$ modified by $Author:$
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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

        if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getSource() != null ? getSource().hashCode() : 0;
    }


    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", source=")
                .append(getSource())
                .append("]")
                .toString();
    }


}
