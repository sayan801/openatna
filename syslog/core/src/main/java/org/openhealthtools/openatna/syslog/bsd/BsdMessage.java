/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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

package org.openhealthtools.openatna.syslog.bsd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.message.StringLogMessage;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 11:31:48 AM
 * @date $Date:$ modified by $Author:$
 */

public class BsdMessage<M> extends SyslogMessage {

    private String tag;

    public BsdMessage(int facility, int severity, String timestamp, String hostName, LogMessage<M> message, String tag) {
        super(facility, severity, timestamp, hostName, message);
        if (tag != null && tag.length() == 0) {
            tag = null;
        }
        this.tag = tag;
    }

    public BsdMessage(int facility, int severity, String hostName, LogMessage<M> message, String tag) {
        this(facility, severity, BsdMessageFactory.createDate(new Date()), hostName, message);

    }

    public BsdMessage(int facility, int severity, String timestamp, String hostName, LogMessage<M> message) {
        this(facility, severity, timestamp, hostName, message, null);
    }

    private String getHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(getFacility() * 8 + getSeverity()).append(">")
                .append(getTimestamp()).append(" ")
                .append(getHostName()).append(" ");
        if (tag != null) {
            sb.append(tag);
        }
        return sb.toString();
    }

    public void write(OutputStream out) throws SyslogException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(out, Constants.ENC_UTF8);
            writer.write(getHeader());
            writer.flush();
            getMessage().write(out);
            writer.flush();
        } catch (IOException e) {
            throw new SyslogException(e);
        }
    }

    public byte[] toByteArray() throws SyslogException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            StringBuilder sb = new StringBuilder();
            sb.append(getHeader());
            bout.write(sb.toString().getBytes(Constants.ENC_UTF8));
            getMessage().write(bout);
            return bout.toByteArray();
        } catch (IOException e) {
            throw new SyslogException(e);
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getHeader());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            getMessage().write(bout);
        } catch (SyslogException e) {
            assert false;
        }
        try {
            sb.append(new String(bout.toByteArray(), getMessage().getExpectedEncoding()));
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
        return sb.toString();
    }

    public String getTag() {
        return tag;
    }

    public static void main(String[] args) {

        try {
            BsdMessage m = new BsdMessage(10, 5, "Oct  1 22:14:15", "127.0.0.1", new StringLogMessage("Don't panic"), "ATNALOG");
            SyslogMessageFactory.registerLogMessage("ATNALOG", StringLogMessage.class);
            SyslogMessageFactory.setFactory(new BsdMessageFactory());
            String s = m.toString();
            System.out.println(s);
            SyslogMessage s2 = SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(s.getBytes(Constants.ENC_UTF8)));
            System.out.println(s2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
