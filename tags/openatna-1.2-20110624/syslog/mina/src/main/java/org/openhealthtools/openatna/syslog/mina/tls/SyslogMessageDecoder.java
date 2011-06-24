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

package org.openhealthtools.openatna.syslog.mina.tls;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 1:59:21 PM
 * @date $Date:$ modified by $Author:$
 */

public class SyslogMessageDecoder implements MessageDecoder {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.mina.tls.SyslogMessageDecoder");

    private ByteBuffer msg = ByteBuffer.wrap(new byte[0]);
    private int headerLength = 0;
    private String error = null;

    public MessageDecoderResult decodable(IoSession ioSession, ByteBuffer byteBuffer) {
        log.fine("Enter");
        return readHeader(byteBuffer);
    }

    public MessageDecoderResult decode(IoSession ioSession, ByteBuffer byteBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        log.fine("Enter");
        if (error != null) {
            SyslogException e = new SyslogException("Error reading message length.");
            e.setSourceIp(((InetSocketAddress) ioSession.getRemoteAddress()).getAddress().getHostAddress());
            e.setBytes(error.getBytes("UTF-8"));
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
                e.setSourceIp(((InetSocketAddress) ioSession.getRemoteAddress()).getAddress().getHostAddress());
                e.setBytes(msg.array());
                protocolDecoderOutput.write(e);
                ioSession.close();
                return MessageDecoderResult.OK;
            }
        }
    }


    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        log.info("Enter");

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
            log.fine("got character=|" + c + "|");
            if (c == ' ') {
                log.fine("got a space character.");
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

    private SyslogMessage createMessage(ByteBuffer buff) throws SyslogException {
        return SyslogMessageFactory.getFactory().read(new ByteArrayInputStream(buff.array()));
    }
}
