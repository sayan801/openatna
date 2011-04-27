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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 8, 2010: 9:48:39 AM
 */

@Entity
@Table(name = "provisional_messages")
public class ProvisionalEntity extends PersistentEntity {

    private Long id;
    private Integer version;
    private byte[] content;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

        if (!Arrays.equals(content, that.content)) {
            return false;
        }

        return true;
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
