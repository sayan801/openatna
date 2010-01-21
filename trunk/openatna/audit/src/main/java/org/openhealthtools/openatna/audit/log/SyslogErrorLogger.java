/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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
import org.openhealthtools.openatna.syslog.SyslogException;

/**
 * Logs errors at the syslog level
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 11:26:26 AM
 * @date $Date:$ modified by $Author:$
 */

public class SyslogErrorLogger {

    private static Log log = LogFactory.getLog("ATNA.SYSLOG_ERROR_LOG");

    private static List<ErrorHandler<SyslogException>> handlers = new ArrayList<ErrorHandler<SyslogException>>();

    private SyslogErrorLogger() {
    }

    public static void addErrorHandler(ErrorHandler<SyslogException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(SyslogException e) {
        for (ErrorHandler<SyslogException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(SyslogException e) {
        invokeHandlers(e);
        StringBuilder sb = new StringBuilder("===> SYSLOG EXCEPTION THROWN\n");
        byte[] bytes = e.getBytes();
        if (bytes.length == 0) {
            sb.append("no bytes available.\n");
        } else if (bytes.length > 8096) {
            sb.append("too many bytes (" + bytes.length + ") to report.\n");
        } else {
            sb.append("bytes are:\n")
                    .append(new String(bytes));
        }
        log.error(sb.toString(), e);
    }

}
