/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */

package org.openhealthtools.openatna.syslog.test.udp;

import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.bsd.BsdMessage;
import org.openhealthtools.openatna.syslog.bsd.BsdMessageFactory;
import org.openhealthtools.openatna.syslog.message.StringLogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class BsdClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BsdClientTest.class);

    public static void main(String[] args) throws Exception {

        BsdMessage m = new BsdMessage(10, 5, "Oct  1 22:14:15", "127.0.0.1",
                new StringLogMessage("!Don't panic!"), "ATNALOG");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        m.write(outputStream);
        LOGGER.info("Output:\n{}", outputStream.toString());

        SyslogMessageFactory.registerLogMessage("ATNALOG", StringLogMessage.class);
        SyslogMessageFactory.setFactory(new BsdMessageFactory());
        byte[] bytes = m.toByteArray();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, new InetSocketAddress("localhost", 1513));
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
    }
}
