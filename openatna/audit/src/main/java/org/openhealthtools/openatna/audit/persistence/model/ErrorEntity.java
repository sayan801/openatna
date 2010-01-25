/*
 * Copyright (c) 2009-2010 University of Cardiff and others.
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

package org.openhealthtools.openatna.audit.persistence.model;

import java.util.Date;

import org.openhealthtools.openatna.anom.Timestamp;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 24, 2010: 10:45:33 PM
 */

public class ErrorEntity extends PersistentEntity {

    private String timestamp;
    private String stackTrace;
    private String exception;
    private String message;

    public ErrorEntity(Throwable t, String message) {
        this.timestamp = Timestamp.format(new Date());
        if (t != null) {
            this.exception = t.getClass().getName();
            this.stackTrace = formatThrowable(t);
        }
        this.message = message;
    }

    private String formatThrowable(Throwable t) {
        StackTraceElement[] trace = t.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < trace.length; i++) {
            sb.append("[" + i + "]").append(trace[i].toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ErrorEntity that = (ErrorEntity) o;

        if (!exception.equals(that.exception)) {
            return false;
        }
        if (!message.equals(that.message)) {
            return false;
        }
        if (!stackTrace.equals(that.stackTrace)) {
            return false;
        }
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + stackTrace.hashCode();
        result = 31 * result + exception.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName())
                .append(" timestamp=").append(timestamp)
                .append(", message=").append(message);
        if (exception != null) {
            sb.append(", exception=").append(exception)
                    .append("stack trace=").append(stackTrace);
        }
        return sb.toString();
    }
}
