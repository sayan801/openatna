package org.openhealthtools.openatna.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * An abstract implementation of IConnection which does a number of items required by all connections. <p/>
 * <p/>
 * To make a new type of connection which requires these features simply extend this class and implement the additionally
 * required features.
 * <p>
 * Remember that the connect call is where the socket (or other connection type) should be made.
 *
 * @author Josh Flachsbart
 */
public abstract class GenericConnection implements IConnection {

    /**
     * Package level logger for debugging only.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericConnection.class);
    /**
     * The actual connection.
     */
    protected Socket socket = null;
    /**
     * The description of the connection. This includes everything needed to connect.
     */
    protected IConnectionDescription description = null;


    public GenericConnection(IConnectionDescription connectionDescription) {
        description = connectionDescription;
    }

    /**
     * @see org.openhealthtools.openatna.net.IConnection#getConnectionDescription()
     */
    public IConnectionDescription getConnectionDescription() {
        return description;
    }

    /**
     * @see org.openhealthtools.openatna.net.IConnection#isConnectionValid()
     */
    public boolean isConnectionValid() {

        boolean isValid = false;
        if (socket != null) {
            isValid = socket.isConnected();
        }
        return isValid;
    }

    /**
     * @see org.openhealthtools.openatna.net.IConnection#getOutputStream()
     */
    public OutputStream getOutputStream() {

        OutputStream returnVal = null;
        try {
            if (isConnectionValid()) {
                returnVal = socket.getOutputStream();
            }
        } catch (IOException e) {
            return null;
        }
        return returnVal;
    }

    /**
     * @see org.openhealthtools.openatna.net.IConnection#getInputStream()
     */
    public InputStream getInputStream() {

        InputStream returnVal = null;
        try {
            if (isConnectionValid()) {
                returnVal = socket.getInputStream();
            }
        } catch (IOException e) {
            return null;
        }
        return returnVal;
    }

    /**
     * @see org.openhealthtools.openatna.net.IConnection#getSocket()
     */
    public Socket getSocket() {

        Socket returnVal = null;
        if (isConnectionValid()) {
            returnVal = socket;
        }
        return returnVal;
    }

    /**
     * @see org.openhealthtools.openatna.net.IConnection#closeConnection()
     */
    public void closeConnection() {

        if (isConnectionValid()) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("IOException: '{}'", e.getMessage());
            }
        }
        socket = null;
    }

    /**
     * This function must be instantiated by the subclasses because it generates all the actual sockets when
     * the connection is made.
     */
    public abstract void connect();
}
