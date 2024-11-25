/**
 * Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * Misys Open Source Solutions - initial API and implementation
 * -
 */

package org.openhealthtools.openatna.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * A standard non encrypted tcp connection.
 * <p/>
 * This should not be created directly but rather, requested from the ConnectionFactory.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory
 */
public class StandardConnection extends GenericConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericConnection.class);

    /**
     * Used by the factory to create a connection.
     */
    public StandardConnection(IConnectionDescription connectionDescription) {
        super(connectionDescription);
    }

    /**
     * Used by factory to start the connection.
     */
    public void connect() {
        try {
            socket = new Socket(description.getHostname(), description.getPort());
            // TODO add ATNA logging message, perhaps in finally?
        } catch (IOException e) {
            LOGGER.error("Failed to create a socket on hostname:" + description.getHostname()
                    + " port:" + description.getPort(), e);
            socket = null;
        }
    }
}
