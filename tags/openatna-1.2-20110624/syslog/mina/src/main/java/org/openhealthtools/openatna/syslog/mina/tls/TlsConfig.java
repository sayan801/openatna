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

import java.util.Map;

import javax.net.ssl.SSLContext;
import org.openhealthtools.openatna.syslog.transport.TransportConfig;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 1:14:09 PM
 * @date $Date:$ modified by $Author:$
 */

public class TlsConfig extends TransportConfig {

    public static final String TLS = "TLSv1";


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
