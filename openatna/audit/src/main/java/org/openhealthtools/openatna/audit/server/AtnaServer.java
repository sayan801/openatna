package org.openhealthtools.openatna.audit.server;

import org.openhealthtools.openatna.audit.server.nio.TcpNioServer;
import org.openhealthtools.openatna.audit.server.nio.UdpNioServer;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andrew Harrison
 */
public class AtnaServer {

    private final Logger logger = LoggerFactory.getLogger(AtnaServer.class);

    private final IConnectionDescription tlsConnection;
    private final IConnectionDescription udpConnection;
    private Server tcpServer = null;
    private Server udpServer = null;
    private final boolean nio;
    private MessageQueue queue = null;

    private final ExecutorService exec;

    public AtnaServer(IConnectionDescription tlsConnection, IConnectionDescription udpConnection, int threads, boolean nio) {
        this.tlsConnection = tlsConnection;
        this.udpConnection = udpConnection;
        this.nio = nio;
        exec = Executors.newFixedThreadPool(threads);
    }

    public AtnaServer(IConnectionDescription tlsConnection, IConnectionDescription udpConnection) {
        this(tlsConnection, udpConnection, 10, false);
    }

    public void start(SyslogListener listener) {
        queue = new MessageQueue(listener);
        queue.start();
        if (tlsConnection != null) {
            if (nio) {
                tcpServer = new TcpNioServer(this, tlsConnection);
            } else {
                tcpServer = new TcpServer(this, tlsConnection);
            }
            tcpServer.start();
        }
        if (udpConnection != null) {
            if (nio) {
                udpServer = new UdpNioServer(this, udpConnection);
            } else {
                udpServer = new UdpServer(this, udpConnection);
            }
            udpServer.start();
        }
    }

    public void stop() {
        logger.info("AtnaServer shutting down...");
        if (tcpServer != null) {
            tcpServer.stop();
        }
        if (udpServer != null) {
            udpServer.stop();
        }
        if (queue != null) {
            queue.stop();
        }

        exec.shutdown();
    }

    public void execute(Runnable r) {
        exec.execute(r);
    }

    public IConnectionDescription getUdpConnection() {
        return this.udpConnection;
    }

    public IConnectionDescription getTlsConnection() {
        return this.tlsConnection;
    }

    public void notifyListeners(final SyslogMessage msg) {
        if (queue != null) {
            queue.put(msg);
        }
    }

    public void notifyException(final SyslogException ex) {
        if (queue != null) {
            queue.put(ex);
        }
    }
}
