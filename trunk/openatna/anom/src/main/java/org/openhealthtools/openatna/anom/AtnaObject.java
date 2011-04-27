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
import java.util.List;

/**
 * The resource to which the message refers; the resource being acted upon.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public class AtnaObject implements Serializable {

    private static final long serialVersionUID = 7352385693414253878L;

    private AtnaCode objectIdTypeCode;
    private String objectName;
    private List<String> objectDetailTypes = new ArrayList<String>();
    private ObjectType objectTypeCode;
    private String objectId;
    private ObjectTypeCodeRole objectTypeCodeRole;
    private String objectSensitivity;
    private List<ObjectDescription> descriptions = new ArrayList<ObjectDescription>();

    public AtnaObject(String objectId, AtnaCode objectIdTypeCode) {
        this.objectId = objectId;
        this.objectIdTypeCode = objectIdTypeCode;
    }

    public AtnaCode getObjectIdTypeCode() {
        return objectIdTypeCode;
    }

    public AtnaObject setObjectIDTypeCode(AtnaCode objectIdTypeCode) {
        this.objectIdTypeCode = objectIdTypeCode;
        return this;
    }

    public String getObjectName() {
        return objectName;
    }

    public AtnaObject setObjectName(String objectName) {
        this.objectName = objectName;
        return this;
    }

    public List<ObjectDescription> getDescriptions() {
        return new ArrayList(descriptions);
    }

    public void addObjectDescription(ObjectDescription desc) {
        descriptions.add(desc);
    }

    public void removeObjectDescription(ObjectDescription desc) {
        descriptions.remove(desc);
    }

    public List<String> getObjectDetailTypes() {
        return new ArrayList<String>(objectDetailTypes);
    }

    public AtnaObject addObjectDetailType(String objectDetailType) {
        this.objectDetailTypes.add(objectDetailType);
        return this;
    }

    public AtnaObject removeObjectDetailType(String objectDetailType) {
        this.objectDetailTypes.remove(objectDetailType);
        return this;
    }

    public ObjectType getObjectTypeCode() {
        return objectTypeCode;
    }

    public AtnaObject setObjectTypeCode(ObjectType objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
        return this;
    }

    public String getObjectId() {
        return objectId;
    }

    public AtnaObject setObjectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public ObjectTypeCodeRole getObjectTypeCodeRole() {
        return objectTypeCodeRole;
    }

    public AtnaObject setObjectTypeCodeRole(ObjectTypeCodeRole objectTypeCodeRole) {
        this.objectTypeCodeRole = objectTypeCodeRole;
        return this;
    }

    public String getObjectSensitivity() {
        return objectSensitivity;
    }

    public AtnaObject setObjectSensitivity(String objectSensitivity) {
        this.objectSensitivity = objectSensitivity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaObject)) {
            return false;
        }

        AtnaObject that = (AtnaObject) o;

        if (objectDetailTypes != null ? !objectDetailTypes.equals(that.objectDetailTypes) : that.objectDetailTypes != null) {
            return false;
        }
        if (objectIdTypeCode != null ? !objectIdTypeCode.equals(that.objectIdTypeCode) : that.objectIdTypeCode != null) {
            return false;
        }
        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) {
            return false;
        }
        if (objectName != null ? !objectName.equals(that.objectName) : that.objectName != null) {
            return false;
        }
        if (objectSensitivity != null ? !objectSensitivity.equals(that.objectSensitivity) : that.objectSensitivity != null) {
            return false;
        }
        if (objectTypeCode != that.objectTypeCode) {
            return false;
        }
        if (objectTypeCodeRole != that.objectTypeCodeRole) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = objectIdTypeCode != null ? objectIdTypeCode.hashCode() : 0;
        result = 31 * result + (objectName != null ? objectName.hashCode() : 0);
        result = 31 * result + (objectDetailTypes != null ? objectDetailTypes.hashCode() : 0);
        result = 31 * result + (objectTypeCode != null ? objectTypeCode.hashCode() : 0);
        result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
        result = 31 * result + (objectTypeCodeRole != null ? objectTypeCodeRole.hashCode() : 0);
        result = 31 * result + (objectSensitivity != null ? objectSensitivity.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder().append("[")
                .append(getClass().getName())
                .append(" object id=")
                .append(getObjectId())
                .append(" object name=")
                .append(getObjectName())
                .append(" object type=")
                .append(getObjectTypeCode())
                .append(" object type role=")
                .append(getObjectTypeCodeRole())
                .append(" object id type=")
                .append(getObjectIdTypeCode())
                .append(" object sensitivity=")
                .append(getObjectSensitivity())
                .append(" object detail types=")
                .append(getObjectDetailTypes())
                .append("]")
                .toString();
    }
}
