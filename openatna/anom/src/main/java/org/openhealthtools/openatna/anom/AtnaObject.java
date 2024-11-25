package org.openhealthtools.openatna.anom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The resource to which the message refers; the resource being acted upon.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class AtnaObject implements Serializable {

    private static final long serialVersionUID = 7352385693414253878L;

    private AtnaCode objectIdTypeCode;
    private String objectName;
    private List<String> objectDetailTypes = new ArrayList<>();
    private ObjectType objectTypeCode;
    private String objectId;
    private ObjectTypeCodeRole objectTypeCodeRole;
    private String objectSensitivity;
    private List<ObjectDescription> descriptions = new ArrayList<>();

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
        return new ArrayList<>(descriptions);
    }

    public void addObjectDescription(ObjectDescription desc) {
        descriptions.add(desc);
    }

    public void removeObjectDescription(ObjectDescription desc) {
        descriptions.remove(desc);
    }

    public List<String> getObjectDetailTypes() {
        return new ArrayList<>(objectDetailTypes);
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

    @Override
    public String toString() {
        return "[" +
                getClass().getName() +
                " object id=" +
                getObjectId() +
                " object name=" +
                getObjectName() +
                " object type=" +
                getObjectTypeCode() +
                " object type role=" +
                getObjectTypeCodeRole() +
                " object id type=" +
                getObjectIdTypeCode() +
                " object sensitivity=" +
                getObjectSensitivity() +
                " object detail types=" +
                getObjectDetailTypes() +
                "]";
    }
}
