package org.openhealthtools.openatna.audit.server.nio;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Andrew Harrison
 */
public class UdpNioServer implements Notifier, Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpNioServer.class);

    private AtnaServer atnaServer;
    private IConnectionDescription udpConnection;
    private IoAcceptor acceptor;


    public UdpNioServer(AtnaServer atnaServer, IConnectionDescription udpConnection) {
        this.atnaServer = atnaServer;
        this.udpConnection = udpConnection;
    }

    public void start() {

        try {
            ByteBuffer.setUseDirectBuffers(false);
            acceptor = new DatagramAcceptor();
            acceptor.getDefaultConfig().setThreadModel(ThreadModel.MANUAL);
            IoAcceptorConfig config = new DatagramAcceptorConfig();
            DefaultIoFilterChainBuilder chain = config.getFilterChain();
            acceptor.setFilterChainBuilder(chain);
            acceptor.bind(new InetSocketAddress(udpConnection.getHostname(), udpConnection.getPort()), new UdpProtocolHandler(this, 32786));
            LOGGER.info("UDP server '{}' running on port '{}'", udpConnection.getHostname(), udpConnection.getPort());

        } catch (IOException e) {
            LOGGER.error("IoException: '{}'", e.getMessage(), e);
        }
    }

    public void stop() {
        LOGGER.info("UDP Server shutting down...");
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
