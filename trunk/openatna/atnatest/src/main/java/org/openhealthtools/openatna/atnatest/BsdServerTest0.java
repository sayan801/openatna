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

package org.openhealthtools.openatna.atnatest;

import java.io.IOException;
import java.net.URL;

import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.audit.process.AtnaMessageListener;
import org.openhealthtools.openatna.audit.process.ProcessorChain;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;
import org.openhealthtools.openatna.syslog.mina.udp.UdpConfig;
import org.openhealthtools.openatna.syslog.mina.udp.UdpServer;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 9:08:56 PM
 * @date $Date:$ modified by $Author:$
 */

public class BsdServerTest0 {

    public static void main(String[] args) {

        URL url = BsdServerTest0.class.getResource("/atnacodes.xml");
        CodeParser.parse(url);
        UdpServer server = new UdpServer();
        SyslogMessageFactory.registerLogMessage("ATNALOG", JaxbLogMessage.class);
        server.addSyslogListener(new AtnaMessageListener(new ProcessorChain()));
        UdpConfig conf = new UdpConfig();
        conf.setPort(1513);
        conf.setHost("localhost");
        server.configure(conf);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
