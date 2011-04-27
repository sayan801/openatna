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

/**
 * This class logs errors that are not specific to ATNA
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 11:26:36 AM
 * @date $Date:$ modified by $Author:$
 */

public class ErrorLogger {

    private static Log log = LogFactory.getLog("ATNA.ERROR_LOG");

    private static List<ErrorHandler<Throwable>> handlers = new ArrayList<ErrorHandler<Throwable>>();

    private ErrorLogger() {
    }

    public static void addErrorHandler(ErrorHandler<Throwable> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(Throwable e) {
        for (ErrorHandler<Throwable> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(Throwable e) {
        invokeHandlers(e);
        StringBuilder sb = new StringBuilder("===> EXCEPTION THROWN\n");
        sb.append("** ERROR:").append(e.getClass().getName()).append("**\n");
        log.error(sb.toString(), e);
    }
}
