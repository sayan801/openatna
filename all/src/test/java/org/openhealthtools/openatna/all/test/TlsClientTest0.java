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

package org.openhealthtools.openatna.all.test;

import org.junit.Test;
import org.openhealthtools.openatna.all.test.ssl.AuthSSLSocketFactory;
import org.openhealthtools.openatna.all.test.ssl.KeystoreDetails;
import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.ProvisionalMessage;
import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessage;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 4:52:05 PM
 * @date $Date:$ modified by $Author:$
 */

public class TlsClientTest0 extends ClientTest {

    @Test
    public void testMessages() {
        try {
            URL u = Thread.currentThread().getContextClassLoader().getResource("testcerts/serverKeyStore");
            KeystoreDetails trust = new KeystoreDetails(u.toString(), "password", "myServerCert");
            URL uu = Thread.currentThread().getContextClassLoader().getResource("testcerts/clientKeyStore");
            KeystoreDetails key = new KeystoreDetails(uu.toString(), "password", "myClientCert", "password");
            AuthSSLSocketFactory f = new AuthSSLSocketFactory(key, trust);
            List<AtnaMessage> messages = getMessages();
            for (int i = 0; i < 1000; i++) {
                Socket s = f.createSecureSocket("localhost", 2862);
                OutputStream out = s.getOutputStream();
                for (AtnaMessage message : messages) {
                    ProtocolMessage sl = new ProtocolMessage(10, 5, "localhost", new JaxbLogMessage(message), "IHE_CLIENT", "ATNALOG", "1234");
                    byte[] bytes = sl.toByteArray();
                    out.write((String.valueOf(bytes.length) + " ").getBytes(Constants.ENC_UTF8));
                    out.write(bytes);
                    out.flush();
                }
                out.close();
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyslogException e) {
            e.printStackTrace();
        } catch (AtnaException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testNoEncryption() {
        try {
            URL u = Thread.currentThread().getContextClassLoader().getResource("testcerts/serverKeyStore");
            KeystoreDetails trust = new KeystoreDetails(u.toString(), "password", "myServerCert");
            URL uu = Thread.currentThread().getContextClassLoader().getResource("testcerts/clientKeyStore");
            KeystoreDetails key = new KeystoreDetails(uu.toString(), "password", "myClientCert", "password");
            AuthSSLSocketFactory f = new AuthSSLSocketFactory(key, trust);
            List<AtnaMessage> messages = getMessages();
            SSLSocket s = (SSLSocket) f.createSecureSocket("localhost", 2862);
            s.setEnabledCipherSuites(new String[]{"SSL_RSA_WITH_NULL_SHA"});
            OutputStream out = s.getOutputStream();
            for (AtnaMessage message : messages) {
                ProtocolMessage sl = new ProtocolMessage(10, 5, "localhost", new JaxbLogMessage(message), "IHE_CLIENT", "ATNALOG", "1234");
                byte[] bytes = sl.toByteArray();
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
        } catch (AtnaException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBadMessage() {
        try {
            URL u = Thread.currentThread().getContextClassLoader().getResource("testcerts/serverKeyStore");
            KeystoreDetails trust = new KeystoreDetails(u.toString(), "password", "myServerCert");
            URL uu = Thread.currentThread().getContextClassLoader().getResource("testcerts/clientKeyStore");
            KeystoreDetails key = new KeystoreDetails(uu.toString(), "password", "myClientCert", "password");
            AuthSSLSocketFactory f = new AuthSSLSocketFactory(key, trust);
            SSLSocket s = (SSLSocket) f.createSecureSocket("localhost", 2862);
            OutputStream out = s.getOutputStream();
            ProvisionalMessage message = new ProvisionalMessage("This is a bad message".getBytes("UTF-8"));
            ProtocolMessage sl = new ProtocolMessage(10, 5, "localhost", new UdpClientTest0.ProvLogMessage(message), "IHE_CLIENT", "ATNALOG", "1234");
            byte[] bytes = sl.toByteArray();
            out.write((String.valueOf(bytes.length) + " ").getBytes(Constants.ENC_UTF8));
            out.write(bytes);
            out.flush();
            out.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyslogException e) {
            e.printStackTrace();
        }
    }


}
