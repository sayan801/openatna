/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.audit.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.AuditException;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 11:26:56 AM
 * @date $Date:$ modified by $Author:$
 */

public class AuditErrorLogger {

    private static Log log = LogFactory.getLog("ATNA.AUDIT_ERROR_LOG");

    private static List<ErrorHandler<AuditException>> handlers = new ArrayList<ErrorHandler<AuditException>>();

    private AuditErrorLogger() {
    }

    public static void addErrorHandler(ErrorHandler<AuditException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(AuditException e) {
        for (ErrorHandler<AuditException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(AuditException e) {
        invokeHandlers(e);
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
