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

public class JaxbAnomMessage implements AnomMessage {

    private AuditMessage msg;

    public JaxbAnomMessage(AuditMessage msg) {
        this.msg = msg;
    }

    public JaxbAnomMessage(JaxbAnomEvent evt) {
        this.msg = new AuditMessage();
        msg.setEventIdentification(evt.getEvt());
    }

    public AnomEvent getEvent() {
        if (msg.getEventIdentification() != null) {
            return new JaxbAnomEvent(msg.getEventIdentification());
        }
        return null;
    }

    public List<AnomParticipant> getParticipants() {
        List<ActiveParticipantType> l = msg.getActiveParticipant();
        List<AnomParticipant> ret = new ArrayList<AnomParticipant>();
        for (ActiveParticipantType a : l) {
            ret.add(new JaxbAnomParticipant(a));
        }
        return ret;
    }

    public AnomMessage addParticipant(AnomParticipant participant) {
        if (participant instanceof JaxbAnomParticipant) {
            JaxbAnomParticipant jp = (JaxbAnomParticipant) participant;
            msg.getActiveParticipant().add(jp.getParticipant());
        }
        return this;
    }

    public AnomMessage removeParticipant(AnomParticipant participant) {
        if (participant instanceof JaxbAnomParticipant) {
            JaxbAnomParticipant jp = (JaxbAnomParticipant) participant;
            msg.getActiveParticipant().remove(jp.getParticipant());
        }
        return this;
    }

    public AnomParticipant getParticipant(String id) {
        List<ActiveParticipantType> l = msg.getActiveParticipant();
        for (ActiveParticipantType type : l) {
            if (type.getUserID().equals(id)) {
                return new JaxbAnomParticipant(type);
            }
        }
        return null;
    }

    public List<AnomSource> getSources() {
        List<AuditSourceIdentificationType> l = msg.getAuditSourceIdentification();
        List<AnomSource> ret = new ArrayList<AnomSource>();
        for (AuditSourceIdentificationType a : l) {
            ret.add(new JaxbAnomSource(a));
        }
        return ret;
    }

    public AnomMessage addSource(AnomSource anomSource) {
        if (anomSource instanceof JaxbAnomSource) {
            JaxbAnomSource js = (JaxbAnomSource) anomSource;
            msg.getAuditSourceIdentification().add(js.getSource());
        }
        return this;
    }

    public AnomMessage removeSource(AnomSource anomSource) {
        if (anomSource instanceof JaxbAnomSource) {
            JaxbAnomSource js = (JaxbAnomSource) anomSource;
            msg.getAuditSourceIdentification().remove(js.getSource());
        }
        return this;
    }

    public AnomSource getSource(String id) {
        List<AuditSourceIdentificationType> l = msg.getAuditSourceIdentification();
        for (AuditSourceIdentificationType type : l) {
            if (type.getAuditSourceID().equals(id)) {
                return new JaxbAnomSource(type);
            }
        }
        return null;
    }

    public List<AnomObject> getObjects() {
        List<ParticipantObjectIdentificationType> l = msg.getParticipantObjectIdentification();
        List<AnomObject> ret = new ArrayList<AnomObject>();
        for (ParticipantObjectIdentificationType a : l) {
            ret.add(new JaxbAnomObject(a));
        }
        return ret;
    }

    public AnomMessage addObject(AnomObject object) {
        if (object instanceof JaxbAnomObject) {
            JaxbAnomObject jo = (JaxbAnomObject) object;
            msg.getParticipantObjectIdentification().add(jo.getObject());
        }
        return this;
    }

    public AnomMessage removeObject(AnomObject object) {
        if (object instanceof JaxbAnomObject) {
            JaxbAnomObject jo = (JaxbAnomObject) object;
            msg.getParticipantObjectIdentification().remove(jo.getObject());
        }
        return this;
    }

    public AnomObject getObject(String id) {
        List<ParticipantObjectIdentificationType> l = msg.getParticipantObjectIdentification();
        for (ParticipantObjectIdentificationType type : l) {
            if (type.getParticipantObjectID().equals(id)) {
                return new JaxbAnomObject(type);
            }
        }
        return null;
    }

    public AuditMessage getMessage() {
        return msg;
    }
}
