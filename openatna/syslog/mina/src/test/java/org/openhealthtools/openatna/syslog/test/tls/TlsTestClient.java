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
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.message.StringLogMessage;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;
import org.openhealthtools.openatna.syslog.protocol.SdParam;
import org.openhealthtools.openatna.syslog.protocol.StructuredElement;
import org.openhealthtools.openatna.syslog.test.tls.ssl.AuthSSLSocketFactory;
import org.openhealthtools.openatna.syslog.test.tls.ssl.KeystoreDetails;

/**
 * Tests TLS 5424 server. This sends multiple log messages concatenated.
 * NOTE: the TLS binding requires the client to send the length of the message, followed by a space,
 * to prepend the message.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 3:27:09 PM
 * @date $Date:$ modified by $Author:$
 */

public class TlsTestClient {

    public static void main(String[] args) {
        try {

            URL u = Thread.currentThread().getContextClassLoader().getResource("keys/serverKeyStore");
            KeystoreDetails trust = new KeystoreDetails(u.toString(), "serverStorePass", "myServerCert");
            URL uu = Thread.currentThread().getContextClassLoader().getResource("keys/clientKeyStore");
            KeystoreDetails key = new KeystoreDetails(uu.toString(), "clientStorePass", "myClientCert", "password");
            AuthSSLSocketFactory f = new AuthSSLSocketFactory(key, trust);

            ProtocolMessage sl =
                    new ProtocolMessage(10, 5, "2009-08-14T14:12:23.115Z", "localhost", new StringLogMessage("<atna></atna>"), "IHE_XDS", "ATNALOG",
                            "1234");
            List<SdParam> params = new ArrayList<SdParam>();
            params.add(new SdParam("param1", "param value\\=1"));
            params.add(new SdParam("param2", "param value] 2"));
            params.add(new SdParam("param3", "param value 3"));
            params.add(new SdParam("param3", "param value 4"));
            StructuredElement se = new StructuredElement("exampleSDID@1234", params);
            sl.addStructuredElement(se);

            Socket s = f.createSecureSocket("localhost", 8443);
            OutputStream out = s.getOutputStream();
            byte[] bytes = sl.toByteArray();
            for (int i = 0; i < 5; i++) {
                // add message length plus space before message
                out.write((String.valueOf(bytes.length) + " ").getBytes(Constants.ENC_UTF8));
                out.write(bytes);
                out.flush();
            }
            out.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyslogException e) {
            e.printStackTrace();
        }
    }
}
