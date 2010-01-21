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

package org.openhealthtools.openatna.all.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.ProvisionalMessage;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;
import org.junit.Test;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 4:52:05 PM
 * @date $Date:$ modified by $Author:$
 * <p/>
 * <85>1 2009-11-29T15:15:19Z hildegard Spartacus 777 PDQIN - ?<AuditMessage>
 * <EventIdentification EventActionCode="E" EventDateTime="2009-11-29T15:15:19" EventOutcomeIndicator="0">
 * <EventID code="110100" codeSystemName="DCM" displayName="Application Activity" />
 * <EventTypeCode code="110120" codeSystemName="DCM" displayName="Application Start" />
 * </EventIdentification>
 * <ActiveParticipant UserID="MESA Application" AlternativeUserID="MESA AE Title" UserIsRequestor="false">
 * <RoleIDCode code="110150" codeSystemName="DCM" displayName="Application"/>
 * </ActiveParticipant>
 * <ActiveParticipant UserID="smm" UserName="Steve Moore" UserIsRequestor="true">
 * <RoleIDCode code="110151" codeSystemName="DCM" displayName="Application Launcher"/>
 * </ActiveParticipant>
 * <AuditSourceIdentification AuditEnterpriseSiteID="Hospital" AuditSourceID="ReadingRoom">
 * <AuditSourceTypeCode code="1"/>
 * </AuditSourceIdentification>
 * </AuditMessage>
 */

public class UdpClientTest0 extends ClientTest {

    @Test
    public void testMessages() {
        try {
            List<AtnaMessage> messages = getMessages();
            for (AtnaMessage message : messages) {
                ProtocolMessage sl = new ProtocolMessage(10, 5, "hildegard", new JaxbLogMessage(message), "Spartacus", "PDQIN", "777");
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

    @Test
    public void testProvMessage() {
        try {
            ProvisionalMessage message = new ProvisionalMessage(prov.getBytes("UTF-8"));
            ProtocolMessage sl = new ProtocolMessage(10, 5, "hildegard", new ProvLogMessage(message), "Spartacus", "PDQIN", "777");
            InetSocketAddress addr = new InetSocketAddress("localhost", 2863);
            DatagramSocket s = new DatagramSocket();
            byte[] bytes = sl.toByteArray();
            DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length, addr);
            s.send(packet);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyslogException e) {
            e.printStackTrace();
        }
    }

    String prov = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<IHEYr4>Stuff we don't care about</IHEYr4>";

    static class ProvLogMessage implements LogMessage<ProvisionalMessage> {

        private ProvisionalMessage msg;

        ProvLogMessage(ProvisionalMessage msg) {
            this.msg = msg;
        }

        public String getExpectedEncoding() {
            return "UTF-8";
        }

        public void read(InputStream in, String encoding) throws IOException {
        }

        public void write(OutputStream out) throws IOException {
            out.write(msg.getContent());
            out.flush();
        }

        public ProvisionalMessage getMessageObject() {
            return msg;
        }
    }

}