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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class wraps an AtnaObject and provides message specific
 * details, specifically an object query, lifecycle, and a list of object details
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 11, 2009: 11:32:22 PM
 * @date $Date:$ modified by $Author:$
 */
public class AtnaMessageObject implements Serializable {

    private static final long serialVersionUID = -7234777344592455537L;

    private AtnaObject object;
    private byte[] objectQuery;
    private List<AtnaObjectDetail> objectDetails = new ArrayList<AtnaObjectDetail>();
    private ObjectDataLifecycle objectDataLifeCycle;

    public AtnaMessageObject(AtnaObject object) {
        this.object = object;
    }

    public AtnaObject getObject() {
        return object;
    }

    public void setObject(AtnaObject object) {
        this.object = object;
    }

    public byte[] getObjectQuery() {
        return objectQuery;
    }

    public AtnaMessageObject setObjectQuery(byte[] value) {
        this.objectQuery = value;
        return this;
    }

    public List<AtnaObjectDetail> getObjectDetails() {
        return new ArrayList<AtnaObjectDetail>(objectDetails);
    }

    /**
     * object details are not mapped uniquely to their type
     *
     * @param type
     * @return
     */
    public List<AtnaObjectDetail> getObjectDetails(String type) {
        ArrayList<AtnaObjectDetail> ret = new ArrayList<AtnaObjectDetail>();
        for (AtnaObjectDetail objectDetail : objectDetails) {
            if (objectDetail.getType().equals(type)) {
                ret.add(objectDetail);
            }
        }
        return ret;
    }

    public AtnaMessageObject addObjectDetail(AtnaObjectDetail detail) {
        objectDetails.add(detail);
        return this;
    }

    public AtnaMessageObject removeObjectDetail(AtnaObjectDetail detail) {
        objectDetails.remove(detail);
        return this;

    }

    public ObjectDataLifecycle getObjectDataLifeCycle() {
        return objectDataLifeCycle;
    }

    public AtnaMessageObject setObjectDataLifeCycle(ObjectDataLifecycle value) {
        this.objectDataLifeCycle = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaMessageObject)) {
            return false;
        }

        AtnaMessageObject that = (AtnaMessageObject) o;

        if (object != null ? !object.equals(that.object) : that.object != null) {
            return false;
        }
        if (objectDataLifeCycle != that.objectDataLifeCycle) {
            return false;
        }
        if (objectDetails != null ? !objectDetails.equals(that.objectDetails) : that.objectDetails != null) {
            return false;
        }
        if (!Arrays.equals(objectQuery, that.objectQuery)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (objectQuery != null ? Arrays.hashCode(objectQuery) : 0);
        result = 31 * result + (objectDetails != null ? objectDetails.hashCode() : 0);
        result = 31 * result + (objectDataLifeCycle != null ? objectDataLifeCycle.hashCode() : 0);
        return result;
    }

    public String toString() {
        byte[] bytes = getObjectQuery();
        if (bytes == null) {
            bytes = new byte[0];
        }
        return new StringBuilder().append("[")
                .append(getClass().getName())
                .append(" object=")
                .append(getObject())
                .append(" data life cycle=")
                .append(getObjectDataLifeCycle())
                .append(" query=")
                .append(new String(bytes))
                .append(" details=")
                .append(getObjectDetails())
                .append("]")
                .toString();
    }
}
