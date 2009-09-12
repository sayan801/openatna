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

public class JaxbAtnaFactory extends AtnaFactory {

    static JAXBContext jc;

    static {
        try {
            jc = JAXBContext.newInstance("org.openhealthtools.openatna.anom.jaxb21.schema");
        } catch (JAXBException e) {
            // this is fatal and out fault
            throw new RuntimeException("Error creating JAXB context:", e);
        }
    }

    public AtnaMessage read(InputStream in) throws AtnaException, IOException {
        if (jc == null) {
            throw new AtnaException("Could not create Jaxb Context");
        }
        try {
            Unmarshaller u = jc.createUnmarshaller();
            AuditMessage a = (AuditMessage) u.unmarshal(in);
            JaxbAtnaMessage jm = new JaxbAtnaMessage(a);
            validate(jm);
            return jm;
        } catch (JAXBException e) {
            throw new AtnaException(e);
        }
    }

    public void write(AtnaMessage message, OutputStream out) throws AtnaException, IOException {
        if (jc == null) {
            throw new AtnaException("Could not create Jaxb Context");
        }
        if (message.getEventDateTime() == null) {
            message.setEventDateTime(new Date());
        }
        validate(message);
        try {
            if (message instanceof JaxbAtnaMessage) {
                JaxbAtnaMessage jmessage = (JaxbAtnaMessage) message;
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(jmessage.getMessage(), out);
            }
        } catch (JAXBException e) {
            throw new AtnaException(e);
        }
    }

    public AtnaMessage newMessage(AtnaCode code, EventOutcome outcome) {
        if (code instanceof JaxbAtnaCode) {
            return new JaxbAtnaMessage((JaxbAtnaCode) code, outcome);
        }
        return null;
    }

    public AtnaSource newSource(String sourceId) {
        return new JaxbAtnaSource(sourceId);
    }

    public AtnaParticipant newParticipant(String userId) {
        return new JaxbAtnaParticipant(userId);
    }

    @Override
    public AtnaMessageParticipant newMessageParticipant(AtnaParticipant participant) {
        if (participant instanceof JaxbAtnaParticipant) {
            return (AtnaMessageParticipant) participant;
        }
        return null;
    }

    public AtnaObject newObject(AtnaCode objectIdType, String objectId) {
        return new JaxbAtnaObject(objectIdType, objectId);
    }

    @Override
    public AtnaMessageObject newMessageObject(AtnaObject object) {
        if (object instanceof JaxbAtnaObject) {
            return (AtnaMessageObject) object;
        }
        return null;
    }

    public AtnaObjectDetail newObjectDetail() {
        return new JaxbObjectDetail();
    }

    public AtnaCode newCode(String code) {
        return new JaxbAtnaCode(code);
    }

    public AtnaCode newCode(String code, String displayName, String codeSystemName) {
        JaxbAtnaCode jcode = new JaxbAtnaCode(code);
        jcode.setDisplayName(displayName);
        jcode.setCodeSystemName(codeSystemName);
        return jcode;
    }
}
