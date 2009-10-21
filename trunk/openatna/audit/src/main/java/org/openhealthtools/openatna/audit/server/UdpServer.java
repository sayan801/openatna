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

package org.openhealthtools.openatna.audit.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.misyshealthcare.connect.net.IConnectionDescription;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.server.udp.UdpServerConnection;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 11:47:14 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpServer {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.UdpServer");

    private AtnaServer atnaServer;
    private IConnectionDescription tlsConnection;
    private UdpServerConnection udpConn = null;
    private boolean running = false;
    private UdpServerThread thread;

    public UdpServer(AtnaServer atnaServer, IConnectionDescription tlsConnection) {
        this.atnaServer = atnaServer;
        this.tlsConnection = tlsConnection;
    }

    public void start() throws IOException {
        udpConn = new UdpServerConnection(tlsConnection);
        DatagramSocket socket = udpConn.getSocket();
        thread = new UdpServerThread(socket);
        running = true;
        thread.start();

    }

    public void stop() {
        running = false;
        thread.interrupt();
        udpConn.closeServerConnection();
    }

    private class UdpServerThread extends Thread {

        private final DatagramSocket socket;

        private UdpServerThread(DatagramSocket socket) {
            this.socket = socket;
        }

        public void run() {
            log.debug(" socket port is:" + socket.getLocalPort());
            while (running && !interrupted()) {
                try {
                    byte[] buffer = new byte[udpConn.getMaxTransmissionUnit()];
                    DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                    socket.receive(packet);
                    atnaServer.execute(new UdpReceiver(packet));
                }
                catch (SocketException x) {
                    log.debug("Socket Exception thrown.");
                }
                catch (SocketTimeoutException x) {
                    x.printStackTrace();
                    // Timed out, but the socket is still valid, don't shut down
                }
                catch (IOException x) {
                    x.printStackTrace();
                    break;
                }
            }
            log.debug(" exiting udp server thread.");
        }
    }

    private class UdpReceiver implements Runnable {

        private DatagramPacket packet;

        private UdpReceiver(DatagramPacket packet) {
            this.packet = packet;
        }

        public void run() {
            SyslogMessage msg = null;
            try {
                byte[] data = new byte[packet.getLength()];
                log.debug(" getting bytes from DatagramPacket with a length of " + data.length);
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, data.length);
                msg = createMessage(data);
            } catch (SyslogException e) {
                atnaServer.notifyException(e);
            } catch (IOException e) {
                atnaServer.notifyException(new SyslogException(e));
            }
            if (msg != null) {
                atnaServer.notifyListeners(msg);
            }
        }

        private SyslogMessage createMessage(byte[] bytes) throws SyslogException, IOException {
            return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(bytes));
        }
    }
}
