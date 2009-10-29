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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;
import org.openhealthtools.openatna.syslog.transport.SyslogServer;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 11:53:38 AM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaServer implements SyslogServer {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.AtnaServer");

    private IConnectionDescription tlsConnection;
    private IConnectionDescription udpConnection;
    private TcpServer tcpServer = null;
    private UdpServer udpServer = null;

    private volatile boolean destroyed = false;

    private Executor exec;
    private Set<SyslogListener> listeners = new HashSet<SyslogListener>();


    public AtnaServer(IConnectionDescription tlsConnection, IConnectionDescription udpConnection, int threads) {
        this.tlsConnection = tlsConnection;
        this.udpConnection = udpConnection;
        exec = Executors.newFixedThreadPool(threads);
        new ShutdownHook().createHook();
    }

    public AtnaServer(IConnectionDescription tlsConnection, IConnectionDescription udpConnection) {
        this(tlsConnection, udpConnection, 5);
    }

    public void start() throws IOException {
        if (tlsConnection != null) {
            tcpServer = new TcpServer(this, tlsConnection);
            tcpServer.start();
        }
        if (udpConnection != null) {
            udpServer = new UdpServer(this, udpConnection);
            udpServer.start();
        }
    }

    public void stop() throws IOException {
        if (tcpServer != null) {
            tcpServer.stop();
        }
        if (udpServer != null) {
            udpServer.stop();
        }
    }

    public void execute(Runnable r) {
        exec.execute(r);
    }

    public void addSyslogListener(SyslogListener listener) {
        listeners.add(listener);
    }

    public void removeSyslogListener(SyslogListener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners(final SyslogMessage msg) {
        for (SyslogListener listener : listeners) {
            log.debug("notifying listener...");
            listener.messageArrived(msg);
        }
    }

    protected void notifyException(final SyslogException ex) {
        for (SyslogListener listener : listeners) {
            log.debug("notifying listener...");
            listener.exceptionThrown(ex);
        }
    }

    private void kill() {
        try {
            stop();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private class ShutdownHook extends Thread {

        private void add() {
            try {
                Method shutdownHook = java.lang.Runtime.class.getMethod("addShutdownHook", new Class[]{java.lang.Thread.class});
                shutdownHook.invoke(Runtime.getRuntime(), new Object[]{this});
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void createHook() {
            add();
        }

        public void run() {
            log.info("AtnaServer$ShutdownHook.run ENTER");
            if (!destroyed) {
                try {
                    kill();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                destroyed = true;
            }
            log.info("AtnaServer$ShutdownHook.run EXIT");
        }
    }
}
