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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;

/**
 * A chain for processors.
 * <p/>
 * The addNext() method is relative to the previous call to one of the add* methods.
 * addFirst() resets the "next" counter to 1.
 * addLast() resets the "next" counter to the size of the list of processors.
 * addNext() increments the "next" counter.
 * <p/>
 * The "next" counter starts off with a value of 0.
 * <p/>
 * The chain will persist the atna message, if no previous processors have set the
 * state of the context to PERSISTED. This means only domain/business processors
 * need to be added manually.
 * <p/>
 * It will also validate the message before passing it to any other processors.
 * To not use the built-in validator, use the constructor with the boolean set to false.
 * <p/>
 * If a processor throws an exception during processing, then the chain is unwound (shameless
 * copy of CXF). The exception is set on the context and each
 * processor that has been called gets its error(context) method called in reverse order.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 9:07:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class ProcessorChain {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.process.ProcessorChain");

    public static enum PHASE {
        PRE_VERIFY,
        POST_VERIFY,
        POST_PERSIST,
    }

    private ProvisionalProcessor prov = new ProvisionalProcessor();
    private ExceptionProcessor except = new ExceptionProcessor();
    private ValidationProcessor validator = new ValidationProcessor();
    private PersistenceProcessor persist = new PersistenceProcessor();
    private PhaseProcessor preVerify = new PhaseProcessor();
    private PhaseProcessor postVerify = new PhaseProcessor();
    private PhaseProcessor postPersist = new PhaseProcessor();
    private boolean validate;
    private Map<String, Object> contextProperties = new HashMap<String, Object>();
    private PersistencePolicies policies = new PersistencePolicies();

    public ProcessorChain(boolean validate) {
        this.validate = validate;
    }

    public ProcessorChain() {
        this(true);
    }

    public ProcessorChain addFirst(AtnaProcessor processor, PHASE phase) {
        switch (phase) {
            case PRE_VERIFY:
                preVerify.addFirst(processor);
                break;
            case POST_VERIFY:
                postVerify.addFirst(processor);
                break;
            case POST_PERSIST:
                postPersist.addFirst(processor);
                break;
            default:
                break;
        }
        return this;
    }

    public ProcessorChain addLast(AtnaProcessor processor, PHASE phase) {
        switch (phase) {
            case PRE_VERIFY:
                preVerify.addLast(processor);
                break;
            case POST_VERIFY:
                postVerify.addLast(processor);
                break;
            case POST_PERSIST:
                postPersist.addLast(processor);
                break;
            default:
                break;
        }
        return this;
    }

    public ProcessorChain addNext(AtnaProcessor processor, PHASE phase) {
        switch (phase) {
            case PRE_VERIFY:
                preVerify.addNext(processor);
                break;
            case POST_VERIFY:
                postVerify.addNext(processor);
                break;
            case POST_PERSIST:
                postPersist.addNext(processor);
                break;
            default:
                break;
        }
        return this;
    }

    public void process(ProcessContext context) throws Exception {
        long before = System.currentTimeMillis();
        context.addProperties(Collections.unmodifiableMap(contextProperties));
        context.setPolicies(getPolicies());
        List<AtnaProcessor> done = new ArrayList<AtnaProcessor>();
        Exception ex = null;
        try {
            prov.process(context);
            if (context.getState() == ProcessContext.State.ABORTED) {
                log.debug("chain aborted after provisional processor");
                return;
            }
            except.process(context);
            done.add(except);
            if (context.getState() == ProcessContext.State.ABORTED) {
                log.debug("chain aborted after exception processor");
                return;
            }
            preVerify.process(context);
            done.add(preVerify);
            if (context.getState() == ProcessContext.State.ABORTED) {
                log.debug("chain aborted after preverify processors");
                return;
            }
            if (validate) {
                validator.process(context);
                done.add(validator);
            }
            postVerify.process(context);
            done.add(postVerify);
            if (context.getState() == ProcessContext.State.ABORTED) {
                log.debug("chain aborted after postverify processor");
                return;
            }
            if (context.getState() != ProcessContext.State.PERSISTED) {
                log.debug("about to persist message");
                persist.process(context);
                done.add(persist);
            }
            postPersist.process(context);
            done.add(postPersist);
        } catch (Exception e) {
            ex = e;
            context.setState(ProcessContext.State.ERROR);
            context.setThrowable(e);
            rewind(done, context);
        }
        long now = System.currentTimeMillis();
        log.debug("message processing time:" + (now - before));
        if (ex != null) {
            throw ex;
        }
    }

    private void rewind(List<AtnaProcessor> completed, ProcessContext context) {
        for (int i = completed.size() - 1; i >= 0; i--) {
            AtnaProcessor ap = completed.get(i);
            ap.error(context);
        }
    }

    public PersistencePolicies getPolicies() {
        return policies;
    }

    public void setPolicies(PersistencePolicies policies) {
        this.policies = policies;
    }

    public void putProperty(String key, Object value) {
        contextProperties.put(key, value);
    }

    public void putProperties(Map<String, Object> props) {
        contextProperties.putAll(props);
    }

    public Object getProperty(String key) {
        return contextProperties.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<? extends T> cls) {
        Object val = contextProperties.get(key);
        if (val != null && cls.isAssignableFrom(val.getClass())) {
            return (T) val;
        }
        return null;
    }

}
