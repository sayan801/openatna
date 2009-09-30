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

package org.openhealthtools.openatna.audit.log;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.openhealthtools.openatna.audit.AuditException;
import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 11:26:56 AM
 * @date $Date:$ modified by $Author:$
 */

public class AuditErrorLogger {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.log.AuditErrorLogger");

    public static void log(AuditException e) {
        StringBuilder sb = new StringBuilder("===> ATNA EXCEPTION THROWN\n");
        AuditException.AuditError error = e.getError();
        sb.append("** AUDIT ERROR:").append(error).append("**\n");
        AtnaMessage msg = e.getAtnaMessage();
        if (msg == null) {
            sb.append("no message available.\n");
        } else {
            sb.append("message is:\n")
                    .append(msg);
        }
        log.error(sb.toString(), e);
    }
}
