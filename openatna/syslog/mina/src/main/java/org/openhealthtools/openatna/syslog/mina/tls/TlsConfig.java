package org.openhealthtools.openatna.syslog.mina.tls;

import org.openhealthtools.openatna.syslog.transport.TransportConfig;

import javax.net.ssl.SSLContext;
import java.util.Map;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 */

public class TlsConfig extends TransportConfig {

    public static final String TLS = "TLSv1.2";

    public TlsConfig() {
        super(TLS);
    }

    public TlsConfig(Map<String, Object> properties) {
        super(TLS, properties);
    }

    public SSLContext getSSLContext() {
        return (SSLContext) getProperty("ssl-context");
    }

    public void setSSLContext(SSLContext context) {
        setProperty("ssl-context", context);
    }

    public int getPort() {
        Integer port = (Integer) getProperty("port");
        if (port == null) {
            port = 8443;
        }
        return port;
    }

    public void setPort(int port) {
        setProperty("port", port);
    }

    public String getHost() {
        return (String) getProperty("host");
    }

    public void setHost(String host) {
        setProperty("host", host);
    }
}
