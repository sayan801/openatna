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
import org.openhealthtools.openatna.anom.jaxb21.schema.ActiveParticipantType;
import org.openhealthtools.openatna.anom.jaxb21.schema.AuditMessage;
import org.openhealthtools.openatna.anom.jaxb21.schema.AuditSourceIdentificationType;
import org.openhealthtools.openatna.anom.jaxb21.schema.ParticipantObjectIdentificationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:15:10 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAtnaMessage implements AtnaMessage {

    private AuditMessage msg;

    public JaxbAtnaMessage(AuditMessage msg) {
        this.msg = msg;
    }

    public JaxbAtnaMessage(JaxbAtnaEvent evt) {
        this.msg = new AuditMessage();
        msg.setEventIdentification(evt.getEvt());
    }

    public AtnaEvent getEvent() {
        if (msg.getEventIdentification() != null) {
            return new JaxbAtnaEvent(msg.getEventIdentification());
        }
        return null;
    }

    public List<AtnaParticipant> getParticipants() {
        List<ActiveParticipantType> l = msg.getActiveParticipant();
        List<AtnaParticipant> ret = new ArrayList<AtnaParticipant>();
        for (ActiveParticipantType a : l) {
            ret.add(new JaxbAtnaParticipant(a));
        }
        return ret;
    }

    public AtnaMessage addParticipant(AtnaParticipant participant) {
        if (participant instanceof JaxbAtnaParticipant) {
            JaxbAtnaParticipant jp = (JaxbAtnaParticipant) participant;
            msg.getActiveParticipant().add(jp.getParticipant());
        }
        return this;
    }

    public AtnaMessage removeParticipant(AtnaParticipant participant) {
        if (participant instanceof JaxbAtnaParticipant) {
            JaxbAtnaParticipant jp = (JaxbAtnaParticipant) participant;
            msg.getActiveParticipant().remove(jp.getParticipant());
        }
        return this;
    }

    public AtnaParticipant getParticipant(String id) {
        List<ActiveParticipantType> l = msg.getActiveParticipant();
        for (ActiveParticipantType type : l) {
            if (type.getUserID().equals(id)) {
                return new JaxbAtnaParticipant(type);
            }
        }
        return null;
    }

    public List<AtnaSource> getSources() {
        List<AuditSourceIdentificationType> l = msg.getAuditSourceIdentification();
        List<AtnaSource> ret = new ArrayList<AtnaSource>();
        for (AuditSourceIdentificationType a : l) {
            ret.add(new JaxbAtnaSource(a));
        }
        return ret;
    }

    public AtnaMessage addSource(AtnaSource atnaSource) {
        if (atnaSource instanceof JaxbAtnaSource) {
            JaxbAtnaSource js = (JaxbAtnaSource) atnaSource;
            msg.getAuditSourceIdentification().add(js.getSource());
        }
        return this;
    }

    public AtnaMessage removeSource(AtnaSource atnaSource) {
        if (atnaSource instanceof JaxbAtnaSource) {
            JaxbAtnaSource js = (JaxbAtnaSource) atnaSource;
            msg.getAuditSourceIdentification().remove(js.getSource());
        }
        return this;
    }

    public AtnaSource getSource(String id) {
        List<AuditSourceIdentificationType> l = msg.getAuditSourceIdentification();
        for (AuditSourceIdentificationType type : l) {
            if (type.getAuditSourceID().equals(id)) {
                return new JaxbAtnaSource(type);
            }
        }
        return null;
    }

    public List<AtnaObject> getObjects() {
        List<ParticipantObjectIdentificationType> l = msg.getParticipantObjectIdentification();
        List<AtnaObject> ret = new ArrayList<AtnaObject>();
        for (ParticipantObjectIdentificationType a : l) {
            ret.add(new JaxbAtnaObject(a));
        }
        return ret;
    }

    public AtnaMessage addObject(AtnaObject object) {
        if (object instanceof JaxbAtnaObject) {
            JaxbAtnaObject jo = (JaxbAtnaObject) object;
            msg.getParticipantObjectIdentification().add(jo.getObject());
        }
        return this;
    }

    public AtnaMessage removeObject(AtnaObject object) {
        if (object instanceof JaxbAtnaObject) {
            JaxbAtnaObject jo = (JaxbAtnaObject) object;
            msg.getParticipantObjectIdentification().remove(jo.getObject());
        }
        return this;
    }

    public AtnaObject getObject(String id) {
        List<ParticipantObjectIdentificationType> l = msg.getParticipantObjectIdentification();
        for (ParticipantObjectIdentificationType type : l) {
            if (type.getParticipantObjectID().equals(id)) {
                return new JaxbAtnaObject(type);
            }
        }
        return null;
    }

    public AuditMessage getMessage() {
        return msg;
    }
}
