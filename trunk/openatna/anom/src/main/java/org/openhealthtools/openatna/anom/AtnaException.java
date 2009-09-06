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

package org.openhealthtools.openatna.anom;

/**
 * Exception to be thrown if there are errors in the message format or
 * (de)serialization.
 * <p/>
 * This may contain a message, but this is not guarranteed, as the exception may have
 * benn thrown before a message was available.
 * <p/>
 * Therefore, the message may be null
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 4:15:28 PM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaException extends Exception {

    public static enum AtnaError {

        UNDEFINED,

        NO_EVENT,
        NO_EVENT_CODE,
        NO_EVENT_OUTCOME,
        NO_EVENT_TIMESTAMP,
        INVALID_EVENT_TIMESTAMP,

        NO_AUDIT_SOURCE,
        NO_AUDIT_SOURCE_ID,

        NO_ACTIVE_PARTICIPANT,
        NO_ACTIVE_PARTICIPANT_ID,

        NO_PARTICIPANT_OBJECT_ID,
        NO_PARTICIPANT_OBJECT_ID_TYPE_CODE

    }

    private AtnaMessage message;
    private AtnaError error = AtnaError.UNDEFINED;

    public AtnaException(String s) {
        this(s, AtnaError.UNDEFINED);
    }

    public AtnaException(String s, Throwable throwable) {
        this(s, throwable, AtnaError.UNDEFINED);
    }

    public AtnaException(Throwable throwable) {
        this(throwable, AtnaError.UNDEFINED);
    }

    public AtnaException(String s, AtnaMessage message) {
        this(s, message, AtnaError.UNDEFINED);
    }

    public AtnaException(String s, Throwable throwable, AtnaMessage message) {
        this(s, throwable, message, AtnaError.UNDEFINED);
    }

    public AtnaException(Throwable throwable, AtnaMessage message) {
        this(throwable, message, AtnaError.UNDEFINED);
    }

    public AtnaException(String s, AtnaError error) {
        this(s, (AtnaMessage) null, error);
    }

    public AtnaException(String s, Throwable throwable, AtnaError error) {
        this(s, throwable, null, error);
    }

    public AtnaException(Throwable throwable, AtnaError error) {
        this(throwable, null, error);
    }

    public AtnaException(String s, AtnaMessage message, AtnaError error) {
        super(s);
        this.message = message;
        this.error = error;
    }

    public AtnaException(String s, Throwable throwable, AtnaMessage message, AtnaError error) {
        super(s, throwable);
        this.message = message;
        this.error = error;
    }

    public AtnaException(Throwable throwable, AtnaMessage message, AtnaError error) {
        super(throwable);
        this.message = message;
        this.error = error;
    }

    public AtnaMessage getAtnaMessage() {
        return message;
    }

    public AtnaError getError() {
        return error;
    }
}
