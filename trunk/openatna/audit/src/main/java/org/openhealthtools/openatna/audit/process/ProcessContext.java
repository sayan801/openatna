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

import java.util.HashMap;
import java.util.Map;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 10:31:12 PM
 * @date $Date:$ modified by $Author:$
 */

public class ProcessContext {

    public static enum State {
        INITIALIZED,
        VALIDATED,
        PERSISTED,
        ABORTED,
        ERROR
    }

    private AtnaMessage message;
    private State state;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private Throwable throwable;
    private PersistencePolicies policies;

    public ProcessContext(AtnaMessage message) {
        this.message = message;
        this.state = State.INITIALIZED;
    }

    public AtnaMessage getMessage() {
        return message;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public PersistencePolicies getPolicies() {
        return policies;
    }

    public void setPolicies(PersistencePolicies policies) {
        this.policies = policies;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void addProperties(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    public void putProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<? extends T> cls) {
        Object val = properties.get(key);
        if (val != null && cls.isAssignableFrom(val.getClass())) {
            return (T) val;
        }
        return null;
    }

}
