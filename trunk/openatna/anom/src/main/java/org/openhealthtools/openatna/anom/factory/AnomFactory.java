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

package org.openhealthtools.openatna.anom.factory;

import org.openhealthtools.openatna.anom.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

public interface AnomFactory {

    public AnomMessage read(InputStream in) throws AnomException, IOException;

    public void write(AnomMessage message, OutputStream out) throws AnomException, IOException;

    public AnomMessage newMessage(AnomEvent event);

    public AnomSource newSource(String sourceId);

    public AnomEvent newEvent(AnomCode code, EventOutcome outcome);

    public AnomParticipant newParticipant(String userId);

    public AnomObject newObject(AnomCode objectIdType, String objectId);

    public AnomObjectDetail newObjectDetail();

    public AnomCode newCode(String code);

    public AnomCode newCode(String code, String codeSystem, String codeSystemName);


}
