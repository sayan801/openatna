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

package org.openhealthtools.openatna.syslog.protocol;


import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;

import java.util.*;
import java.io.*;

/**
 * RFC 5424 Syslog message format implementation.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 13, 2009: 11:01:20 AM
 * @date $Date:$ modified by $Author:$
 */

public class ProtocolMessage extends SyslogMessage {

    private String appName = "-";
    private String messageId = "-";
    private String procId = "-";
    private List<StructuredElement> structuredElement = new ArrayList<StructuredElement>();


    public ProtocolMessage(int facility, int severity, String timestamp, String hostName, LogMessage message, String appName, String messageId, String procId) throws SyslogException {
        super(facility, severity, timestamp, hostName, message);
        if (timestamp == null) {
            timestamp = "-";
        }
        if (!timestamp.equals("-")) {
            ProtocolMessageFactory.createDate(timestamp);
        }
        this.appName = appName;
        this.messageId = messageId;
        this.procId = procId;
    }

    public ProtocolMessage(int facility, int severity, String hostName, LogMessage message, String appName, String messageId, String procId) throws SyslogException {
        this(facility, severity, ProtocolMessageFactory.formatDate(new Date()), hostName, message, appName, messageId, procId);
    }

    public ProtocolMessage(int priority, String hostName, LogMessage message, String appName, String messageId) throws SyslogException {
        this(priority / 8, priority % 8, ProtocolMessageFactory.formatDate(new Date()), hostName, message, appName, messageId, "-");
    }

    public String getProcId() {
        return procId;
    }

    public String getAppName() {
        return appName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void addStructuredElement(StructuredElement element) {
        structuredElement.add(element);
    }

    public void removeStructuredElement(StructuredElement element) {
        structuredElement.remove(element);
    }

    private String getHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(getFacility() * 8 + getSeverity()).append(">")
                .append(ProtocolMessageFactory.VERSION_CHAR).append(" ")
                .append(getTimestamp()).append(" ")
                .append(getHostName()).append(" ")
                .append(appName).append(" ")
                .append(procId).append(" ")
                .append(messageId).append(" ");
        return sb.toString();
    }

    public void write(OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, Constants.ENC_UTF8);
        writer.write(getHeader());
        if (structuredElement.size() > 0) {
            for (StructuredElement element : structuredElement) {
                writer.write(element.toString());
            }
        } else {
            writer.write("-");
        }
        writer.write(" ");
        writer.flush();
        getMessage().write(out);
        writer.flush();
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        StringBuilder sb = new StringBuilder();
        sb.append(getHeader());
        if (structuredElement.size() > 0) {
            for (StructuredElement element : structuredElement) {
                sb.append(element.toString());
            }
        } else {
            sb.append("-");
        }
        sb.append(" ");
        bout.write(sb.toString().getBytes(Constants.ENC_UTF8));
        getMessage().write(bout);
        return bout.toByteArray();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getHeader());
        if (structuredElement.size() > 0) {
            for (StructuredElement element : structuredElement) {
                sb.append(element.toString());
            }
        } else {
            sb.append("-");
        }
        sb.append(" ");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            getMessage().write(bout);
        } catch (IOException e) {
            assert false;
        }
        try {
            sb.append(new String(bout.toByteArray(), getMessage().getExpectedEncoding()));
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
        return sb.toString();
    }


}
