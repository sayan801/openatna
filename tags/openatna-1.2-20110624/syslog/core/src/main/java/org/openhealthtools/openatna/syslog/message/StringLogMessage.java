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

package org.openhealthtools.openatna.syslog.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;

/**
 * Simple string implementation of LogMessage.
 * The default encoding is UTF-8
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 15, 2009: 11:47:45 AM
 * @date $Date:$ modified by $Author:$
 */

public class StringLogMessage implements LogMessage<String> {

    private String payload = "";
    private String expectedEncoding = Constants.ENC_UTF8;

    public StringLogMessage() {
    }

    public StringLogMessage(String payload) {
        this(payload, Constants.ENC_UTF8);
    }

    public StringLogMessage(String payload, String expectedEncoding) {
        this.expectedEncoding = expectedEncoding;
        try {
            this.payload = new String(payload.getBytes(), this.expectedEncoding);
        } catch (UnsupportedEncodingException e) {
            this.payload = payload;
        }
    }

    public String getExpectedEncoding() {
        return expectedEncoding;
    }

    public void read(InputStream in, String encoding) throws SyslogException {
        try {
            byte[] bytes = new byte[8198];
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = in.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, c, encoding));
            }
            this.payload = sb.toString();
        } catch (IOException e) {
            throw new SyslogException(e);
        }
    }

    public void write(OutputStream out) throws SyslogException {
        try {
            out.write(payload.getBytes());
            out.flush();
        } catch (IOException e) {
            throw new SyslogException(e, payload.getBytes());
        }
    }

    public String getMessageObject() {
        return payload;
    }
}
