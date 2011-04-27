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
import org.openhealthtools.openatna.anom.AtnaException;

/**
 * This class logs errors at the ATNA message parsing layer.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 11:26:36 AM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaErrorLogger {

    private static Log log = LogFactory.getLog("ATNA.ATNA_ERROR_LOG");

    private static List<ErrorHandler<AtnaException>> handlers = new ArrayList<ErrorHandler<AtnaException>>();

    private AtnaErrorLogger() {
    }

    public static void addErrorHandler(ErrorHandler<AtnaException> handler) {
        handlers.add(handler);
    }

    private static void invokeHandlers(AtnaException e) {
        for (ErrorHandler<AtnaException> handler : handlers) {
            handler.handle(e);
        }
    }

    public static void log(AtnaException e) {
        invokeHandlers(e);
        StringBuilder sb = new StringBuilder("===> ATNA EXCEPTION THROWN\n");
        AtnaException.AtnaError error = e.getError();
        sb.append("** ATNA ERROR:").append(error).append("**\n");

        log.error(sb.toString(), e);
    }

}
