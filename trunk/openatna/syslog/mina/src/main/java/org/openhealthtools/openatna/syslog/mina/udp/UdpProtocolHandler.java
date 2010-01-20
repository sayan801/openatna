/*
 * Copyright (c) 2009-2010 University of Cardiff and others.
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

package org.openhealthtools.openatna.syslog.mina.udp;

import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 2:43:15 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpProtocolHandler extends IoHandlerAdapter {

    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.mina.udp.UdpProtocolHandler");


    private UdpServer server;
    private UdpConfig config;

    public UdpProtocolHandler(UdpServer server, UdpConfig config) {
        this.server = server;
        this.config = config;
    }

    public void sessionCreated(IoSession session) {
        log.info("Enter");
    }

    public void sessionIdle(IoSession session, IdleStatus status) {
        log.info("Enter");
    }

    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
        session.close();
    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {
        log.info("Enter");

        if (!(message instanceof ByteBuffer)) {
            return;
        }
        ByteBuffer buff = (ByteBuffer) message;
        if (buff.limit() > config.getMtu()) {
            log.info("message is too long: " + buff.limit() + ". It exceeds config MTU of " + config.getMtu());
            return;
        }
        try {
            InputStream in = buff.asInputStream();
            SyslogMessageFactory factory = SyslogMessageFactory.getFactory();
            SyslogMessage msg = factory.read(in);
            server.notifyListeners(msg);
        } catch (SyslogException e) {
            e.printStackTrace();
        }
    }
}
