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

package org.openhealthtools.openatna.all.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.bsd.BsdMessage;
import org.junit.Test;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 9:59:42 PM
 * @date $Date:$ modified by $Author:$
 */

public class BsdClientTest0 extends ClientTest {

    @Test
    public void testMessages() {
        try {
            List<AtnaMessage> messages = getMessages();
            for (AtnaMessage message : messages) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message.setEventDateTime(new Date());
                BsdMessage m = new BsdMessage(10, 5, "127.0.0.1", new JaxbLogMessage(message), "ATNALOG");

                byte[] bytes = m.toByteArray();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, new InetSocketAddress("10.243.0.153", 2863));
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AtnaException e) {
            e.printStackTrace();
        } catch (SyslogException e) {
            e.printStackTrace();
        }
    }

}
