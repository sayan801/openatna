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

package org.openhealthtools.openatna.audit;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.audit.process.AtnaMessageListener;
import org.openhealthtools.openatna.audit.process.AtnaProcessor;
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

public class AuditService {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.AuditService");


    public static final String PROPERTY_DAO_FACTORY = AuditService.class.getName() + ".dao.factory";
    public static final String PROPERTY_PERSISTENCE_POLICIES = AuditService.class.getName() + ".persistence.policies";

    private SyslogServer syslogServer;
    private Set<URL> codeUrls = new HashSet<URL>();
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
        SyslogMessageFactory.setDefaultLogMessage(serviceConfig.getLogMessageClass());
        if (syslogServer == null) {
            throw new RuntimeException("no server defined!");
        }
        if (serviceConfig.getDaoFactory() == null) {
            serviceConfig.setDaoFactory(SpringDaoFactory.getFactory());
        }
        URL defCodes = getClass().getResource("/conf/atnacodes.xml");
        addCodeUrls(defCodes);
        CodeParser.parse(getCodeUrls());
        chain.putProperty(PROPERTY_PERSISTENCE_POLICIES, serviceConfig.getPersistencePolicies());
        chain.putProperty(PROPERTY_DAO_FACTORY, serviceConfig.getDaoFactory());
        List<AtnaProcessor> processors = serviceConfig.getProcessors();
        for (AtnaProcessor processor : processors) {
            chain.addNext(processor);
        }
        syslogServer.addSyslogListener(new AtnaMessageListener(chain));
        syslogServer.start();
    }

    /**
     * stop the service
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        syslogServer.stop();
    }

    /**
     * get the URLs pointing to coded value XML.
     * default codes are added automatically just before starting the service
     *
     * @return
     */
    public URL[] getCodeUrls() {
        return codeUrls.toArray(new URL[codeUrls.size()]);
    }

    /**
     * add URLs pointing to XML files containing Coded Values.
     * default codes are added automatically
     *
     * @param urls
     */
    public void addCodeUrls(URL... urls) {
        for (URL url : urls) {
            if (url != null) {
                log.debug("loading Codes from url:" + url);
                codeUrls.add(url);
            }
        }
    }

    /**
     * set the URLs pointing to Coded Values XML
     * default codes are added automatically
     *
     * @param urls
     */
    public void setCodeUrls(URL[] urls) {
        codeUrls.clear();
        addCodeUrls(urls);
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

    /**
     * get the processing chain that will be invoked when a message arrives.
     *
     * @return
     */
    public ProcessorChain getChain() {
        return chain;
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
