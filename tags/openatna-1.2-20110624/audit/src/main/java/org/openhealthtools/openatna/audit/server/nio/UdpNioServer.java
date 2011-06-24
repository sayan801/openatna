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
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
import org.openhealthtools.openatna.audit.server.AtnaServer;
import org.openhealthtools.openatna.audit.server.Server;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.mina.Notifier;
import org.openhealthtools.openatna.syslog.mina.udp.UdpProtocolHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 28, 2010
 */
public class UdpNioServer implements Notifier, Server {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.nio.UdpNioServer");


    private AtnaServer atnaServer;
    private IConnectionDescription udpConnection;
    private IoAcceptor acceptor;


    public UdpNioServer(AtnaServer atnaServer, IConnectionDescription udpConnection) {
        this.atnaServer = atnaServer;
        this.udpConnection = udpConnection;
    }

    public void start() {
        try {
            String host = udpConnection.getHostname();

            ByteBuffer.setUseDirectBuffers(false);
            acceptor = new DatagramAcceptor();
            acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);
            IoAcceptorConfig config = new DatagramAcceptorConfig();
            DefaultIoFilterChainBuilder chain = config.getFilterChain();

            acceptor.setFilterChainBuilder(chain);
            acceptor.bind(new InetSocketAddress(host, udpConnection.getPort()), new UdpProtocolHandler(this, 32786));
            log.info("UDP server started on port " + udpConnection.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        log.info("UDP Server shutting down...");
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
