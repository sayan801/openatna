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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class UdpServer implements Notifier {

    private final Logger logger = LoggerFactory.getLogger(UdpServer.class);

    private final ExecutorService exec = Executors.newFixedThreadPool(10);
    private final Set<SyslogListener> listeners = new HashSet<>();
    private UdpConfig udpconfig;
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
            logger.info("Server has been started on port '{}'", udpconfig.getPort());
        } catch (IOException e) {
            logger.error("IOException: '{}'", e.getMessage(), e);
        }
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
                listener.messageArrived(msg);
            }
        });
    }

    public void notifyException(final SyslogException ex) {
        exec.execute(() -> {
            for (SyslogListener listener : listeners) {
                listener.exceptionThrown(ex);
            }
        });
    }
}
