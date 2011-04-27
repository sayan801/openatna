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

package org.openhealthtools.openatna.anom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 7, 2010: 11:12:23 PM
 */

public class SopClass {

    private String uid;
    private int numberOfInstances = 0;
    private Set<String> instanceUids = new HashSet<String>();

    public SopClass(String uid, int numberOfInstances) {
        this.uid = uid;
        this.numberOfInstances = numberOfInstances;
    }

    public SopClass() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getNumberOfInstances() {
        return numberOfInstances;
    }

    public void setNumberOfInstances(int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
    }

    public void addInstanceUid(String uid) {
        instanceUids.add(uid);
    }

    public List<String> getInstanceUids() {
        return new ArrayList<String>(instanceUids);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SopClass sopClass = (SopClass) o;

        if (numberOfInstances != sopClass.numberOfInstances) {
            return false;
        }
        if (!instanceUids.equals(sopClass.instanceUids)) {
            return false;
        }
        if (uid != null ? !uid.equals(sopClass.uid) : sopClass.uid != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + numberOfInstances;
        result = 31 * result + instanceUids.hashCode();
        return result;
    }
}
