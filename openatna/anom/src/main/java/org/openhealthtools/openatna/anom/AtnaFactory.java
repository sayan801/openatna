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

package org.openhealthtools.openatna.anom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.jaxb21.JaxbAtnaFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

/**
 * <p/>
 * AtnaMessage msg = factory.newMessage(factory.newCode("xyz"), EventOutcome.SUCCESS);
 * msg.addSource(factory.newSource("localhost"))
 * .addParticipant(factory.newParticipant("me"));
 * <p/>
 * confused? :-)
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 4:02:28 PM
 * @date $Date:$ modified by $Author:$
 */

public abstract class AtnaFactory {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.anom.AtnaFactory");


    public static final String FACTORY_PROPERTY = "org.openhealthtools.openatna.anom.AtnaFactory";
    public static final String PROP_FILE = "openatna.properties";
    private static volatile AtnaFactory factory;

    // this is probably temporary. A factory should be set in configuration somewhere.
    public static synchronized AtnaFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        String cls = System.getProperty(FACTORY_PROPERTY);
        if (cls == null) {
            InputStream in = AtnaFactory.class.getClassLoader().getResourceAsStream(PROP_FILE);
            if (in != null) {
                Properties props = new Properties();
                try {
                    props.load(in);
                    cls = props.getProperty(FACTORY_PROPERTY);
                    if (cls != null) {
                        Class clazz = Class.forName(cls);
                        factory = (AtnaFactory) clazz.newInstance();
                    }
                } catch (Exception e) {
                    log.debug(" could not factory load class " + cls, e);
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.debug("error closing stream.", e);
                    }
                }
            }
        } else {
            try {
                Class clazz = Class.forName(cls);
                factory = (AtnaFactory) clazz.newInstance();
            } catch (Exception e) {
                log.debug(" could not factory load class " + cls, e);
            }
        }
        if (factory == null) {
            factory = new JaxbAtnaFactory();
        }
        return factory;
    }

    public abstract AtnaMessage read(InputStream in) throws AtnaException, IOException;

    public abstract void write(AtnaMessage message, OutputStream out) throws AtnaException, IOException;

    public abstract AtnaMessage newMessage(AtnaCode code, EventOutcome outcome);

    public abstract AtnaSource newSource(String sourceId);

    public abstract AtnaParticipant newParticipant(String userId);

    public abstract AtnaMessageParticipant newMessageParticipant(AtnaParticipant participant);

    public abstract AtnaObject newObject(AtnaCode objectIdType, String objectId);

    public abstract AtnaMessageObject newMessageObject(AtnaObject object);

    public abstract AtnaObjectDetail newObjectDetail();

    public abstract AtnaCode newCode(String code);

    public abstract AtnaCode newCode(String code, String displayName, String codeSystemName);

    // this should move to a higher level.
    protected void validate(AtnaMessage message) throws AtnaException {

        AtnaCode evt = message.getEventCode();
        if (evt.getCode() == null) {
            throw new AtnaException("invalid event code", AtnaException.AtnaError.NO_EVENT_CODE);
        }
        if (message.getEventOutcome() == null) {
            throw new AtnaException("invalid event outcome", AtnaException.AtnaError.NO_EVENT_OUTCOME);
        }
        if (message.getEventDateTime() == null) {
            throw new AtnaException("invalid time stamp", AtnaException.AtnaError.INVALID_EVENT_TIMESTAMP);
        }
        List<AtnaCode> codes = message.getEventTypeCodes();
        for (AtnaCode code : codes) {
            if (code.getCode() == null) {
                throw new AtnaException("no active participant user id defined", AtnaException.AtnaError.INVALID_CODE);
            }
        }
        List<AtnaSource> sources = message.getSources();
        if (sources.size() == 0) {
            throw new AtnaException("no audit source defined", AtnaException.AtnaError.NO_AUDIT_SOURCE);
        }
        for (AtnaSource source : sources) {
            if (source.getSourceID() == null) {
                throw new AtnaException("no audit source id defined", AtnaException.AtnaError.NO_AUDIT_SOURCE_ID);
            }
            codes = source.getSourceTypeCodes();
            for (AtnaCode code : codes) {
                if (code.getCode() == null) {
                    throw new AtnaException("no active participant user id defined", AtnaException.AtnaError.INVALID_CODE);
                }
            }
        }
        List<AtnaMessageParticipant> participants = message.getParticipants();
        if (participants.size() == 0) {
            throw new AtnaException("no participants defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT);
        }
        for (AtnaMessageParticipant participant : participants) {
            if (participant.getParticipant().getUserID() == null) {
                throw new AtnaException("no active participant user id defined", AtnaException.AtnaError.NO_ACTIVE_PARTICIPANT_ID);
            }
            codes = participant.getParticipant().getRoleIDCodes();
            for (AtnaCode code : codes) {
                if (code.getCode() == null) {
                    throw new AtnaException("no active participant user id defined", AtnaException.AtnaError.INVALID_CODE);
                }
            }
        }
        List<AtnaMessageObject> objects = message.getObjects();
        for (AtnaMessageObject object : objects) {
            if (object.getObject().getObjectID() == null) {
                throw new AtnaException("no participant object id defined", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT_ID);
            }
            if (object.getObject().getObjectIDTypeCode() == null || object.getObject().getObjectIDTypeCode().getCode() == null) {
                throw new AtnaException("invalid object id type code", AtnaException.AtnaError.NO_PARTICIPANT_OBJECT_ID_TYPE_CODE);
            }
        }
    }


}
