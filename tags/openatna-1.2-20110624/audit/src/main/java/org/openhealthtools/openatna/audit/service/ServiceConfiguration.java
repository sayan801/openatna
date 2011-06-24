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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.syslog.LogMessage;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 4:39:30 PM
 * @date $Date:$ modified by $Author:$
 */

public class ServiceConfiguration {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.service.ServiceConfiguration");

    private PersistencePolicies persistencePolicies = new PersistencePolicies();
    private Class<? extends LogMessage> logMessageClass;
    private List<String> preVerifyProcessors = new ArrayList<String>();
    private List<String> postVerifyProcessors = new ArrayList<String>();
    private List<String> postPersistProcessors = new ArrayList<String>();

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

    public Map<ProcessorChain.PHASE, List<String>> getProcessors() {
        Map<ProcessorChain.PHASE, List<String>> map = new HashMap<ProcessorChain.PHASE, List<String>>();
        map.put(ProcessorChain.PHASE.PRE_VERIFY, preVerifyProcessors);
        map.put(ProcessorChain.PHASE.POST_VERIFY, postVerifyProcessors);
        map.put(ProcessorChain.PHASE.POST_PERSIST, postPersistProcessors);
        return map;
    }

    public void addPreVerifyProcessor(String processor) {
        preVerifyProcessors.add(processor);
    }

    public void addPostVerifyProcessor(String processor) {
        postVerifyProcessors.add(processor);
    }

    public void addPostPersistProcessor(String processor) {
        postPersistProcessors.add(processor);
    }

    public List<String> getPreVerifyProcessors() {
        return preVerifyProcessors;
    }

    public void setPreVerifyProcessors(List<String> preVerifyProcessors) {
        this.preVerifyProcessors = preVerifyProcessors;
    }

    public List<String> getPostVerifyProcessors() {
        return postVerifyProcessors;
    }

    public void setPostVerifyProcessors(List<String> postVerifyProcessors) {
        this.postVerifyProcessors = postVerifyProcessors;
    }

    public List<String> getPostPersistProcessors() {
        return postPersistProcessors;
    }

    public void setPostPersistProcessors(List<String> postPersistProcessors) {
        this.postPersistProcessors = postPersistProcessors;
    }

    public void addProcessor(String processor, ProcessorChain.PHASE phase) {
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
