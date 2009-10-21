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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;
import org.openhealthtools.openatna.syslog.protocol.SdParam;
import org.openhealthtools.openatna.syslog.protocol.StructuredElement;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 4:52:05 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpClientTest0 {

    public static void main(String[] args) {
        try {

            URL defCodes = Thread.currentThread().getContextClassLoader().getResource("atnacodes.xml");
            if (defCodes != null) {
            }
            CodeParser.parse(defCodes);

            ProtocolMessage sl = new ProtocolMessage(10, 5, "2009-08-14T14:12:23.115Z", "localhost", new JaxbLogMessage(createMessage()), "IHE_XDS", "ATNALOG", "1234");
            List<SdParam> params = new ArrayList<SdParam>();
            params.add(new SdParam("param1", "param value\\=1"));
            params.add(new SdParam("param2", "param value] 2"));
            params.add(new SdParam("param3", "param value 3"));
            params.add(new SdParam("param3", "param value 4"));
            StructuredElement se = new StructuredElement("exampleSDID@1234", params);
            sl.addStructuredElement(se);
            InetSocketAddress addr = new InetSocketAddress("localhost", 2863);

            DatagramSocket s = new DatagramSocket();

            byte[] bytes = sl.toByteArray();
            //for (int i = 0; i < 5; i++) {
            DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length, addr);
            s.send(packet);

            //}

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyslogException e) {
            e.printStackTrace();
        }
    }


    /**
     * this is taken from test-data.xml in the resource/ dir of persistence module
     * To load these sources, objects etc into the db, run Populate.java in the test/ dir of the persistence module
     *
     * @return
     */
    protected static AtnaMessage createMessage() {
        AtnaCode evtCode = IheCodes.eventTypePatientIdFeed();
        AtnaMessage msg = new AtnaMessage(evtCode, EventOutcome.SUCCESS);
        msg.addSource(new AtnaSource("cat").addSourceTypeCode(AtnaCodes.sourceTypeIsoOperatingSoftware()))
                .addParticipant(new AtnaMessageParticipant(new AtnaParticipant("scmabh")))
                .addObject(new AtnaMessageObject(new AtnaObject("obj1", new AtnaCode(AtnaCode.OBJECT_ID_TYPE, "110180", null, "DCM", null, null))));
        msg.getObject("obj1").addObjectDetail(new AtnaObjectDetail().setType("version").setValue("THIS IS DETAIL".getBytes()));
        return msg;
    }

}