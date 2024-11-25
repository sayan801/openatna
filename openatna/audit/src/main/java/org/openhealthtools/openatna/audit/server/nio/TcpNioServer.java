package org.openhealthtools.openatna.audit.server.nio;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TcpNioServer implements Notifier, Server {

    private final Logger logger = LoggerFactory.getLogger(TcpNioServer.class);

    private final AtnaServer atnaServer;
    private final IConnectionDescription tlsConnection;
    private IoAcceptor acceptor;


    public TcpNioServer(AtnaServer atnaServer, IConnectionDescription tlsConnection) {
        this.atnaServer = atnaServer;
        this.tlsConnection = tlsConnection;
    }

    public void start() {

        try {
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
            acceptor.bind(new InetSocketAddress(tlsConnection.getHostname(), tlsConnection.getPort()), new SyslogProtocolHandler(this));
            logger.info("TLS Server '{}' running on port: '{}'", tlsConnection.getHostname(), tlsConnection.getPort());

        } catch (IOException e) {
            logger.error("IoException: '{}'", e.getMessage(), e);
        }
    }

    public void stop() {
        logger.info("TLS Server shutting down...");
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
