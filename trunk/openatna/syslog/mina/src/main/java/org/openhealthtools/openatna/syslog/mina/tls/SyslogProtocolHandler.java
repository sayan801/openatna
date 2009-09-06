/*
 * Copyright (c) 2009 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.syslog.mina.tls;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;

import java.util.logging.Logger;


/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 1:35:29 PM
 * @date $Date:$ modified by $Author:$
 */

public class SyslogProtocolHandler extends IoHandlerAdapter {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.mina.tls.SyslogProtocolHandler");


    private TlsServer server;

    public SyslogProtocolHandler(TlsServer server) {
        this.server = server;
    }

    public void sessionCreated(IoSession session) {
        if (session.getTransportType() == TransportType.SOCKET) {
            ((SocketSessionConfig) session.getConfig())
                    .setReceiveBufferSize(2048);
        }

        session.setIdleTime(IdleStatus.BOTH_IDLE, 10);
        // We're going to use SSL negotiation notification.
        session.setAttribute(SSLFilter.USE_NOTIFICATION);
    }

    public void sessionIdle(IoSession session, IdleStatus status) {
    }

    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
        session.close();
    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if (message instanceof SyslogMessage) {
            SyslogMessage sl = (SyslogMessage) message;
            server.notifyListeners(sl);
        } else if (message instanceof SyslogException) {
            SyslogException ex = (SyslogException) message;
            server.notifyException(ex);
        }

    }
}
