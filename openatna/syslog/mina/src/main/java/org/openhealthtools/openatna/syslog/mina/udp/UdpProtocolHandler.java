package org.openhealthtools.openatna.syslog.mina.udp;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.mina.Notifier;
import org.openhealthtools.openatna.syslog.mina.SyslogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 */
public class UdpProtocolHandler extends IoHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(UdpProtocolHandler.class);

    private Notifier server;
    private int mtu;

    public UdpProtocolHandler(Notifier server, int mtu) {
        this.server = server;
        this.mtu = mtu;
    }

    @Override
    public void sessionCreated(IoSession session) {
        logger.info("Enter");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.info("Enter");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.warn("Error: '{}'", cause.getMessage(), cause);
        session.close();
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        logger.info("Enter");

        if (!(message instanceof ByteBuffer)) {
            return;
        }
        ByteBuffer buff = (ByteBuffer) message;
        if (buff.limit() > mtu) {
            logger.info("message is too long: '{}'. It exceeds config MTU of '{}'", buff.limit(), mtu);
            SyslogException e = new SyslogException("Packet exceeds MTU of " + mtu);
            e.setSourceIp(SyslogUtil.getHostname(session));
            buff.rewind();
            byte[] bytes = new byte[buff.limit()];
            buff.get(bytes);
            e.setBytes(bytes);
            session.close();
            server.notifyException(e);
            return;
        }
        try {
            InputStream in = buff.asInputStream();
            SyslogMessageFactory factory = SyslogMessageFactory.getFactory();
            SyslogMessage msg = factory.read(in);
            msg.setSourceIp(SyslogUtil.getHostname(session));
            server.notifyMessage(msg);
        } catch (SyslogException e) {
            e.setSourceIp(SyslogUtil.getHostname(session));
            buff.rewind();
            byte[] bytes = new byte[buff.limit()];
            buff.get(bytes);
            e.setBytes(bytes);
            session.close();
            server.notifyException(e);
        }
    }
}
