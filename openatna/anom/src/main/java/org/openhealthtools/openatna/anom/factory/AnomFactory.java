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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * factory for reading, writing, and creating anom objects
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 4:02:28 PM
 * @date $Date:$ modified by $Author:$
 */

public interface AnomFactory {

    public AuditMessage read(InputStream in) throws AnomException, IOException;

    public void write(AuditMessage message, OutputStream out) throws AnomException, IOException;

    public AuditMessage newMessage();

    public AuditSource newSource();

    public AuditEvent newEvent();

    public AuditParticipant newParticipant();

    public AuditObject newObject();

    public AuditObjectDetail newObjectDetail();

    public CodedValue newCodedValue();
}
