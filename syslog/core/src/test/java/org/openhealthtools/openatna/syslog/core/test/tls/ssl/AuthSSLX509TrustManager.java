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

package org.openhealthtools.openatna.syslog.core.test.tls.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.X509TrustManager;

/**
 * <p>
 * <p/>
 * </p>
 */

public class AuthSSLX509TrustManager implements X509TrustManager {

    private X509TrustManager trustManager = null;
    private X509TrustManager defaultTrustManager = null;
    List<String> authorizedDns = null;
    /**
     * Log object for this class.
     */
    static Logger log = Logger.getLogger("org.openhealthtools.openatna.syslog.test.tls.ssl.AuthSSLX509TrustManager");

    /**
     * Constructor for AuthSSLX509TrustManager.
     */
    public AuthSSLX509TrustManager(final X509TrustManager trustManager, final X509TrustManager defaultTrustManager, List<String> authorizedDns) {
        super();
        if (trustManager == null) {
            throw new IllegalArgumentException("Trust manager may not be null");
        }
        this.trustManager = trustManager;
        this.defaultTrustManager = defaultTrustManager;
        this.authorizedDns = authorizedDns;
        if (this.authorizedDns == null) {
            this.authorizedDns = new ArrayList<String>();
        }
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
     */
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (certificates != null) {
            boolean isAuthDN = false;
            if (authorizedDns.size() == 0) {
                isAuthDN = true;
            }
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                if (isAuthDN == false) {
                    for (String authorizedDn : authorizedDns) {
                        if (authorizedDn.equals(cert.getSubjectDN())) {
                            isAuthDN = true;
                        }
                    }
                }
                log.fine(" Client certificate " + (c + 1) + ":");
                log.fine("  Subject DN: " + cert.getSubjectDN());
                log.fine("  Signature Algorithm: " + cert.getSigAlgName());
                log.fine("  Valid from: " + cert.getNotBefore());
                log.fine("  Valid until: " + cert.getNotAfter());
                log.fine("  Issuer: " + cert.getIssuerDN());
            }
            if (!isAuthDN) {
                throw new CertificateException("Subject DN is not authorized to perform the requested action.");
            }
            trustManager.checkClientTrusted(certificates, authType);
        }

    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
     */
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (certificates != null) {
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                log.fine(" Server certificate " + (c + 1) + ":");
                log.fine("  Subject DN: " + cert.getSubjectDN());
                log.fine("  Signature Algorithm: " + cert.getSigAlgName());
                log.fine("  Valid from: " + cert.getNotBefore());
                log.fine("  Valid until: " + cert.getNotAfter());
                log.fine("  Issuer: " + cert.getIssuerDN());
            }
        }

        try {
            if (defaultTrustManager != null) {
                defaultTrustManager.checkServerTrusted(certificates, authType);
            }
        } catch (CertificateException e) {
            trustManager.checkServerTrusted(certificates, authType);
        }
    }

    /**
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] certs = this.trustManager.getAcceptedIssuers();

        if (defaultTrustManager != null) {
            X509Certificate[] suncerts = this.defaultTrustManager.getAcceptedIssuers();
            X509Certificate[] all = new X509Certificate[certs.length + suncerts.length];
            System.arraycopy(certs, 0, all, 0, certs.length);
            System.arraycopy(suncerts, 0, all, certs.length, suncerts.length);
            certs = all;
        }
        if (certs == null) {
            certs = new X509Certificate[0];
        }

        return certs;
    }
}