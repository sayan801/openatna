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
 * @date Jan 7, 2010: 11:10:02 PM
 */

public class ObjectDescription {

    private Set<String> mppsUids = new HashSet<String>();
    private List<String> accessionNumbers = new ArrayList<String>();
    private List<SopClass> sopClasses = new ArrayList<SopClass>();

    public ObjectDescription() {

    }

    public List<String> getMppsUids() {
        return new ArrayList<String>(mppsUids);
    }

    public List<String> getAccessionNumbers() {
        return accessionNumbers;
    }

    public List<SopClass> getSopClasses() {
        return sopClasses;
    }

    public void addMppsUid(String uid) {
        mppsUids.add(uid);
    }

    public void removeMppsUid(String uid) {
        mppsUids.remove(uid);
    }

    public void addAccessionNumber(String number) {
        accessionNumbers.add(number);
    }

    public void addSopClass(SopClass sopClass) {
        sopClasses.add(sopClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectDescription that = (ObjectDescription) o;

        if (accessionNumbers != null ? !accessionNumbers.equals(that.accessionNumbers) : that.accessionNumbers != null) {
            return false;
        }
        if (mppsUids != null ? !mppsUids.equals(that.mppsUids) : that.mppsUids != null) {
            return false;
        }
        if (sopClasses != null ? !sopClasses.equals(that.sopClasses) : that.sopClasses != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mppsUids != null ? mppsUids.hashCode() : 0;
        result = 31 * result + (accessionNumbers != null ? accessionNumbers.hashCode() : 0);
        result = 31 * result + (sopClasses != null ? sopClasses.hashCode() : 0);
        return result;
    }
}
