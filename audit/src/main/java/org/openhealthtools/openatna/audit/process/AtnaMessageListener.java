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

import java.util.Date;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.log.PersistenceErrorLogger;
import org.openhealthtools.openatna.audit.log.SyslogErrorLogger;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.openhealthtools.openatna.audit.service.ServiceConfiguration;
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
        atnaMessage.setSourceAddress(message.getSourceIp());
        try {
            service.process(atnaMessage);
        } catch (Exception e) {
            byte[] bytes = "no message available".getBytes();
            try {
                bytes = message.toByteArray();
            } catch (SyslogException e1) {

            }
            SyslogException ex = new SyslogException(e.getMessage(), e, bytes);
            if (message.getSourceIp() != null) {
                ex.setSourceIp(message.getSourceIp());
            }
            exceptionThrown(ex);
        }
    }

    public void exceptionThrown(SyslogException exception) {
        SyslogErrorLogger.log(exception);
        ServiceConfiguration config = service.getServiceConfig();
        if (config != null) {
            PersistencePolicies pp = config.getPersistencePolicies();
            if (pp != null) {
                if (pp.isPersistErrors()) {
                    ErrorDao dao = AtnaFactory.errorDao();
                    ErrorEntity ent = createEntity(exception);
                    synchronized (this) {
                        try {
                            dao.save(ent);
                        } catch (AtnaPersistenceException e) {
                            PersistenceErrorLogger.log(e);
                        }
                    }
                }
            }
        }
    }

    private ErrorEntity createEntity(SyslogException e) {
        ErrorEntity ent = new ErrorEntity();
        ent.setErrorTimestamp(new Date());

        if (e.getBytes() != null) {
            ent.setPayload(e.getBytes());
        }
        if (e.getSourceIp() != null) {
            ent.setSourceIp(e.getSourceIp());
        }
        if (e.getMessage() != null) {
            ent.setErrorMessage(e.getClass().getName() + ":" + e.getMessage());
        }
        ent.setStackTrace(createStackTrace(e));
        return ent;
    }

    private byte[] createStackTrace(Throwable e) {
        StringBuilder sb = new StringBuilder();

        StackTraceElement[] els = e.getStackTrace();
        for (StackTraceElement el : els) {
            sb.append(el.toString()).append("\n\t");
        }
        e = e.getCause();
        while (e != null) {
            sb.append("\n\tCaused by:\n");
            els = e.getStackTrace();
            for (StackTraceElement el : els) {
                sb.append(el.toString()).append("\n\t");
            }
            e = e.getCause();
        }

        return sb.toString().getBytes();
    }
}
