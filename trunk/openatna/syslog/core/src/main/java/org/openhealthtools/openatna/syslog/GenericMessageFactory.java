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

import org.openhealthtools.openatna.syslog.bsd.BsdMessageFactory;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessageFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Reads in data and creates BSD style syslog messages
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 11:48:37 AM
 * @date $Date:$ modified by $Author:$
 */

public class GenericMessageFactory extends SyslogMessageFactory {

    private static BsdMessageFactory bsdFactory = new BsdMessageFactory();
    private static ProtocolMessageFactory protFactory = new ProtocolMessageFactory();


    /**
     * returns true if this is a BSD syslog message.
     * Relies on the 5424 VERSION value being '1'
     * otherwise false
     *
     * @param bytes
     * @return
     * @throws org.openhealthtools.openatna.syslog.SyslogException
     *
     */
    private boolean isBSD(byte[] bytes) throws SyslogException {
        try {
            boolean isBSD = true;
            String s = new String(bytes);
            int close = s.indexOf('>');
            if (close > 1 && close < bytes.length - 2) {
                char next = s.charAt(close + 1);
                if (next == Constants.VERSION) {
                    next = s.charAt(close + 2);
                    if (next == ' ') {
                        isBSD = false;
                    }
                }
            }
            return isBSD;
        } catch (Exception e) {
            throw new SyslogException(e);
        }
    }

    public SyslogMessage read(InputStream in) throws SyslogException {
        try {
            PushbackInputStream pin = new PushbackInputStream(in, 7);
            byte[] bytes = new byte[7];
            pin.read(bytes);

            boolean bsd = isBSD(bytes);
            pin.unread(bytes);
            if (bsd) {
                return bsdFactory.read(pin);
            } else {
                return protFactory.read(pin);
            }
        } catch (IOException e) {
            throw new SyslogException(e);
        }
    }
}