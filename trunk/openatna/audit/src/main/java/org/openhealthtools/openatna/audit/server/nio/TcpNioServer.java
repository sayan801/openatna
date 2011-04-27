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

package org.openhealthtools.openatna.audit.server.nio;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.*;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.openhealthtools.openatna.audit.server.AtnaServer;
import org.openhealthtools.openatna.audit.server.Server;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.net.SecureConnectionDescription;
import org.openhealthtools.openatna.net.SecureSocketFactory;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.mina.Notifier;
import org.openhealthtools.openatna.syslog.mina.tls.SyslogProtocolCodecFactory;
import org.openhealthtools.openatna.syslog.mina.tls.SyslogProtocolHandler;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 28, 2010
 */
public class TcpNioServer implements Notifier, Server {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.nio.TcpNioServer");


    private AtnaServer atnaServer;
    private IConnectionDescription tlsConnection;
    private IoAcceptor acceptor;


    public TcpNioServer(AtnaServer atnaServer, IConnectionDescription tlsConnection) {
        this.atnaServer = atnaServer;
        this.tlsConnection = tlsConnection;
    }

    public void start() {
        try {
            String host = tlsConnection.getHostname();
            ByteBuffer.setUseDirectBuffers(false);
            acceptor = new SocketAcceptor(Runtime.getRuntime().availableProcessors() + 1, Executors.newCachedThreadPool());
            acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);
            IoAcceptorConfig config = new SocketAcceptorConfig();
            DefaultIoFilterChainBuilder chain = config.getFilterChain();
            if (tlsConnection instanceof SecureConnectionDescription) {
                SecureSocketFactory factory = new SecureSocketFactory((SecureConnectionDescription) tlsConnection);
                SSLContext ctx = factory.getSSLContext();
                if (ctx != null) {
                    SSLFilter sslFilter = new SSLFilter(ctx);
                    sslFilter.setEnabledProtocols(factory.getAtnaProtocols());
                    sslFilter.setEnabledCipherSuites(factory.getAtnaCipherSuites());
                    chain.addLast("sslFilter", sslFilter);
                }
            }
            chain.addLast("codec", new ProtocolCodecFilter(new SyslogProtocolCodecFactory()));
            acceptor.setFilterChainBuilder(chain);
            acceptor.bind(new InetSocketAddress(host, tlsConnection.getPort()), new SyslogProtocolHandler(this));
            log.info("TLS Server running on port:" + tlsConnection.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        log.info("TLS Server shutting down...");
        if (acceptor != null) {
            acceptor.unbindAll();
        }
    }


    public void notifyMessage(SyslogMessage msg) {
        atnaServer.notifyListeners(msg);
    }

    public void notifyException(SyslogException e) {
        atnaServer.notifyException(e);
    }
}
