package org.openhealthtools.openatna.syslog.core.test.tls;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.core.test.tls.ssl.AuthSSLSocketFactory;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * very simple BIO test
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class TlsServer {

    private final Logger logger = LoggerFactory.getLogger(TlsServer.class);
    private final Executor exec = Executors.newFixedThreadPool(10);
    private final Set<SyslogListener> listeners = new HashSet<>();
    private TlsConfig tlsconfig;
    private boolean stopped = false;
    private ServerThread thread;

    public void configure(TlsConfig config) {
        this.tlsconfig = config;
    }

    public void start() throws IOException {
        String host = tlsconfig.getHost();
        if (host == null) {
            host = InetAddress.getLocalHost().getHostAddress();
        }

        AuthSSLSocketFactory f = tlsconfig.getSocketFactory();
        ServerSocket serverSocket;
        if (f != null) {
            boolean auth = tlsconfig.isRequireClientAuth();
            logger.info("Using TLS communication protocol");
            serverSocket = f.createServerSocket(tlsconfig.getPort(), auth);
        } else {
            serverSocket = new ServerSocket(tlsconfig.getPort());
        }
        thread = new ServerThread(serverSocket);
        thread.start();
        logger.info("Server started on port " + tlsconfig.getPort());
    }

    public void stop() {
        stopped = true;
        thread.interrupt();
    }

    public void addSyslogListener(SyslogListener listener) {
        listeners.add(listener);
    }

    public void removeSyslogListener(SyslogListener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners(final SyslogMessage msg) {

        exec.execute(() -> {
            for (SyslogListener listener : listeners) {
                logger.info("notifying listener...");
                listener.messageArrived(msg);
            }
        });
    }

    protected void notifyException(final SyslogException ex) {

        exec.execute(() -> {
            for (SyslogListener listener : listeners) {
                logger.info("Notifying exception...");
                listener.exceptionThrown(ex);
            }
        });

    }

    private class ServerThread extends Thread {

        private final ServerSocket server;

        public ServerThread(ServerSocket server) {
            this.server = server;
        }

        public void run() {

            while (!stopped) {
                try {
                    Socket s = server.accept();
                    exec.execute(new WorkerThread(s));
                } catch (IOException e) {
                    logger.error("{}: '{}'", e.getClass(), e.getMessage(), e);
                }
            }
        }
    }

    private class WorkerThread extends Thread {

        private final Socket socket;

        private WorkerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream in = socket.getInputStream();
                while (true) {
                    byte[] b = new byte[128];
                    int count = 0;
                    while (count < b.length) {
                        int c = in.read();
                        if (c == -1) {
                            return;
                        }
                        if ((c & 0xff) == ' ') {
                            break;
                        }
                        b[count++] = (byte) c;
                    }
                    if (count > 0) {
                        int length = Integer.parseInt(new String(b, 0, count));
                        byte[] bytes = new byte[length];
                        int len = in.read(bytes);
                        SyslogMessage msg = null;
                        try {
                            msg = createMessage(bytes);
                        } catch (SyslogException e) {
                            notifyException(new SyslogException(e, bytes));
                        }
                        if (msg != null) {
                            notifyListeners(msg);
                        }
                    }
                }

            } catch (IOException e) {
                notifyException(new SyslogException(e));
            }
        }

        private SyslogMessage createMessage(byte[] bytes) throws SyslogException {

            // doesn't even check to see if the full length has been read!
            return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(bytes));
        }
    }
}
