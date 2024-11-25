package org.openhealthtools.openatna.syslog.mina.tls;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.mina.Notifier;
import org.openhealthtools.openatna.syslog.mina.SyslogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 */
public class SyslogProtocolHandler extends IoHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(SyslogProtocolHandler.class);

    private final Notifier server;

    public SyslogProtocolHandler(Notifier server) {
        this.server = server;
    }

    @Override
    public void sessionCreated(IoSession session) {

        if (session.getTransportType() == TransportType.SOCKET) {
            ((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(2048);
        }

        session.setIdleTime(IdleStatus.BOTH_IDLE, 10);
        // We're going to use SSL negotiation notification.
        session.setAttribute(SSLFilter.USE_NOTIFICATION);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        // Not implemented by eHDSI OpenNCP
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {

        logger.warn("Error: '{}'", cause.getMessage(), cause);
        session.close();
    }

    @Override
    public void messageReceived(IoSession session, Object message) {

        String sourceIp = SyslogUtil.getHostname(session);
        logger.info("[Syslog Protocol] Message '{}' received from '{}'", message.getClass(), sourceIp);
        if (message instanceof SyslogMessage) {
            SyslogMessage syslogMessage = (SyslogMessage) message;
            syslogMessage.setSourceIp(sourceIp);
            server.notifyMessage(syslogMessage);

        } else if (message instanceof SyslogException) {
            SyslogException syslogException = (SyslogException) message;
            syslogException.setSourceIp(sourceIp);
            server.notifyException(syslogException);
        }
    }
}
