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

import java.util.List;

/**
 * Audit message interface
 * <p/>
 * This contains modifiers for everything bar the event.
 * A message should always have an event, i.e. be constructed with one.
 * The event itself is still mutable.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */

public interface AtnaMessage {

    public AtnaEvent getEvent();

    public List<AtnaParticipant> getParticipants();

    public AtnaMessage addParticipant(AtnaParticipant participant);

    public AtnaMessage removeParticipant(AtnaParticipant participant);

    public AtnaParticipant getParticipant(String id);

    public List<AtnaSource> getSources();

    public AtnaMessage addSource(AtnaSource atnaSource);

    public AtnaMessage removeSource(AtnaSource atnaSource);

    public AtnaSource getSource(String id);

    public List<AtnaObject> getObjects();

    public AtnaMessage addObject(AtnaObject object);

    public AtnaMessage removeObject(AtnaObject object);

    public AtnaObject getObject(String id);
}
