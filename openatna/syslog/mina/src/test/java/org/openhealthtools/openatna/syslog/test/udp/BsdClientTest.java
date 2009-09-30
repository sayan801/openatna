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

package org.openhealthtools.openatna.syslog.test.udp;

import org.openhealthtools.openatna.syslog.message.StringLogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.bsd.BsdMessageFactory;
import org.openhealthtools.openatna.syslog.bsd.BsdMessage;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.DatagramSocket;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 2:56:20 PM
 * @date $Date:$ modified by $Author:$
 */

public class BsdClientTest {

    public static void main(String[] args) throws Exception {

        BsdMessage m = new BsdMessage(10, 5, "Oct  1 22:14:15", "127.0.0.1", new StringLogMessage("!Don't panic!"), "ATNALOG");
        System.out.println("message:");
        m.write(System.out);
        SyslogMessageFactory.registerLogMessage("ATNALOG", StringLogMessage.class);
        SyslogMessageFactory.setFactory(new BsdMessageFactory());
        byte[] bytes = m.toByteArray();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, new InetSocketAddress("localhost", 1513));
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);

    }


}