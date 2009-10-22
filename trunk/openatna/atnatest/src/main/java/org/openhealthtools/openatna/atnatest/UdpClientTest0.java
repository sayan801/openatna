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

package org.openhealthtools.openatna.atnatest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;
import org.junit.Test;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 4:52:05 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpClientTest0 extends ClientTest {

    @Test
    public void testMessages() {
        try {
            List<AtnaMessage> messages = getMessages();
            for (AtnaMessage message : messages) {
                ProtocolMessage sl = new ProtocolMessage(10, 5, "localhost", new JaxbLogMessage(message), "IHE_XDS", "ATNALOG", "1234");
                InetSocketAddress addr = new InetSocketAddress("localhost", 2863);
                DatagramSocket s = new DatagramSocket();
                byte[] bytes = sl.toByteArray();
                DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length, addr);
                s.send(packet);
                s.close();
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