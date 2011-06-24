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

package org.openhealthtools.openatna.audit.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.anom.codes.CodeRegistry;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.EntityConverter;
import org.openhealthtools.openatna.audit.process.AtnaMessageListener;
import org.openhealthtools.openatna.audit.process.AtnaProcessor;
import org.openhealthtools.openatna.audit.process.ProcessContext;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.audit.server.AtnaServer;
import org.openhealthtools.openatna.audit.server.ServerConfiguration;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * This pulls together various configurations to create an ATNA Audit service
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 9:27:35 PM
 * @date $Date:$ modified by $Author:$
 */

public class AuditServiceImpl implements AuditService {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.service.AuditServiceImpl");

    private ServerConfiguration serverConfig;
    private ServiceConfiguration serviceConfig = new ServiceConfiguration();
    private ProcessorChain chain = new ProcessorChain();
    private AtnaServer syslogServer;


    /**
     * start the service
     *
     * @throws IOException
     */
    public void start() throws IOException {
        if (serviceConfig.getLogMessageClass() == null) {
            throw new RuntimeException("no log message defined!");
        }

        loadCodes();

        chain.setPolicies(serviceConfig.getPersistencePolicies());
        Map<ProcessorChain.PHASE, List<String>> processors = serviceConfig.getProcessors();
        for (ProcessorChain.PHASE phase : processors.keySet()) {
            List<String> ap = processors.get(phase);
            for (String atnaProcessor : ap) {
                try {
                    AtnaProcessor proc = (AtnaProcessor) Class.forName(atnaProcessor, true,
                            getClass().getClassLoader()).newInstance();
                    chain.addNext(proc, phase);
                } catch (Exception e) {
                    log.warn("Could not load processor " + atnaProcessor);
                }
            }
        }
        if (serverConfig != null) {
            serverConfig.load();
            List<AtnaServer> servers = serverConfig.getServers();
            if (servers.size() == 0) {
                log.warn("Could not start service. No AtnaServers were loaded!");
                return;
            } else {
                this.syslogServer = servers.get(0);
                if (syslogServer != null) {
                    SyslogMessageFactory.setDefaultLogMessage(serviceConfig.getLogMessageClass());
                    syslogServer.start(new AtnaMessageListener(this));
                }
            }
        }
    }

    private void loadCodes() {
        URL defCodes = getClass().getResource("/conf/atnacodes.xml");
        if (defCodes != null) {
            serviceConfig.addCodeUrl(defCodes.toString());
        }
        CodeParser.parse(serviceConfig.getCodeUrls());
        List<AtnaCode> l = CodeRegistry.allCodes();
        CodeDao dao = AtnaFactory.codeDao();
        for (AtnaCode atnaCode : l) {
            CodeEntity ce = EntityConverter.createCode(atnaCode, EntityConverter.getCodeType(atnaCode));
            PersistencePolicies pp = new PersistencePolicies();
            pp.setErrorOnDuplicateInsert(false);
            pp.setAllowNewCodes(true);
            try {
                if (dao.save(ce, pp)) {
                    log.info("loading code:" + atnaCode);
                }
            } catch (AtnaPersistenceException e) {
                log.info("Exception thrown while storing code:" + e.getMessage());
            }
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

    public ServerConfiguration getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfiguration serverConfig) {
        this.serverConfig = serverConfig;
    }


    public void process(AtnaMessage message) throws Exception {
        ProcessContext context = new ProcessContext(message);
        chain.process(context);
    }

    public ServiceConfiguration getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfiguration serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
