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

package org.openhealthtools.openatna.audit.impl;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.audit.AuditService;
import org.openhealthtools.openatna.audit.ServiceConfig;
import org.openhealthtools.openatna.audit.process.AtnaMessageListener;
import org.openhealthtools.openatna.audit.process.AtnaProcessor;
import org.openhealthtools.openatna.audit.process.ProcessContext;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.transport.SyslogServer;

/**
 * This pulls together various configurations to create an ATNA Audit service
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 9:27:35 PM
 * @date $Date:$ modified by $Author:$
 */

public class AuditServiceImpl implements AuditService {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.impl.AuditServiceImpl");

    private SyslogServer syslogServer;
    private ServiceConfig serviceConfig = new ServiceConfig();
    private ProcessorChain chain = new ProcessorChain();

    /**
     * start the service
     *
     * @throws IOException
     */
    public void start() throws IOException {
        if (serviceConfig.getLogMessageClass() == null) {
            throw new RuntimeException("no log message defined!");
        }
        if (serviceConfig.getDaoFactory() == null) {
            serviceConfig.setDaoFactory(SpringDaoFactory.getFactory());
        }
        URL defCodes = getClass().getResource("/conf/atnacodes.xml");

        serviceConfig.addCodeUrls(defCodes);
        CodeParser.parse(serviceConfig.getCodeUrls());
        chain.putProperty(AuditService.PROPERTY_PERSISTENCE_POLICIES, serviceConfig.getPersistencePolicies());
        chain.putProperty(AuditService.PROPERTY_DAO_FACTORY, serviceConfig.getDaoFactory());
        Map<ProcessorChain.PHASE, List<AtnaProcessor>> processors = serviceConfig.getProcessors();
        for (ProcessorChain.PHASE phase : processors.keySet()) {
            List<AtnaProcessor> ap = processors.get(phase);
            for (AtnaProcessor atnaProcessor : ap) {
                chain.addNext(atnaProcessor, phase);
            }
        }
        if (syslogServer != null) {
            SyslogMessageFactory.setDefaultLogMessage(serviceConfig.getLogMessageClass());
            syslogServer.addSyslogListener(new AtnaMessageListener(this));
            syslogServer.start();
        }
    }

    /**
     * stop the service
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        if (syslogServer != null) {
            syslogServer.stop();
        }
    }


    /**
     * get the syslog server that will receive the messages.
     * This should be fully configured, including have the LogMessage set.
     *
     * @return
     */
    public SyslogServer getSyslogServer() {
        return syslogServer;
    }

    /**
     * set the fully configured syslog server
     *
     * @param syslogServer
     */
    public void setSyslogServer(SyslogServer syslogServer) {
        this.syslogServer = syslogServer;
    }

    public void process(AtnaMessage message) {
        ProcessContext context = new ProcessContext(message);
        chain.process(context);
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
