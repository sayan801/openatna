package org.openhealthtools.openatna.net;

import javax.net.ssl.SSLSocket;

/**
 * An encrypted tcp connection.
 * <p/>
 * This should not be created directly but rather, requested from the ConnectionFactory.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory
 */
public class SecureConnection extends GenericConnection {

    /**
     * Used by the factory to create a connection.
     */
    public SecureConnection(IConnectionDescription connectionDescription) {
        super(connectionDescription);
    }

    /**
     * Checks to make sure the description matches requirements for SecureConnection.
     */
    @Override
    public boolean isConnectionValid() {

        boolean isValid = false;
        if ((description != null) && description.isSecure() && (description instanceof SecureConnectionDescription)) {
            isValid = super.isConnectionValid();
        }
        return isValid;
    }

    /**
     * Used by factory to start the connection.
     */
    public void connect() {

        SSLSocket secureSocket = null;
        if (description != null && description instanceof SecureConnectionDescription) {
            SecureConnectionDescription scd = (SecureConnectionDescription) description;
            // Secure socket factory handles bidirectional certs.
            SecureSocketFactory sslFactory = new SecureSocketFactory(scd);
            secureSocket = (SSLSocket) sslFactory.createSocket(description.getHostname(), description.getPort());
            // TODO Add ATNA logging.
        }
        socket = secureSocket;
    }
}
