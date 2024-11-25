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

        } catch (IOException | SyslogException | AtnaException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
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

        } catch (IOException | SyslogException | AtnaException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
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

        } catch (IOException | SyslogException e) {
            LOGGER.error("{}: '{}'", e.getClass(), e.getMessage(), e);
        }
    }
}
