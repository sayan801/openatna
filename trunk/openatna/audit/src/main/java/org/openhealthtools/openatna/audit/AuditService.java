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
import java.util.Set;

import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.audit.process.AtnaMessageListener;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.transport.SyslogServer;

/**
 * At last...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 9:27:35 PM
 * @date $Date:$ modified by $Author:$
 */

public class AuditService {

    public static final String PROPERTY_DAO_FACTORY = AuditService.class.getName() + ".dao.factory";

    private AuditPolicy policy = new AuditPolicy();
    private Class<? extends LogMessage> logMessageClass;
    private SyslogServer syslogServer;
    private Set<URL> codeUrls = new HashSet<URL>();
    private DaoFactory daoFactory;

    public void start() throws IOException {
        if (syslogServer == null) {
            throw new RuntimeException("no server defined!");
        }
        if (getLogMessageClass() == null) {
            throw new RuntimeException("no log message class defined!");
        }
        if (daoFactory == null) {
            daoFactory = SpringDaoFactory.getFactory();
        }
        URL defCodes = Thread.currentThread().getContextClassLoader().getResource("atnacodes.xml");
        addCodeUrls(defCodes);
        CodeParser.parse(getCodeUrls());
        SyslogMessageFactory.setDefaultLogMessage(getLogMessageClass());
        ProcessorChain chain = new ProcessorChain();
        chain.putProperties(policy);
        chain.putProperty(PROPERTY_DAO_FACTORY, daoFactory);
        syslogServer.addSyslogListener(new AtnaMessageListener(chain));
        syslogServer.start();
    }

    public void stop() throws IOException {
        syslogServer.stop();
    }


    public URL[] getCodeUrls() {
        return codeUrls.toArray(new URL[codeUrls.size()]);
    }

    public void addCodeUrls(URL... urls) {
        for (URL url : urls) {
            codeUrls.add(url);
        }
    }

    public SyslogServer getSyslogServer() {
        return syslogServer;
    }

    public void setSyslogServer(SyslogServer syslogServer) {
        this.syslogServer = syslogServer;
    }

    public Class<? extends LogMessage> getLogMessageClass() {
        return logMessageClass;
    }

    public void setLogMessageClass(Class<? extends LogMessage> logMessageClass) {
        this.logMessageClass = logMessageClass;
    }

    public AuditPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(AuditPolicy policy) {
        this.policy = policy;
    }

    public DaoFactory getDaoFactory() {
        return daoFactory;
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
