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

package org.openhealthtools.openatna.syslog;


import java.io.OutputStream;
import java.io.Serializable;
import java.io.IOException;

/**
 * Super class for SyslogMessage implementations
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 11:14:40 AM
 * @date $Date:$ modified by $Author:$
 */

public abstract class SyslogMessage implements Serializable {

    private int facility = 0;
    private int severity = 0;
    private String timestamp = "";
    private String hostName = "";
    private LogMessage message;

    protected SyslogMessage(int facility, int severity, String timestamp, String hostName, LogMessage message) {
        this.facility = facility;
        this.severity = severity;
        this.timestamp = timestamp;
        this.hostName = hostName;
        this.message = message;
    }

    public int getFacility() {
        return facility;
    }

    public int getSeverity() {
        return severity;
    }

    public String getHostName() {
        return hostName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public LogMessage getMessage() {
        return message;
    }

    public abstract void write(OutputStream out) throws IOException;

    public abstract byte[] toByteArray() throws IOException;
}