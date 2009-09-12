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

import java.util.Date;
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

    public AtnaCode getEventCode();

    public List<AtnaCode> getEventTypeCodes();

    public AtnaMessage addEventTypeCode(AtnaCode value);

    public AtnaMessage removeEventTypeCode(AtnaCode value);

    public EventAction getEventActionCode();

    public AtnaMessage setEventActionCode(EventAction value);

    public Date getEventDateTime();

    public AtnaMessage setEventDateTime(Date value);

    public EventOutcome getEventOutcome();

    public AtnaMessage setEventOutcome(EventOutcome value);

    public List<AtnaMessageParticipant> getParticipants();

    public AtnaMessage addParticipant(AtnaMessageParticipant participant);

    public AtnaMessage removeParticipant(AtnaMessageParticipant participant);

    public AtnaMessageParticipant getParticipant(String id);

    public List<AtnaSource> getSources();

    public AtnaMessage addSource(AtnaSource atnaSource);

    public AtnaMessage removeSource(AtnaSource atnaSource);

    public AtnaSource getSource(String id);

    public List<AtnaMessageObject> getObjects();

    public AtnaMessage addObject(AtnaMessageObject object);

    public AtnaMessage removeObject(AtnaMessageObject object);

    public AtnaMessageObject getObject(String id);
}
