/**
 * Copyright (c) 2009-2011 University of Cardiff and others
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
 * University of Cardiff - initial API and implementation
 * -
 */
package org.openhealthtools.openatna.syslog.test.tls.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
    private static Logger log = LoggerFactory.getLogger(AuthSSLX509TrustManager.class);

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
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[], String authType)
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
                log.info(" Client certificate " + (c + 1) + ":");
                log.info("  Subject DN: " + cert.getSubjectDN());
                log.info("  Signature Algorithm: " + cert.getSigAlgName());
                log.info("  Valid from: " + cert.getNotBefore());
                log.info("  Valid until: " + cert.getNotAfter());
                log.info("  Issuer: " + cert.getIssuerDN());
            }
            if (!isAuthDN) {
                throw new CertificateException("Subject DN is not authorized to perform the requested action.");
            }
            trustManager.checkClientTrusted(certificates, authType);
        }

    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[], String authType)
     */
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (certificates != null) {
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                log.info(" Server certificate " + (c + 1) + ":");
                log.info("  Subject DN: " + cert.getSubjectDN());
                log.info("  Signature Algorithm: " + cert.getSigAlgName());
                log.info("  Valid from: " + cert.getNotBefore());
                log.info("  Valid until: " + cert.getNotAfter());
                log.info("  Issuer: " + cert.getIssuerDN());
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
