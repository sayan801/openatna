/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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

package org.openhealthtools.openatna.syslog.core.test.tls;

import java.util.Map;

import org.openhealthtools.openatna.syslog.core.test.tls.ssl.AuthSSLSocketFactory;
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

    public AuthSSLSocketFactory getSocketFactory() {
        return (AuthSSLSocketFactory) getProperty("socket-factory");
    }

    public void setSocketFactory(AuthSSLSocketFactory factory) {
        setProperty("socket-factory", factory);
    }

    public int getPort() {
        Integer port = (Integer) getProperty("port");
        if (port == null) {
            port = 8443;
        }
        return port;
    }

    public void setRequireClientAuth(boolean auth) {
        if (auth) {
            setProperty("client-auth", Boolean.TRUE);
        } else {
            setProperty("client-auth", Boolean.FALSE);
        }
    }

    public boolean isRequireClientAuth() {
        return getProperty("client-auth") != null &&
                getProperty("client-auth") == Boolean.TRUE;
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