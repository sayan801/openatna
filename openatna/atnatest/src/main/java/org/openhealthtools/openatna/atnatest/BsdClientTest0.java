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

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.DatagramSocket;
import java.io.IOException;

import org.openhealthtools.openatna.syslog.bsd.BsdMessage;
import org.openhealthtools.openatna.syslog.bsd.BsdMessageFactory;
import org.openhealthtools.openatna.syslog.message.StringLogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.audit.process.AtnaLogMessage;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 9:59:42 PM
 * @date $Date:$ modified by $Author:$
 */

public class BsdClientTest0 {

    public static void main(String[] args) {
        try {
            BsdMessage m = new BsdMessage(10, 5, "Oct  1 22:14:15", "127.0.0.1", new JaxbLogMessage(createMessage()), "ATNALOG");
            System.out.println("message:");
            m.write(System.out);
            byte[] bytes = m.toByteArray();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, new InetSocketAddress("localhost", 1513));
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this is taken from test-data.xml in the resource/ dir of persistence module
     * To load these sources, objects etc into the db, run Populate.java in the test/ dir of the persistence module
     * @return
     */
    protected static AtnaMessage createMessage() {
        AtnaCode evtCode = IheCodes.eventTypePatientIdFeed();
        AtnaMessage msg = new AtnaMessage(evtCode, EventOutcome.SUCCESS);
        msg.addSource(new AtnaSource("cat").addSourceTypeCode(AtnaCodes.sourceTypeIsoOperatingSoftware()))
                .addParticipant(new AtnaMessageParticipant(new AtnaParticipant("scmabh")))
                .addObject(new AtnaMessageObject(new AtnaObject("obj1", new AtnaCode("110180", null, "DCM"))));
        msg.getObject("obj1").addObjectDetail(new AtnaObjectDetail().setType("version").setValue("THIS IS DETAIL".getBytes()));
        return msg;
    }
}
