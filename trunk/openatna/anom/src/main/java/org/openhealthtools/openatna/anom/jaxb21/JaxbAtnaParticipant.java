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

import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaParticipant;
import org.openhealthtools.openatna.anom.NetworkAccessPoint;
import org.openhealthtools.openatna.anom.jaxb21.schema.ActiveParticipantType;
import org.openhealthtools.openatna.anom.jaxb21.schema.CodedValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:59:03 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAtnaParticipant implements AtnaParticipant {

    private ActiveParticipantType participant;

    public JaxbAtnaParticipant(ActiveParticipantType participant) {
        this.participant = participant;
    }

    public JaxbAtnaParticipant(String userId) {
        this.participant = new ActiveParticipantType();
        participant.setUserID(userId);
    }

    public List<AtnaCode> getRoleIDCodes() {
        List<AtnaCode> codes = new ArrayList<AtnaCode>();
        List<CodedValueType> l = participant.getRoleIDCode();
        for (CodedValueType codedValueType : l) {
            codes.add(new JaxbAtnaCode(codedValueType));
        }
        return codes;
    }

    public AtnaParticipant addRoleIDCode(AtnaCode value) {
        if (value instanceof JaxbAtnaCode) {
            JaxbAtnaCode code = (JaxbAtnaCode) value;
            participant.getRoleIDCode().add(code.getCodedValueType());
        }
        return this;
    }

    public AtnaParticipant removeRoleIDCode(AtnaCode value) {
        if (value instanceof JaxbAtnaCode) {
            JaxbAtnaCode code = (JaxbAtnaCode) value;
            participant.getRoleIDCode().remove(code.getCodedValueType());
        }
        return this;
    }

    public String getUserID() {
        return participant.getUserID();
    }

    public AtnaParticipant setUserID(String value) {
        participant.setUserID(value);
        return this;
    }

    public String getAlternativeUserID() {
        return participant.getAlternativeUserID();
    }

    public AtnaParticipant setAlternativeUserID(String value) {
        participant.setAlternativeUserID(value);
        return this;
    }

    public String getUserName() {
        return participant.getUserName();
    }

    public AtnaParticipant setUserName(String value) {
        participant.setUserName(value);
        return this;
    }

    public boolean isUserIsRequestor() {
        return participant.isUserIsRequestor();
    }

    public AtnaParticipant setUserIsRequestor(Boolean value) {
        participant.setUserIsRequestor(value);
        return this;
    }

    public String getNetworkAccessPointID() {
        return participant.getNetworkAccessPointID();
    }

    public AtnaParticipant setNetworkAccessPointID(String value) {
        participant.setNetworkAccessPointID(value);
        return this;
    }

    public NetworkAccessPoint getNetworkAccessPointTypeCode() {
        return NetworkAccessPoint.getAccessPoint(participant.getNetworkAccessPointTypeCode());
    }

    public AtnaParticipant setNetworkAccessPointTypeCode(NetworkAccessPoint value) {
        participant.setNetworkAccessPointTypeCode((short) value.value());
        return this;
    }

    public ActiveParticipantType getParticipant() {
        return participant;
    }
}
