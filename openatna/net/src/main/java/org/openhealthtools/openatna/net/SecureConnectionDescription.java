package org.openhealthtools.openatna.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * A description of a secure (SSL/TLS) connection. <p />
 * <p/>
 * This description contains all of the additional information required by secure connections.
 * In general this should be obtained by a call to the getConnectionDescription function
 * in the ConnectionFactory, and not created directly.
 *
 * @author Josh Flachsbart
 */
public class SecureConnectionDescription extends StandardConnectionDescription {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecureConnectionDescription.class);
    private String trustStoreLocation = null;
    private String trustStorePassword = null;
    private String keyStoreLocation = null;
    private String keyStorePassword = null;

    /**
     * Constructor for bidirectional authentication connections.
     * <p/>
     * Should not be used except for by the ConnectionFactory.getConnectionDescription function.
     */
    public SecureConnectionDescription() {
        LOGGER.debug("Generating bidirectional secure connection description.");
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IConnectionDescription#isSecure()
     */
    public boolean isSecure() {
        return true;
    }

    /**
     * Should only be used by the factory.
     * <p/>
     * Currently bidirectional authentication is true.
     */
    public boolean complete() {
        boolean complete = super.complete();
        //If there is an invoked level, the description is not finished processing.
        if (invokedLevel >= 1) {
            return false;
        }
        //  one directional or no authentication may work.
        if (((keyStorePassword != null && keyStoreLocation != null)) &&
                ((trustStorePassword != null && trustStoreLocation != null))) {
            this.complete = complete;
        } else {
            LOGGER.warn("Attempt to complete invalid secure connection description.");
            this.complete = false;
        }

        return this.complete;
    }

    /**
     * Use to get the URL of the keystore described in this connection description. <p />
     * <p/>
     * Most likely folder/keystore or something like that.  Should be local.
     *
     * @return The location of this keystore as a URL class.
     */
    public URL getKeyStore() {
        URL keyStore = null;
        if (keyStoreLocation != null) {
            try {
                keyStore = new URL("file:" + keyStoreLocation);
            } catch (Exception e) {
                LOGGER.error("Keystore has a malformed name: " + keyStoreLocation, e);
            }
        }
        return keyStore;
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setKeyStore(String keyStoreLocation) {
        if (!complete) {
            this.keyStoreLocation = keyStoreLocation;
        } else {
            LOGGER.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    public String getKeyStoreString() {
        return keyStoreLocation;
    }

    /**
     * Use to get the URL of the truststore described in this connection description. <p />
     * <p/>
     * Most likely folder/truststore or something like that.  Should be local.
     *
     * @return The location of this truststore as a URL class.
     */
    public URL getTrustStore() {
        URL trustStore = null;
        if (trustStoreLocation != null) {
            try {
                trustStore = new URL("file:" + trustStoreLocation);
            } catch (Exception e) {
                LOGGER.error("Truststore has a malformed name: " + trustStoreLocation, e);
            }
        }
        return trustStore;
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setTrustStore(String trustStoreLocation) {
        if (!complete) {
            this.trustStoreLocation = trustStoreLocation;
        } else {
            LOGGER.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    public String getTrustStoreString() {
        return trustStoreLocation;
    }

    /**
     * Returns the password to use for this keystore. <p />
     * <p/>
     * Note that for now it is assumed that the key has the
     * same password.
     *
     * @return The password for the key and the keystore.
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setKeyStorePassword(String keyStorePassword) {
        if (!complete) {
            this.keyStorePassword = keyStorePassword;
        } else {
            LOGGER.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Returns the password to use for this truststore. <p />
     *
     * @return The password for the truststore.
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setTrustStorePassword(String trustStorePassword) {
        if (!complete) {
            this.trustStorePassword = trustStorePassword;
        } else {
            LOGGER.warn("Connection Descriptor setter used outside of factory.");
        }
    }
}
