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

package org.openhealthtools.openatna.audit.process;

import java.util.HashMap;
import java.util.Map;

import org.openhealthtools.openatna.anom.AtnaMessage;

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
        ERROR
    }

    private AtnaMessage message;
    private State state;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private Throwable throwable;

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

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void putProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public <T> T getProperty(String key, Class<? extends T> cls) {
        Object val = properties.get(key);
        if (val != null && cls.isAssignableFrom(val.getClass())) {
            return (T) val;
        }
        return null;
    }

}