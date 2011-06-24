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

package org.openhealthtools.openatna.syslog.test.tls;

import java.io.IOException;
import java.net.URL;

import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.mina.tls.TlsConfig;
import org.openhealthtools.openatna.syslog.mina.tls.TlsServer;
import org.openhealthtools.openatna.syslog.test.tls.ssl.AuthSSLSocketFactory;
import org.openhealthtools.openatna.syslog.test.tls.ssl.KeystoreDetails;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 3:02:18 PM
 * @date $Date:$ modified by $Author:$
 */

public class TlsTestServer {

    public static void main(String[] args) {

        URL u = Thread.currentThread().getContextClassLoader().getResource("keys/serverKeyStore");
        KeystoreDetails key = new KeystoreDetails(u.toString(), "serverStorePass", "myServerCert", "password");
        URL uu = Thread.currentThread().getContextClassLoader().getResource("keys/clientKeyStore");
        KeystoreDetails trust = new KeystoreDetails(uu.toString(), "clientStorePass", "myClientCert");
        try {
            AuthSSLSocketFactory f = new AuthSSLSocketFactory(key, trust);
            TlsConfig c = new TlsConfig();
            c.setSSLContext(f.getSSLContext());
            c.setHost("localhost");
            TlsServer server = new TlsServer();
            server.configure(c);
            server.addSyslogListener(new Listener());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    static class Listener implements SyslogListener {

        public void messageArrived(SyslogMessage message) {
            System.out.println("serialized message:");
            System.out.println(message.toString());
            System.out.println("application message:");
            System.out.println(message.getMessage().getMessageObject());
        }

        public void exceptionThrown(SyslogException exception) {
            exception.printStackTrace();
        }
    }
}
