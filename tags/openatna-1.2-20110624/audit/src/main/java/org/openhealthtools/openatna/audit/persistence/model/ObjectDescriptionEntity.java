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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 7, 2010: 11:48:17 PM
 */

@Entity
@Table(name = "object_descriptions")
public class ObjectDescriptionEntity extends PersistentEntity {

    private Long id;
    private Integer version;
    private String mppsUids = "";
    private String accessionNumbers = "";
    private Set<SopClassEntity> sopClasses = new HashSet<SopClassEntity>();


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

    public String getMppsUids() {
        return mppsUids;
    }

    public void setMppsUids(String mppsUids) {
        this.mppsUids = mppsUids;
    }

    public List<String> mppsUidsAsList() {
        String uids = getMppsUids();
        String[] vals = uids.split(" ");
        List<String> ret = new ArrayList<String>();
        for (String val : vals) {
            if (val.length() > 0) {
                ret.add(val);
            }
        }
        return ret;
    }

    public void addMppsUid(String uid) {
        if (getMppsUids().length() == 0) {
            setMppsUids(uid);
        } else {
            setMppsUids(getMppsUids() + " " + uid);
        }
    }

    public List<String> accessionNumbersAsList() {
        String numbers = getAccessionNumbers();
        String[] vals = numbers.split(" ");
        List<String> ret = new ArrayList<String>();
        for (String val : vals) {
            if (val.length() > 0) {
                ret.add(val);
            }
        }
        return ret;
    }

    public void addAccessionNumber(String num) {
        if (getAccessionNumbers().length() == 0) {
            setAccessionNumbers(num);
        } else {
            setAccessionNumbers(getAccessionNumbers() + " " + num);
        }
    }

    public String getAccessionNumbers() {
        return accessionNumbers;
    }

    public void setAccessionNumbers(String accessionNumbers) {
        this.accessionNumbers = accessionNumbers;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "descriptions_sop_classes")
    public Set<SopClassEntity> getSopClasses() {
        return sopClasses;
    }

    public void setSopClasses(Set<SopClassEntity> sopClasses) {
        this.sopClasses = sopClasses;
    }

    public void addSopClass(SopClassEntity sce) {
        getSopClasses().add(sce);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectDescriptionEntity that = (ObjectDescriptionEntity) o;

        if (accessionNumbers != null ? !accessionNumbers.equals(that.accessionNumbers) : that.accessionNumbers != null) {
            return false;
        }
        if (mppsUids != null ? !mppsUids.equals(that.mppsUids) : that.mppsUids != null) {
            return false;
        }

        return true;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", version=")
                .append(getVersion())
                .append(", mppsUids=")
                .append(getMppsUids())
                .append(", accessionNumbers=")
                .append(getAccessionNumbers())
                .append(", SOP Classes=")
                .append(getSopClasses())
                .append("]")
                .toString();
    }

    @Override
    public int hashCode() {
        int result = mppsUids != null ? mppsUids.hashCode() : 0;
        result = 31 * result + (accessionNumbers != null ? accessionNumbers.hashCode() : 0);
        return result;
    }
}
