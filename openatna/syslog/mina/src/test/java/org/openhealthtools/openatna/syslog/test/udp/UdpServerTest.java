/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */

package org.openhealthtools.openatna.syslog.test.udp;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.message.XmlLogMessage;
import org.openhealthtools.openatna.syslog.mina.udp.UdpConfig;
import org.openhealthtools.openatna.syslog.mina.udp.UdpServer;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * runs a UDP 5426 server.
 * This sets the LogMessage for a particular Message id.
 * This means the LogMEssage is expected to be XML.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 2:56:12 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServerTest.class);

    public static void main(String[] args) {

        // set a particular LogMessage for a particular Message Id
        SyslogMessageFactory.registerLogMessage("ATNALOG", XmlLogMessage.class);

        UdpServer server = new UdpServer();
        server.addSyslogListener(new Listener());
        UdpConfig conf = new UdpConfig();
        conf.setPort(1513);
        conf.setHost("localhost");
        server.configure(conf);
        server.start();
    }

    static class Listener implements SyslogListener {

        public void messageArrived(SyslogMessage message) {
            LOGGER.info("serialized message: '{}'", message.toString());
            LOGGER.info("application message: '{}'", message.getMessage().getMessageObject());
        }

        public void exceptionThrown(SyslogException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
        }
    }
}
