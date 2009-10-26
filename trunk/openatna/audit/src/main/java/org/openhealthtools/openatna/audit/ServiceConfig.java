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

import java.util.*;

import org.openhealthtools.openatna.audit.process.AtnaProcessor;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.PersistencePolicies;
import org.openhealthtools.openatna.syslog.LogMessage;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 4:39:30 PM
 * @date $Date:$ modified by $Author:$
 */

public class ServiceConfig {

    private PersistencePolicies persistencePolicies = new PersistencePolicies();
    private Class<? extends LogMessage> logMessageClass;
    private Map<ProcessorChain.PHASE, List<AtnaProcessor>> processors = new HashMap<ProcessorChain.PHASE, List<AtnaProcessor>>();
    private DaoFactory daoFactory;
    private boolean validationProcessor = true;

    public PersistencePolicies getPersistencePolicies() {
        return persistencePolicies;
    }

    public void setPersistencePolicies(PersistencePolicies persistencePolicies) {
        this.persistencePolicies = persistencePolicies;
    }

    public Class<? extends LogMessage> getLogMessageClass() {
        return logMessageClass;
    }

    public void setLogMessageClass(Class<? extends LogMessage> logMessageClass) {
        this.logMessageClass = logMessageClass;
    }

    public List<AtnaProcessor> getProcessors(ProcessorChain.PHASE phase) {
        return processors.get(phase);
    }

    public Map<ProcessorChain.PHASE, List<AtnaProcessor>> getProcessors() {
        return Collections.unmodifiableMap(processors);
    }

    public void addProcessor(AtnaProcessor processor, ProcessorChain.PHASE phase) {
        List<AtnaProcessor> l = processors.get(phase);
        if (l == null) {
            l = new ArrayList<AtnaProcessor>();
        }
        l.add(processor);
        processors.put(phase, l);
    }

    public DaoFactory getDaoFactory() {
        return daoFactory;
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public boolean isValidationProcessor() {
        return validationProcessor;
    }

    public void setValidationProcessor(boolean validationProcessor) {
        this.validationProcessor = validationProcessor;
    }
}
