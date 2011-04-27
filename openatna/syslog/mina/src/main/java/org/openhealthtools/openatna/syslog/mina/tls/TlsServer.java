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

package org.openhealthtools.openatna.syslog.mina.tls;

import org.apache.mina.common.*;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.mina.Notifier;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
 * @created Aug 18, 2009: 1:05:27 PM
 * @date $Date:$ modified by $Author:$
 */

public class TlsServer implements Notifier {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.mina.tls.TlsServer");


    private TlsConfig tlsconfig;
    private IoAcceptor acceptor;
    private Executor exec = Executors.newFixedThreadPool(5);

    private Set<SyslogListener> listeners = new HashSet<SyslogListener>();

    public void configure(TlsConfig config) {
        this.tlsconfig = config;
    }

    public void start() throws IOException {
        String host = tlsconfig.getHost();
        if (host == null) {
            host = InetAddress.getLocalHost().getHostAddress();
        }

        ByteBuffer.setUseDirectBuffers(false);
        acceptor = new SocketAcceptor(Runtime.getRuntime().availableProcessors() + 1, Executors.newCachedThreadPool());
        acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);

        IoAcceptorConfig config = new SocketAcceptorConfig();

        DefaultIoFilterChainBuilder chain = config.getFilterChain();

        SSLContext ctx = tlsconfig.getSSLContext();
        if (ctx != null) {
            log.info("USING TLS...");
            SSLFilter sslFilter = new SSLFilter(ctx);
            chain.addLast("sslFilter", sslFilter);
        }
        chain.addLast("codec", new ProtocolCodecFilter(new SyslogProtocolCodecFactory()));
        acceptor.setFilterChainBuilder(chain);
        acceptor.bind(new InetSocketAddress(host, tlsconfig.getPort()), new SyslogProtocolHandler(this));
        Set<SocketAddress> addr = acceptor.getManagedServiceAddresses();
        for (SocketAddress sa : addr) {
            log.info("TlsServer.start " + sa.toString());
        }
        log.info("server started on port " + tlsconfig.getPort());

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
                    log.info("notifying listener...");
                    listener.messageArrived(msg);
                }
            }
        });

    }

    public void notifyException(final SyslogException ex) {
        exec.execute(new Runnable() {
            public void run() {
                for (SyslogListener listener : listeners) {
                    log.info("notifying listener...");
                    listener.exceptionThrown(ex);
                }
            }
        });

    }
}
