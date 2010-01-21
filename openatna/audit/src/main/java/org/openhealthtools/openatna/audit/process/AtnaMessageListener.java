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

package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.log.SyslogErrorLogger;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 6:23:33 PM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaMessageListener implements SyslogListener<AtnaMessage> {

    private AuditService service;

    public AtnaMessageListener(AuditService service) {
        this.service = service;
    }

    public void messageArrived(SyslogMessage<AtnaMessage> message) {
        LogMessage<AtnaMessage> msg = message.getMessage();
        AtnaMessage atnaMessage = msg.getMessageObject();
        service.process(atnaMessage);
    }

    public void exceptionThrown(SyslogException exception) {
        SyslogErrorLogger.log(exception);
    }
}
