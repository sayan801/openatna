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

package org.openhealthtools.openatna.persistence;

/**
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 3:24:05 PM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaPersistenceException extends Exception {

    public static enum PersistenceError {
        UNDEFINED,
        CONNECTION_ERROR,
        DATA_ERROR,
        CONSTRAINT_VIOLATION_ERROR,
        LOCK_ERROR,
        GRAMMER_ERROR,
        DATA_ACCESS_ERROR,
        DATA_RETRIEVAL_ERROR,
        NON_EXISTENT_CODE,
        NON_EXISTENT_NETWORK_ACCESS_POINT,
        NON_EXISTENT_PARTICIPANT,
        NON_EXISTENT_OBJECT,
        NON_EXISTENT_SOURCE,
        DUPLICATE_CODE,
        DUPLICATE_NETWORK_ACCESS_POINT,
        DUPLICATE_PARTICIPANT,
        DUPLICATE_OBJECT,
        DUPLICATE_SOURCE,
        NO_PARTICIPANT,
        NO_OBJECT,
        NO_EVENT_ID,
        NO_SOURCE,
        UNMODIFIABLE,
        READ_ONLY
        
    }

    private PersistenceError error = PersistenceError.UNDEFINED;

    public AtnaPersistenceException(String s) {
        super(s);
    }

    public AtnaPersistenceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AtnaPersistenceException(Throwable throwable) {
        super(throwable);
    }

    public AtnaPersistenceException(String s, PersistenceError error) {
        super(s);
        this.error = error;
    }

    public AtnaPersistenceException(String s, Throwable throwable, PersistenceError error) {
        super(s, throwable);
        this.error = error;
    }

    public AtnaPersistenceException(Throwable throwable, PersistenceError error) {
        super(throwable);
        this.error = error;
    }

    public PersistenceError getError() {
        return error;
    }

}
