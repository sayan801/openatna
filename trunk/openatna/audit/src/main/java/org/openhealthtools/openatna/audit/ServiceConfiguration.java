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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.process.AtnaProcessor;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.syslog.LogMessage;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 4:39:30 PM
 * @date $Date:$ modified by $Author:$
 */

public class ServiceConfiguration {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.ServiceConfiguration");

    private PersistencePolicies persistencePolicies = new PersistencePolicies();
    private Class<? extends LogMessage> logMessageClass;
    private List<AtnaProcessor> preVerifyProcessors = new ArrayList<AtnaProcessor>();
    private List<AtnaProcessor> postVerifyProcessors = new ArrayList<AtnaProcessor>();
    private List<AtnaProcessor> postPersistProcessors = new ArrayList<AtnaProcessor>();

    private boolean validationProcessor = true;
    private Set<String> codeUrls = new HashSet<String>();


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

    public Map<ProcessorChain.PHASE, List<AtnaProcessor>> getProcessors() {
        Map<ProcessorChain.PHASE, List<AtnaProcessor>> map = new HashMap<ProcessorChain.PHASE, List<AtnaProcessor>>();
        map.put(ProcessorChain.PHASE.PRE_VERIFY, preVerifyProcessors);
        map.put(ProcessorChain.PHASE.POST_VERIFY, postVerifyProcessors);
        map.put(ProcessorChain.PHASE.POST_PERSIST, postPersistProcessors);
        return map;
    }

    public void addPreVerifyProcessor(AtnaProcessor processor) {
        preVerifyProcessors.add(processor);
    }

    public void addPostVerifyProcessor(AtnaProcessor processor) {
        postVerifyProcessors.add(processor);
    }

    public void addPostPersistProcessor(AtnaProcessor processor) {
        postPersistProcessors.add(processor);
    }

    public List<AtnaProcessor> getPreVerifyProcessors() {
        return preVerifyProcessors;
    }

    public void setPreVerifyProcessors(List<AtnaProcessor> preVerifyProcessors) {
        this.preVerifyProcessors = preVerifyProcessors;
    }

    public List<AtnaProcessor> getPostVerifyProcessors() {
        return postVerifyProcessors;
    }

    public void setPostVerifyProcessors(List<AtnaProcessor> postVerifyProcessors) {
        this.postVerifyProcessors = postVerifyProcessors;
    }

    public List<AtnaProcessor> getPostPersistProcessors() {
        return postPersistProcessors;
    }

    public void setPostPersistProcessors(List<AtnaProcessor> postPersistProcessors) {
        this.postPersistProcessors = postPersistProcessors;
    }

    public void addProcessor(AtnaProcessor processor, ProcessorChain.PHASE phase) {
        switch (phase) {
            case PRE_VERIFY:
                addPreVerifyProcessor(processor);
                break;
            case POST_VERIFY:
                addPostVerifyProcessor(processor);
                break;
            case POST_PERSIST:
                addPostPersistProcessor(processor);
                break;
            default:
                break;

        }
    }

    public boolean isValidationProcessor() {
        return validationProcessor;
    }

    public void setValidationProcessor(boolean validationProcessor) {
        this.validationProcessor = validationProcessor;
    }

    public void addCodeUrl(String url) {
        codeUrls.add(url);
    }

    public Set<String> getCodeUrls() {
        return codeUrls;
    }

    public void setCodeUrls(Set<String> codeUrls) {
        this.codeUrls = codeUrls;
    }
}
