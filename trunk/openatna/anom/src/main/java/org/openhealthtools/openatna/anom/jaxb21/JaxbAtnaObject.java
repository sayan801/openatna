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

package org.openhealthtools.openatna.anom.jaxb21;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.anom.jaxb21.schema.ParticipantObjectIdentificationType;
import org.openhealthtools.openatna.anom.jaxb21.schema.TypeValuePairType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 7:24:55 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAtnaObject implements AtnaObject {

    private ParticipantObjectIdentificationType object;

    public JaxbAtnaObject(ParticipantObjectIdentificationType object) {
        this.object = object;
    }

    public JaxbAtnaObject(AtnaCode objectIdType, String id) {
        this.object = new ParticipantObjectIdentificationType();
        object.setParticipantObjectID(id);
        if (objectIdType instanceof JaxbAtnaCode) {
            object.setParticipantObjectIDTypeCode(((JaxbAtnaCode) objectIdType).getCodedValueType());
        }
    }

    public AtnaCode getObjectIDTypeCode() {
        if (object.getParticipantObjectIDTypeCode() != null) {
            return new JaxbAtnaCode(object.getParticipantObjectIDTypeCode());
        }
        return null;
    }

    public String getObjectName() {
        return object.getParticipantObjectName();
    }

    public AtnaObject setObjectName(String value) {
        object.setParticipantObjectName(value);
        return this;
    }

    public byte[] getObjectQuery() {
        return object.getParticipantObjectQuery();
    }

    public AtnaObject setObjectQuery(byte[] value) {
        object.setParticipantObjectQuery(value);
        return this;
    }

    public List<AtnaObjectDetail> getObjectDetails() {
        List<TypeValuePairType> details = object.getParticipantObjectDetail();
        List<AtnaObjectDetail> ret = new ArrayList<AtnaObjectDetail>();
        for (TypeValuePairType detail : details) {
            ret.add(new JaxbObjectDetail(detail));
        }
        return ret;
    }

    public List<AtnaObjectDetail> getObjectDetails(String type) {
        List<TypeValuePairType> details = object.getParticipantObjectDetail();
        List<AtnaObjectDetail> ret = new ArrayList<AtnaObjectDetail>();
        for (TypeValuePairType detail : details) {
            if (detail.getType().equals(type)) {
                ret.add(new JaxbObjectDetail(detail));
            }
        }
        return ret;
    }

    public AtnaObject addObjectDetail(AtnaObjectDetail detail) {
        if (detail instanceof JaxbObjectDetail) {
            object.getParticipantObjectDetail().add(((JaxbObjectDetail) detail).getPair());
        }
        return this;
    }

    public AtnaObject removeObjectDetail(AtnaObjectDetail detail) {
        if (detail instanceof JaxbObjectDetail) {
            object.getParticipantObjectDetail().remove(((JaxbObjectDetail) detail).getPair());
        }
        return this;
    }

    public String getObjectID() {
        return object.getParticipantObjectID();
    }

    public ObjectType getObjectTypeCode() {
        return ObjectType.getType(object.getParticipantObjectTypeCode());
    }

    public AtnaObject setObjectTypeCode(ObjectType value) {
        object.setParticipantObjectTypeCode((short) value.value());
        return this;
    }

    public ObjectTypeCodeRole getObjectTypeCodeRole() {
        return ObjectTypeCodeRole.getRole(object.getParticipantObjectTypeCodeRole());
    }

    public AtnaObject setObjectTypeCodeRole(ObjectTypeCodeRole value) {
        object.setParticipantObjectTypeCodeRole((short) value.value());
        return this;
    }

    public ObjectDataLifecycle getObjectDataLifeCycle() {
        return ObjectDataLifecycle.getLifecycle(object.getParticipantObjectDataLifeCycle());
    }

    public AtnaObject setObjectDataLifeCycle(ObjectDataLifecycle value) {
        object.setParticipantObjectDataLifeCycle((short) value.value());
        return this;
    }

    public String getObjectSensitivity() {
        return object.getParticipantObjectSensitivity();
    }

    public AtnaObject setObjectSensitivity(String value) {
        object.setParticipantObjectSensitivity(value);
        return this;
    }

    public ParticipantObjectIdentificationType getObject() {
        return object;
    }
}
