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

package org.openhealthtools.openatna.audit.persistence;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 3:24:05 PM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaPersistenceException extends Exception {

    public static enum PersistenceError {
        // general DB errors
        UNDEFINED,
        CONNECTION_ERROR,
        DATA_ERROR,
        CONSTRAINT_VIOLATION_ERROR,
        LOCK_ERROR,
        GRAMMER_ERROR,
        DATA_ACCESS_ERROR,
        DATA_RETRIEVAL_ERROR,
        // atna specific errors
        /**
         * no mathcing code in DB
         */
        NON_EXISTENT_CODE,

        /**
         * a matching code exists, but it is of the wrong type
         */
        WRONG_CODE_TYPE,
        /**
         * no matching network access point in DB
         */
        NON_EXISTENT_NETWORK_ACCESS_POINT,
        /**
         * no matching avtive participant in DB
         */
        NON_EXISTENT_PARTICIPANT,
        /**
         * no matching participant Object in DB
         */
        NON_EXISTENT_OBJECT,
        /**
         * no matching audit source in DB
         */
        NON_EXISTENT_SOURCE,
        /**
         * attempt to insert a code that is already in DB
         */
        DUPLICATE_CODE,
        /**
         * attempt to insert a network access point that is already in DB
         */
        DUPLICATE_NETWORK_ACCESS_POINT,
        /**
         * attempt to insert a participant that is already in DB
         */
        DUPLICATE_PARTICIPANT,
        /**
         * attempt to insert a participating object that is already in DB
         */
        DUPLICATE_OBJECT,
        /**
         * attempt to insert an audit source that is already in DB
         */
        DUPLICATE_SOURCE,
        /**
         * no participant in entity.
         */
        NO_PARTICIPANT,
        /**
         * no object defined. This is not a message syntax error, but an entity error.
         * If an AtnaObjectEntity is defined, it should contain an ObjectEntity
         */
        NO_OBJECT,
        /**
         * no Event id code defined
         */
        NO_EVENT_ID,
        /**
         * no audit source defined
         */
        NO_SOURCE,
        /**
         * entity cannot be modified
         */
        UNMODIFIABLE,
        /**
         * entity is read only
         */
        READ_ONLY,
        /**
         * fields not used for comparison do not match
         * representation in DB
         */
        INCONSISTENT_REPRESENTATION,

        /**
         * if an object contains a detail that it does not know about.
         */
        UNKNOWN_DETAIL_TYPE

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
