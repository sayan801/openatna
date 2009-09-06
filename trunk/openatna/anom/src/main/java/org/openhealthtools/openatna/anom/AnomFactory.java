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
import org.openhealthtools.openatna.anom.jaxb21.JaxbAnomFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

/**
 * factory for reading, writing, and creating anom objects.
 * Messages are created by first creating an AnomEvent, and then passing this in.
 * An AnomEvent is created using an AnomCode, which in turn requires a string code at least:
 * <p/>
 * AnomEvent evt = factory.newEvent(factory.newCode("xyz"), EventOutcome.SUCCESS);
 * AnomMessage msg = factory.newMessage(evt);
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

public abstract class AnomFactory {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.anom.AnomFactory");


    public static final String FACTORY_PROPERTY = "org.openhealthtools.openatna.anom.AnomFactory";
    public static final String PROP_FILE = "openatna.properties";
    private static volatile AnomFactory factory;

    public static synchronized AnomFactory getFactory() {
        if (factory != null) {
            return factory;
        }
        String cls = System.getProperty(FACTORY_PROPERTY);
        if (cls == null) {
            InputStream in = AnomFactory.class.getClassLoader().getResourceAsStream(PROP_FILE);
            if (in != null) {
                Properties props = new Properties();
                try {
                    props.load(in);
                    cls = props.getProperty(FACTORY_PROPERTY);
                    if (cls != null) {
                        Class clazz = Class.forName(cls);
                        factory = (AnomFactory) clazz.newInstance();
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
                factory = (AnomFactory) clazz.newInstance();
            } catch (Exception e) {
                log.debug(" could not factory load class " + cls, e);
            }
        }
        if (factory == null) {
            factory = new JaxbAnomFactory();
        }
        return factory;
    }

    public abstract AnomMessage read(InputStream in) throws AnomException, IOException;

    public abstract void write(AnomMessage message, OutputStream out) throws AnomException, IOException;

    public abstract AnomMessage newMessage(AnomEvent event);

    public abstract AnomSource newSource(String sourceId);

    public abstract AnomEvent newEvent(AnomCode code, EventOutcome outcome);

    public abstract AnomParticipant newParticipant(String userId);

    public abstract AnomObject newObject(AnomCode objectIdType, String objectId);

    public abstract AnomObjectDetail newObjectDetail();

    public abstract AnomCode newCode(String code);

    public abstract AnomCode newCode(String code, String codeSystem, String codeSystemName);

    protected void validate(AnomMessage message) throws AnomException {
        if (message.getEvent() == null) {
            throw new AnomException("no event identification defined");
        }
        AnomEvent evt = message.getEvent();
        if (evt.getEventCode() == null || evt.getEventCode().getCode() == null) {
            throw new AnomException("invalid event code");
        }
        if (evt.getEventOutcome() == null) {
            throw new AnomException("invalid event outcome");
        }
        if (evt.getEventDateTime() == null) {
            throw new AnomException("invalid time stamp");
        }
        List<AnomSource> sources = message.getSources();
        if (sources.size() == 0) {
            throw new AnomException("no audit source defined");
        }
        for (AnomSource source : sources) {
            if (source.getSourceID() == null) {
                throw new AnomException("no audit source id defined");
            }
        }
        List<AnomParticipant> participants = message.getParticipants();
        if (participants.size() == 0) {
            throw new AnomException("no participants defined");
        }
        for (AnomParticipant participant : participants) {
            if (participant.getUserID() == null) {
                throw new AnomException("no active participant user id defined");
            }
        }
        List<AnomObject> objects = message.getObjects();
        for (AnomObject object : objects) {
            if (object.getObjectID() == null) {
                throw new AnomException("no participant object id defined");
            }
            if (object.getObjectIDTypeCode() == null || object.getObjectIDTypeCode().getCode() == null) {
                throw new AnomException("invalid object id type code");
            }
        }
    }


}
