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

package org.openhealthtools.openatna.syslog.mina.udp;

import java.util.Map;

import org.openhealthtools.openatna.syslog.transport.TransportConfig;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 2:34:57 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpConfig extends TransportConfig {

    public UdpConfig() {
        super("UDP");
    }

    public UdpConfig(Map<String, Object> properties) {
        super("UDP", properties);
    }


    public int getPort() {
        Integer port = (Integer) getProperty("port");
        if (port == null) {
            port = 2862;
        }
        return port;
    }

    public void setPort(int port) {
        setProperty("port", port);
    }

    public int getMtu() {
        Integer mtu = (Integer) getProperty("mtu");
        if (mtu == null) {
            mtu = 32768;
        }
        return mtu;
    }

    public void setMtu(int mtu) {
        setProperty("mtu", mtu);
    }

    public String getHost() {
        return (String) getProperty("host");
    }

    public void setHost(String host) {
        setProperty("host", host);
    }
}
