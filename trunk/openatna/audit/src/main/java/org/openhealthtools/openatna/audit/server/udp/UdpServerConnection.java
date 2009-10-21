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

package org.openhealthtools.openatna.audit.server.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.PropertySet;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 11:17:42 PM
 * @date $Date:$ modified by $Author:$
 */

public class UdpServerConnection {

    private IConnectionDescription description;
    private DatagramSocket socket;
    private int maxTransmissionUnit = 32786;

    public UdpServerConnection(IConnectionDescription description) throws IOException {
        this.description = description;
        init();
    }

    private void init() throws IOException {
        int port = description.getPort();
        String addr = description.getHostname();
        socket = new DatagramSocket(port, InetAddress.getByName(addr));
        PropertySet udpProps = description.getPropertySet("UdpProperties");
        if (udpProps == null) {
            return;
        }
        String mtu = udpProps.getValue("mtu");
        if (mtu != null) {
            try {
                this.maxTransmissionUnit = Integer.parseInt(mtu);
            } catch (NumberFormatException e) {
                // log I guess
            }
        }
    }

    public int getMaxTransmissionUnit() {
        return maxTransmissionUnit;
    }

    public IConnectionDescription getConnectionDescription() {
        return description;
    }

    public void closeServerConnection() {
        if (isServerConnectionValid()) {
            socket.close();
        }
    }

    public boolean isServerConnectionValid() {
        return socket != null && socket.isBound();
    }

    public DatagramSocket getSocket() {
        return socket;
    }


}
