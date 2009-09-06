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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.anom.jaxb21.schema.AuditMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:14:18 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAnomFactory extends AnomFactory {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.anom.jaxb21.JaxbAnomFactory");


    static JAXBContext jc;

    static {
        try {
            jc = JAXBContext.newInstance("org.openhealthtools.openatna.anom.jaxb21.schema");
        } catch (JAXBException e) {
            log.error("Error creating JAXB context:", e);
        }
    }

    public AnomMessage read(InputStream in) throws AnomException, IOException {
        if (jc == null) {
            throw new AnomException("Could not create Jaxb Context");
        }
        try {
            Unmarshaller u = jc.createUnmarshaller();
            AuditMessage a = (AuditMessage) u.unmarshal(in);
            JaxbAnomMessage jm = new JaxbAnomMessage(a);
            validate(jm);
            return jm;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write(AnomMessage message, OutputStream out) throws AnomException, IOException {
        if (jc == null) {
            throw new AnomException("Could not create Jaxb Context");
        }
        if (message.getEvent().getEventDateTime() == null) {
            message.getEvent().setEventDateTime(new Date());
        }
        validate(message);
        try {
            if (message instanceof JaxbAnomMessage) {
                JaxbAnomMessage jmessage = (JaxbAnomMessage) message;
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(jmessage.getMessage(), out);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public AnomMessage newMessage(AnomEvent event) {
        if (event instanceof JaxbAnomEvent) {
            return new JaxbAnomMessage((JaxbAnomEvent) event);
        }
        return null;
    }

    public AnomSource newSource(String sourceId) {
        return new JaxbAnomSource(sourceId);
    }

    public AnomEvent newEvent(AnomCode code, EventOutcome outcome) {
        if (code instanceof JaxbAnomCode) {
            return new JaxbAnomEvent((JaxbAnomCode) code, outcome);
        }
        return null;
    }

    public AnomParticipant newParticipant(String userId) {
        return new JaxbAnomParticipant(userId);
    }

    public AnomObject newObject(AnomCode objectIdType, String objectId) {
        return new JaxbAnomObject(objectIdType, objectId);
    }

    public AnomObjectDetail newObjectDetail() {
        return new JaxbObjectDetail();
    }

    public AnomCode newCode(String code) {
        return new JaxbAnomCode(code);
    }

    public AnomCode newCode(String code, String codeSystem, String codeSystemName) {
        JaxbAnomCode jcode = new JaxbAnomCode(code);
        jcode.setCodeSystem(codeSystem);
        jcode.setCodeSystemName(codeSystemName);
        return jcode;
    }
}
