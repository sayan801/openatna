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
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;

/**
 * Persistence Exception logger
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 3:23:41 PM
 * @date $Date:$ modified by $Author:$
 */

public class PersistenceErrorLogger {

    private static Log log = LogFactory.getLog("ATNA.PERSISTENCE_ERROR_LOG");

    private static List<ErrorHandler<AtnaPersistenceException>> handlers
            = new ArrayList<ErrorHandler<AtnaPersistenceException>>();

    private PersistenceErrorLogger() {
    }

    public static void addErrorHandler(ErrorHandler<AtnaPersistenceException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(AtnaPersistenceException e) {
        for (ErrorHandler<AtnaPersistenceException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(AtnaPersistenceException e) {
        invokeHandlers(e);
        StringBuilder sb = new StringBuilder("===> ATNA PERSISTENCE EXCEPTION THROWN\n");
        AtnaPersistenceException.PersistenceError error = e.getError();
        sb.append("** PERSISTENCE ERROR:").append(error).append("**\n");

        log.error(sb.toString(), e);
    }
}
