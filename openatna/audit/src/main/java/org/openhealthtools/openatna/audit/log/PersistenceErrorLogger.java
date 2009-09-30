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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;

/**
 * Persistence Exception logger
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 3:23:41 PM
 * @date $Date:$ modified by $Author:$
 */

public class PersistenceErrorLogger {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.log.PersistenceErrorLogger");

    public static void log(AtnaPersistenceException e) {
        StringBuilder sb = new StringBuilder("===> ATNA PERSISTENCE EXCEPTION THROWN\n");
        AtnaPersistenceException.PersistenceError error = e.getError();
        sb.append("** PERSISTENCE ERROR:").append(error).append("**\n");

        log.error(sb.toString(), e);
    }
}
