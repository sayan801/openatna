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

package org.openhealthtools.openatna.syslog.test.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openatna.syslog.message.StringLogMessage;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;
import org.openhealthtools.openatna.syslog.protocol.SdParam;
import org.openhealthtools.openatna.syslog.protocol.StructuredElement;

/**
 * runs a RFC 5426 UDP client. This sends some dummy XML.
 * The server is set to read XML with the XmlLogMessage class.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 2:56:20 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpClientTest {

    public static void main(String[] args) throws Exception {

        ProtocolMessage m =
                new ProtocolMessage(10, 5, "2009-08-14T14:12:23.115Z", "localhost", new StringLogMessage("<atna></atna>"), "IHE_XDS", "ATNALOG",
                        "1234");
        List<SdParam> params = new ArrayList<SdParam>();
        params.add(new SdParam("param1", "param value\\=1"));
        params.add(new SdParam("param2", "param value] 2"));
        params.add(new SdParam("param3", "param value 3"));
        params.add(new SdParam("param3", "param value 4"));
        StructuredElement se = new StructuredElement("exampleSDID@1234", params);
        m.addStructuredElement(se);
        List<SdParam> ps = se.getParams();
        for (SdParam p : ps) {
            System.out.println("parameter value:" + p.getValue());
        }
        byte[] bytes = m.toByteArray();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, new InetSocketAddress("localhost", 1513));
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);


    }


}
