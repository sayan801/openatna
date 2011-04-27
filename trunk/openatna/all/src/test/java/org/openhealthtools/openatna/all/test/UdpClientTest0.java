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

import org.junit.Test;
import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.ProvisionalMessage;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

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
            System.out.println("UdpClientTest0.testMessages testing " + messages.size() + " messages");
            for (AtnaMessage message : messages) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message.setEventDateTime(new Date());
                ProtocolMessage sl = new ProtocolMessage(10, 5, "carefx1/10.243.0.118", new JaxbLogMessage(message), "XDS", "AUDIT", "777");
                InetSocketAddress addr = new InetSocketAddress("localhost", 2861);
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

    @Test
    public void testBadMessage() {
        try {
            ProtocolMessage sl = new ProtocolMessage(10, 5, "hildegard", new BadLogMessage(badProv), "Spartacus", "PDQIN", "777");
            InetSocketAddress addr = new InetSocketAddress("localhost", 2861);
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


    String badProv = "This is a bad message";

    String badMsg =
            "<AuditMessage><EventIdentification EventActionCode=\"C\" EventDateTime=\"2010-03-02T15:03:11.115Z\" EventOutcomeIndicator=\"0\"><EventID code=\"110107\" codeSystemName=\"DCM\" displayName=\"Import\" /><EventTypeCode code=\"ITI-14\" codeSystemName=\"IHE Transactions\" displayName=\"Register Document Set\" /></EventIdentification><ActiveParticipant AlternativeUserID=\"CAREFX USER\" UserIsRequestor=\"true\"><RoleIDCode code=\"110153\" codeSystemName=\"DCM\" displayName=\"Source\" /></ActiveParticipant><ActiveParticipant UserID=\"http://localhost:8080/services/xdsregistryb\" UserName=\"CAREFX\" AlternativeUserID=\"altID\" IsRequestor=\"false\"><RoleIDCode code=\"110152\" codeSystemName=\"Desination\" displayName=\"DCM\" /></ActiveParticipant><AuditSourceIdentification AuditEnterpriseSiteID=\"Hospital\" AuditSourceID=\"ReadingRoom\"><AuditSourceTypeCode code=\"1\" /></AuditSourceIdentification><ParticipantObjectIdentification ParticipantObjectID=\"SELF-5^^^&amp;amp;1.3.6.1.4.1.21367.2005.3.7&amp;amp;ISO\" ParticipantObjectTypeCode=\"1\" ParticipantObjectTypeCodeRole=\"1\" ParticipantObjectDataLifeCycle=\"1\" ParticipantObjectName=\"CAREFX\"><ParticipantObjectIDTypeCode code=\"2\" codeSystemName=\"RFC-3381\" displayName=\"Patient Number\" /></ParticipantObjectIdentification><ParticipantObjectIdentification ParticipantObjectID=\"2.16.840.1.114107.1.1.17.1.135001013018002033.126756017\" ParticipantObjectTypeCode=\"2\" ParticipantObjectTypeCodeRole=\"20\"><ParticipantObjectIDTypeCode code=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" codeSystemName=\"IHE XDS Metadata\" displayName=\"submission set classificationNode\" /></ParticipantObjectIdentification></AuditMessage>";


    protected static class ProvLogMessage implements LogMessage<ProvisionalMessage> {

        private ProvisionalMessage msg;

        ProvLogMessage(ProvisionalMessage msg) {
            this.msg = msg;
        }

        public String getExpectedEncoding() {
            return "UTF-8";
        }

        public void read(InputStream in, String encoding) throws SyslogException {
        }

        public void write(OutputStream out) throws SyslogException {
            try {
                out.write(msg.getContent());
                out.flush();
            } catch (IOException e) {
                throw new SyslogException(e);
            }
        }

        public ProvisionalMessage getMessageObject() {
            return msg;
        }
    }

    protected static class BadLogMessage implements LogMessage<String> {

        private String msg;

        BadLogMessage(String msg) {
            this.msg = msg;
        }

        public String getExpectedEncoding() {
            return "UTF-8";
        }

        public void read(InputStream in, String encoding) throws SyslogException {
        }

        public void write(OutputStream out) throws SyslogException {
            try {
                out.write(msg.getBytes("UTF-8"));
                out.flush();
            } catch (IOException e) {
                throw new SyslogException(e);
            }
        }

        public String getMessageObject() {
            return msg;
        }
    }

}
