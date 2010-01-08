/*
 * Copyright (c) 2009 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.audit.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 7, 2010: 11:47:55 PM
 */

@Entity
@Table(name = "sop_classes")
public class SopClassEntity {


    private Long id;
    private Integer version;

    private String uid;
    private Integer numberOfInstances = 0;
    private String instanceUids = "";

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getNumberOfInstances() {
        return numberOfInstances;
    }

    public void setNumberOfInstances(Integer numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
    }

    public String getInstanceUids() {
        return instanceUids;
    }

    public void setInstanceUids(String instanceUids) {
        this.instanceUids = instanceUids;
    }

    public List<String> getInstanceUidsAsList() {
        String uids = getInstanceUids();
        String[] vals = uids.split(" ");
        List<String> ret = new ArrayList<String>();
        for (String val : vals) {
            if (val.length() > 0) {
                ret.add(val);
            }
        }
        return ret;
    }

    public void addInstanceUid(String uid) {
        if (instanceUids.length() == 0) {
            instanceUids = uid;
        } else {
            instanceUids += " " + uid;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SopClassEntity that = (SopClassEntity) o;

        if (instanceUids != null ? !instanceUids.equals(that.instanceUids) : that.instanceUids != null) {
            return false;
        }
        if (numberOfInstances != null ? !numberOfInstances.equals(that.numberOfInstances) : that.numberOfInstances != null) {
            return false;
        }
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (numberOfInstances != null ? numberOfInstances.hashCode() : 0);
        result = 31 * result + (instanceUids != null ? instanceUids.hashCode() : 0);
        return result;
    }
}
