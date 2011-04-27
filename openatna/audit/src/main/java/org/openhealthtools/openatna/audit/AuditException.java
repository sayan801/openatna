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

package org.openhealthtools.openatna.audit;

import org.openhealthtools.openatna.anom.AtnaMessage;

/**
 * Exceptions thrown at the audit layer. This should contain a well-formed
 * Anom message. These errors are the result of policy/security infringements.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 6, 2009: 12:50:56 PM
 * @date $Date:$ modified by $Author:$
 */

public class AuditException extends Exception {

    public static enum AuditError {
        UNDEFINED,
        NULL_MESSAGE,
        INVALID_MESSAGE,

        INVALID_EVENT,
        INVALID_EVENT_CODE,
        INVALID_EVENT_OUTCOME,
        INVALID_EVENT_TIMESTAMP,
        INVALID_EVENT_ACTION,

        INVALID_AUDIT_SOURCE,
        INVALID_AUDIT_SOURCE_ID,
        INVALID_AUDIT_SOURCE_TYPE_CODE,

        INVALID_ACTIVE_PARTICIPANT,
        INVALID_ACTIVE_PARTICIPANT_ID,
        INVALID_ACTIVE_PARTICIPANT_ROLE_ID_CODE,
        INVALID_NETWORK_ACCESS_POINT,

        INVALID_PARTICIPANT_OBJECT_ID,
        INVALID_PARTICIPANT_OBJECT_ID_TYPE_CODE,
        INVALID_PARTICIPANT_OBJECT_TYPE,
        INVALID_PARTICIPANT_OBJECT_TYPE_CODE_ROLE,
        INVALID_PARTICIPANT_OBJECT_SENSITIVITY
    }

    private AtnaMessage atnaMessage;
    private AuditError error = AuditError.UNDEFINED;

    public AuditException(String s, AtnaMessage message, AuditError error) {
        super(s);
        this.atnaMessage = message;
        this.error = error;
    }

    public AuditException(String s, Throwable throwable, AtnaMessage message, AuditError error) {
        super(s, throwable);
        this.atnaMessage = message;
        this.error = error;
    }

    public AuditException(Throwable throwable, AtnaMessage message, AuditError error) {
        super(throwable);
        this.atnaMessage = message;
        this.error = error;
    }

    public AuditException(String s, AtnaMessage message) {
        this(s, message, AuditError.UNDEFINED);
    }

    public AuditException(String s, Throwable throwable, AtnaMessage message) {
        this(s, throwable, message, AuditError.UNDEFINED);
    }

    public AuditException(Throwable throwable, AtnaMessage message) {
        this(throwable, message, AuditError.UNDEFINED);
    }

    public AtnaMessage getAtnaMessage() {
        return atnaMessage;
    }

    public AuditError getError() {
        return error;
    }
}
