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
 * Event Identification
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AtnaEvent {

    public AtnaCode getEventCode();

    public List<AtnaCode> getEventTypeCodes();

    public AtnaEvent addEventTypeCode(AtnaCode value);

    public AtnaEvent removeEventTypeCode(AtnaCode value);

    public EventAction getEventActionCode();

    public AtnaEvent setEventActionCode(EventAction value);

    public Date getEventDateTime();

    public AtnaEvent setEventDateTime(Date value);

    public EventOutcome getEventOutcome();

    public AtnaEvent setEventOutcome(EventOutcome value);

}
