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

package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.audit.AuditException;
import org.openhealthtools.openatna.audit.log.AtnaErrorLogger;
import org.openhealthtools.openatna.audit.log.AuditErrorLogger;
import org.openhealthtools.openatna.audit.log.ErrorLogger;
import org.openhealthtools.openatna.audit.log.PersistenceErrorLogger;
import org.openhealthtools.openatna.audit.log.SyslogErrorLogger;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.syslog.SyslogException;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 1, 2009: 9:19:17 AM
 * @date $Date:$ modified by $Author:$
 */

public class ExceptionProcessor implements AtnaProcessor {

    public void process(ProcessContext context) throws Exception {
    }

    public void error(ProcessContext context) {
        Throwable t = context.getThrowable();
        if (t != null) {
            if (t instanceof AtnaException) {
                AtnaErrorLogger.log((AtnaException) t);
            } else if (t instanceof AuditException) {
                AuditErrorLogger.log((AuditException) t);
            } else if (t instanceof AtnaPersistenceException) {
                PersistenceErrorLogger.log((AtnaPersistenceException) t);
            } else if (t instanceof SyslogException) {
                SyslogErrorLogger.log((SyslogException) t);
            } else {
                ErrorLogger.log(t);
            }
        }
    }
}
