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
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.net.ConnectionFactory;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.net.IServerConnection;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 1:41:18 PM
 * @date $Date:$ modified by $Author:$
 */

public class TcpServer {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.TcpServer");


    private AtnaServer atnaServer;
    private IConnectionDescription tlsConnection;
    private IServerConnection tlsConn = null;
    private boolean running = false;
    private TcpServerThread thread;

    public TcpServer(AtnaServer atnaServer, IConnectionDescription tlsConnection) {
        this.atnaServer = atnaServer;
        this.tlsConnection = tlsConnection;
    }

    public void start() {
        tlsConn = ConnectionFactory.getServerConnection(tlsConnection);
        ServerSocket ss = tlsConn.getServerSocket();
        running = true;
        thread = new TcpServerThread(ss);
        thread.start();
        log.debug("TLS Server running on port:" + tlsConnection.getPort());
    }

    public void stop() {
        running = false;
        thread.interrupt();
        tlsConn.closeServerConnection();
        log.debug("TLS Server shutting down...");
    }


    private class TcpServerThread extends Thread {

        private ServerSocket server;

        public TcpServerThread(ServerSocket server) {
            this.server = server;
        }

        public void run() {

            while (running && !interrupted()) {
                try {
                    Socket s = server.accept();
                    log.debug(logSocket(s));
                    atnaServer.execute(new WorkerThread(s));
                } catch (SocketException e) {
                    log.debug("Socket closed.");
                } catch (IOException e) {
                    atnaServer.notifyException(new SyslogException(e));
                }
            }
        }

        private String logSocket(Socket socket) {
            InetSocketAddress local = (InetSocketAddress) socket.getLocalSocketAddress();
            InetSocketAddress addr = (InetSocketAddress) socket.getRemoteSocketAddress();
            return "TCP data received from:" + addr.getHostName() + ":" + addr.getPort() +
                    " to:" + local.getHostName() + ":" + local.getPort();
        }
    }

    private class WorkerThread extends Thread {
        private Socket socket;

        private WorkerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream in = socket.getInputStream();
                int maxErr = 3;
                while (true) {
                    if (maxErr == 0) {
                        return;
                    }
                    byte[] b = new byte[128];
                    int count = 0;
                    while (count < b.length) {
                        int c = in.read();
                        if (c == -1) {
                            return;
                        }
                        if ((c & 0xff) == ' ') {
                            break;
                        }
                        b[count++] = (byte) c;
                    }
                    if (count > 0) {
                        int length;
                        try {
                            length = Integer.parseInt(new String(b, 0, count));
                        } catch (NumberFormatException e) {
                            maxErr--;
                            atnaServer.notifyException(new SyslogException(e, b));
                            continue;
                        }
                        byte[] bytes = new byte[length];
                        int len = in.read(bytes);
                        while (len < length) {
                            int curr = in.read(bytes, len, bytes.length - len);
                            if (curr == -1) {
                                break;
                            }
                            len += curr;
                        }
                        SyslogMessage msg = null;
                        try {
                            msg = createMessage(bytes);
                        } catch (SyslogException e) {
                            maxErr--;
                            atnaServer.notifyException(new SyslogException(e, bytes));
                        }
                        if (msg != null) {
                            atnaServer.notifyListeners(msg);
                        }
                    }
                }

            } catch (IOException e) {
                atnaServer.notifyException(new SyslogException(e));
            }

        }

        private SyslogMessage createMessage(byte[] bytes) throws SyslogException, IOException {
            log.debug("creating message from bytes: " + new String(bytes));
            return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(bytes));
        }


    }


}
