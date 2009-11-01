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

package org.openhealthtools.openatna.audit.test;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.process.AtnaLogMessage;
import org.openhealthtools.openatna.jaxb21.JaxbIOFactory;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 10:11:53 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbLogMessage extends AtnaLogMessage {

    public JaxbLogMessage() {
        super(new JaxbIOFactory());
    }

    public JaxbLogMessage(AtnaMessage message) {
        super(message, new JaxbIOFactory());
    }
}