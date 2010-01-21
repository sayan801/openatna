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

package org.openhealthtools.openatna.syslog.core.test.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.core.test.tls.ssl.AuthSSLSocketFactory;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

/**
 * very simple BIO test
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 31, 2009: 11:18:58 PM
 * @date $Date:$ modified by $Author:$
 */

public class TlsServer {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.core.test.tls.TlsServer");


    private TlsConfig tlsconfig;
    private Executor exec = Executors.newFixedThreadPool(5);
    private boolean stopped = false;
    private ServerSocket serverSocket;
    private ServerThread thread;

    private Set<SyslogListener> listeners = new HashSet<SyslogListener>();

    public void configure(TlsConfig config) {
        this.tlsconfig = config;
    }

    public void start() throws IOException {
        String host = tlsconfig.getHost();
        if (host == null) {
            host = InetAddress.getLocalHost().getHostAddress();
        }

        AuthSSLSocketFactory f = tlsconfig.getSocketFactory();
        if (f != null) {
            boolean auth = tlsconfig.isRequireClientAuth();
            log.info("USING TLS...");
            serverSocket = f.createServerSocket(tlsconfig.getPort(), auth);
        } else {
            serverSocket = new ServerSocket(tlsconfig.getPort());
        }
        thread = new ServerThread(serverSocket);
        thread.start();
        log.info("server started on port " + tlsconfig.getPort());

    }

    public void stop() throws IOException {
        stopped = true;
        thread.interrupt();
    }

    public void addSyslogListener(SyslogListener listener) {
        listeners.add(listener);
    }

    public void removeSyslogListener(SyslogListener listener) {
        listeners.remove(listener);
    }


    protected void notifyListeners(final SyslogMessage msg) {
        exec.execute(new Runnable() {
            public void run() {
                for (SyslogListener listener : listeners) {
                    log.info("notifying listener...");
                    listener.messageArrived(msg);
                }
            }
        });

    }

    protected void notifyException(final SyslogException ex) {
        exec.execute(new Runnable() {
            public void run() {
                for (SyslogListener listener : listeners) {
                    log.info("notifying exception...");
                    listener.exceptionThrown(ex);
                }
            }
        });

    }

    private class ServerThread extends Thread {

        private ServerSocket server;

        public ServerThread(ServerSocket server) {
            this.server = server;
        }

        public void run() {
            while (!stopped) {
                try {
                    Socket s = server.accept();
                    exec.execute(new WorkerThread(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
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
                        int length = Integer.parseInt(new String(b, 0, count));
                        byte[] bytes = new byte[length];
                        int len = in.read(bytes);
                        SyslogMessage msg = null;
                        try {
                            msg = createMessage(bytes);
                        } catch (SyslogException e) {
                            notifyException(new SyslogException(e, bytes));
                        }
                        if (msg != null) {
                            notifyListeners(msg);
                        }
                    }
                }

            } catch (IOException e) {
                notifyException(new SyslogException(e));
            }

        }

        private SyslogMessage createMessage(byte[] bytes) throws SyslogException, IOException {


            // doesn't even check to see if the full length has been read!
            return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(bytes));

        }
    }
}
