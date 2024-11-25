package org.openhealthtools.openatna.syslog.mina.tls;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.mina.SyslogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 */
public class SyslogMessageDecoder implements MessageDecoder {

    private static final String ACTION_ENTER = "Enter";
    private final Logger logger = LoggerFactory.getLogger("org.openhealthtools.openatna.syslog.mina.tls.SyslogMessageDecoder");
    private ByteBuffer msg = ByteBuffer.wrap(new byte[0]);
    private int headerLength = 0;
    private String error = null;

    public MessageDecoderResult decodable(IoSession ioSession, ByteBuffer byteBuffer) {
        logger.debug(ACTION_ENTER);
        return readHeader(byteBuffer);
    }

    public MessageDecoderResult decode(IoSession ioSession, ByteBuffer byteBuffer, ProtocolDecoderOutput protocolDecoderOutput) {
        logger.debug(ACTION_ENTER);
        if (error != null) {
            SyslogException e = new SyslogException("Error reading message length.");
            e.setSourceIp(SyslogUtil.getHostname(ioSession));
            e.setBytes(error.getBytes(StandardCharsets.UTF_8));
            protocolDecoderOutput.write(e);
            ioSession.close();
            return MessageDecoderResult.OK;
        }
        if (headerLength > 0) {
            byteBuffer.position(byteBuffer.position() + headerLength);
            headerLength = 0;
        }
        int n = Math.min(msg.remaining(), byteBuffer.remaining());
        if (n > 0) {
            msg.put(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), n);
            byteBuffer.position(byteBuffer.position() + n);
        }
        if (msg.remaining() > 0) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            try {
                SyslogMessage sm = createMessage(msg);
                msg.clear();
                protocolDecoderOutput.write(sm);
                return MessageDecoderResult.OK;
            } catch (SyslogException e) {
                e.setSourceIp(SyslogUtil.getHostname(ioSession));
                e.setBytes(msg.array());
                protocolDecoderOutput.write(e);
                ioSession.close();
                return MessageDecoderResult.OK;
            }
        }
    }


    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) {
        logger.debug(ACTION_ENTER);
    }

    private MessageDecoderResult readHeader(ByteBuffer byteBuffer) {
        boolean readSpace = false;
        StringBuilder total = new StringBuilder();
        int count = 0;
        while ((byteBuffer.position() + count) < byteBuffer.limit()) {
            if (count > 10) {
                error = total.toString();
                return MessageDecoderResult.OK;
            }
            byte b = byteBuffer.get(byteBuffer.position() + count);
            count++;
            char c = (char) (b & 0xff);
            logger.debug("got character=|{}|", c);
            if (c == ' ') {
                logger.debug("got a space character.");
                readSpace = true;
                try {
                    int length = Integer.parseInt(total.toString());
                    msg = ByteBuffer.wrap(new byte[length]);
                    headerLength = count;
                    break;
                } catch (NumberFormatException e) {
                    error = total.toString();
                    return MessageDecoderResult.OK;
                }
            } else {
                if (Character.isDigit(c)) {
                    total.append(c);
                } else {
                    error = total.toString();
                    return MessageDecoderResult.OK;
                }
            }
        }
        if (!readSpace) {
            return MessageDecoderResult.NEED_DATA;
        }
        return MessageDecoderResult.OK;

    }

    /**
     * @param buff
     * @return
     * @throws SyslogException
     */
    private SyslogMessage createMessage(ByteBuffer buff) throws SyslogException {
        return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(buff.array()));
    }
}
