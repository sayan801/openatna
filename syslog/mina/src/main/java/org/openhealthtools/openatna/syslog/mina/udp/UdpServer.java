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

package org.openhealthtools.openatna.syslog.mina.udp;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.mina.Notifier;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 2:34:33 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpServer implements Notifier {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.mina.udp.UdpServer");

    private Executor exec = Executors.newFixedThreadPool(5);
    private UdpConfig udpconfig;
    private Set<SyslogListener> listeners = new HashSet<SyslogListener>();
    private DatagramAcceptor acceptor;

    public void configure(UdpConfig config) {
        this.udpconfig = config;
    }

    public void start() {
        try {
            String host = udpconfig.getHost();
            if (host == null) {
                host = InetAddress.getLocalHost().getHostAddress();
            }
            ByteBuffer.setUseDirectBuffers(false);
            acceptor = new DatagramAcceptor();
            acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);
            IoAcceptorConfig config = new DatagramAcceptorConfig();
            DefaultIoFilterChainBuilder chain = config.getFilterChain();

            acceptor.setFilterChainBuilder(chain);
            acceptor.bind(new InetSocketAddress(host, udpconfig.getPort()), new UdpProtocolHandler(this, udpconfig.getMtu()));
            log.info("server started on port " + udpconfig.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        if (acceptor != null) {
            acceptor.unbindAll();
        }
    }


    public void addSyslogListener(SyslogListener listener) {
        listeners.add(listener);
    }

    public void removeSyslogListener(SyslogListener listener) {
        listeners.remove(listener);
    }

    public void notifyMessage(final SyslogMessage msg) {
        exec.execute(new Runnable() {
            public void run() {
                for (SyslogListener listener : listeners) {
                    listener.messageArrived(msg);
                }
            }
        });
    }

    public void notifyException(final SyslogException ex) {
        exec.execute(new Runnable() {
            public void run() {
                for (SyslogListener listener : listeners) {
                    listener.exceptionThrown(ex);
                }
            }
        });
    }
}
