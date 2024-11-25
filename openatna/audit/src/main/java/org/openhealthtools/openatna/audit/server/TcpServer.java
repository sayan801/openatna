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

package org.openhealthtools.openatna.audit.server;

import org.openhealthtools.openatna.net.ConnectionFactory;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.net.IServerConnection;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */

public class TcpServer implements Server {

    private static Logger log = LoggerFactory.getLogger("org.openhealthtools.openatna.audit.server.TcpServer");

    private AtnaServer atnaServer;
    private IConnectionDescription tlsConnection;
    private IServerConnection tlsConn = null;
    private boolean running = false;
    private TcpServerThread thread;
    private ServerSocket ss = null;

    public TcpServer(AtnaServer atnaServer, IConnectionDescription tlsConnection) {
        this.atnaServer = atnaServer;
        this.tlsConnection = tlsConnection;
    }

    public void start() {
        tlsConn = ConnectionFactory.getServerConnection(tlsConnection);
        ss = tlsConn.getServerSocket();
        running = true;
        thread = new TcpServerThread(ss);
        thread.start();
        log.info("TLS Server running on port:" + tlsConnection.getPort());
    }

    public void stop() {
        running = false;
        try {
            ss.close();
        } catch (IOException e) {
            log.warn("Unable to close Tcp server socket.", e);
        }
        thread.interrupt();
        tlsConn.closeServerConnection();
        log.info("TLS Server shutting down...");
    }

    private class TcpServerThread extends Thread {

        private ServerSocket server;

        public TcpServerThread(ServerSocket server) {
            this.server = server;
        }

        @Override
        public void run() {
            if (server == null) {
                log.info("Server socket is null. Cannot start server.");
                running = false;
                return;
            }
            while (running && !interrupted()) {
                Socket s = null;
                try {
                    s = server.accept();
                    log.debug(logSocket(s));
                    atnaServer.execute(new WorkerThread(s));
                } catch (RuntimeException e) {
                    throw (e);
                } catch (SocketException e) {
                    log.debug("Socket closed.");
                    log.error("Socket Exception: {}", e.getMessage(), e);
                } catch (IOException e) {
                    SyslogException ex = new SyslogException(e.getMessage(), e);
                    atnaServer.notifyException(ex);
                } catch (Exception e) {
                    SyslogException ex = new SyslogException(e.getMessage(), e);
                    if (s != null) {
                        ex.setSourceIp(((InetSocketAddress) s.getRemoteSocketAddress()).getAddress().getHostAddress());
                    }
                    atnaServer.notifyException(ex);
                }
            }
        }

        private String logSocket(Socket socket) {
            InetSocketAddress local = (InetSocketAddress) socket.getLocalSocketAddress();
            InetSocketAddress addr = (InetSocketAddress) socket.getRemoteSocketAddress();
            return "TCP data received from:" + addr.getHostName() + ":" + addr.getPort()
                    + " to:" + local.getHostName() + ":" + local.getPort();
        }
    }

    private class WorkerThread extends Thread {
        private Socket socket;

        private WorkerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                while (true) {

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
                            log.debug("length of incoming message: '{}'", length);
                        } catch (NumberFormatException e) {
                            SyslogException ex = new SyslogException(e, b);
                            ex.setSourceIp(((InetSocketAddress) socket.getRemoteSocketAddress())
                                    .getAddress().getHostAddress());
                            atnaServer.notifyException(ex);
                            break;
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
                        log.debug("read in '{}' bytes to convert to message.", len);
                        SyslogMessage msg = null;
                        try {
                            msg = createMessage(bytes);
                        } catch (SyslogException e) {
                            e.setBytes(bytes);
                            e.setSourceIp(((InetSocketAddress) socket.getRemoteSocketAddress())
                                    .getAddress().getHostAddress());
                            atnaServer.notifyException(e);
                        }
                        if (msg != null) {
                            InetSocketAddress addr = (InetSocketAddress) socket.getRemoteSocketAddress();
                            msg.setSourceIp(addr.getAddress().getHostAddress());
                            atnaServer.notifyListeners(msg);
                        }
                    }
                }

            } catch (IOException e) {
                SyslogException ex = new SyslogException(e);
                ex.setSourceIp(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress());
                atnaServer.notifyException(ex);
            }

        }

        private SyslogMessage createMessage(byte[] bytes) throws SyslogException {
            if (log.isDebugEnabled()) {
                try {
                    log.debug("creating message from bytes: " + new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }
            return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(bytes));
        }
    }
}
