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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TlsServer implements Notifier {

    private final Logger logger = LoggerFactory.getLogger(TlsServer.class);
    private final ExecutorService exec = Executors.newFixedThreadPool(10);
    private final Set<SyslogListener> listeners = new HashSet<>();
    private TlsConfig tlsconfig;
    private IoAcceptor acceptor;

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
            logger.info("Communication over TLS enabled...");
            SSLFilter sslFilter = new SSLFilter(ctx);
            chain.addLast("sslFilter", sslFilter);
        }
        chain.addLast("codec", new ProtocolCodecFilter(new SyslogProtocolCodecFactory()));
        acceptor.setFilterChainBuilder(chain);
        acceptor.bind(new InetSocketAddress(host, tlsconfig.getPort()), new SyslogProtocolHandler(this));
        Set<SocketAddress> addr = acceptor.getManagedServiceAddresses();
        if (logger.isInfoEnabled()) {
            for (SocketAddress sa : addr) {
                logger.info("TLS Server started: '{}'", sa.toString());
            }
        }
        logger.info("TLS Server started on port: '{}'", tlsconfig.getPort());

    }

    public void stop() {

        if (acceptor != null) {
            acceptor.unbindAll();
        }
        exec.shutdown();
    }

    public void addSyslogListener(SyslogListener listener) {
        listeners.add(listener);
    }

    public void removeSyslogListener(SyslogListener listener) {
        listeners.remove(listener);
    }

    public void notifyMessage(final SyslogMessage msg) {

        exec.execute(() -> {
            for (SyslogListener listener : listeners) {
                logger.info("Notifying listener...");
                listener.messageArrived(msg);
            }
        });
    }

    public void notifyException(final SyslogException ex) {

        exec.execute(() -> {
            for (SyslogListener listener : listeners) {
                logger.info("Notifying listener about error...");
                listener.exceptionThrown(ex);
            }
        });
    }
}
