/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
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
 *    Misys Open Source Solutions - initial API and implementation
 *
 */
package org.openhealthtools.openatna.net;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * X509TrustManager with log info.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class LoggedX509TrustManager implements X509TrustManager {
	private X509TrustManager defaultTrustManager = null;

	/** Log object for this class. */
	private static final Logger LOG = Logger.getLogger(LoggedX509TrustManager.class);
	SecureConnectionDescription scd;

	/**
	 * Constructor for AuthSSLX509TrustManager.
	 */
	public LoggedX509TrustManager(final X509TrustManager defaultTrustManager, SecureConnectionDescription scd) {
		super();
		LOG.setLevel(Level.ALL);
		if (defaultTrustManager == null) throw new IllegalArgumentException("Trust manager may not be null");
		this.defaultTrustManager = defaultTrustManager;
		this.scd = scd;
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[], String)
	 */
	public void checkClientTrusted(X509Certificate[] certificates, String authType) 
		throws CertificateException 
	{
		if (LOG.isInfoEnabled() && certificates != null) {
			for (int c = 0; c < certificates.length; c++) {
				X509Certificate cert = certificates[c];
				LOG.info("\n Client certificate " + (c + 1) + ":");
				LOG.info("\n  Subject DN: " + cert.getSubjectDN());
				LOG.info("\n  Signature Algorithm: " + cert.getSigAlgName());
				LOG.info("\n  Valid from: " + cert.getNotBefore());
				LOG.info("\n  Valid until: " + cert.getNotAfter());
				LOG.info("\n  Issuer: " + cert.getIssuerDN());
			}
		}
		// This will throw a CertificateException if it is not trusted.
		try {
			this.defaultTrustManager.checkClientTrusted(certificates, authType);
		} catch (CertificateException e) {
			LOG.error("Something wrong with the client certificate (auth type: \" + authType +\")", e);
			throw e;
		}
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[], String)
	 */
	public void checkServerTrusted(X509Certificate[] certificates, String authType) 
		throws CertificateException 
	{
		if (LOG.isInfoEnabled() && certificates != null) {
            String certificateChain = "Server Certificate Chain: \n";
			for (int c = 0; c < certificates.length; c++) {
				X509Certificate cert = certificates[c];
				certificateChain += "\n Server certificate " + (c + 1) + ":"
				    + "\n  Subject DN: " + cert.getSubjectDN()
				    + "\n  Signature Algorithm: " + cert.getSigAlgName()
				    + "\n  Valid from: " + cert.getNotBefore()
				    + "\n  Valid until: " + cert.getNotAfter()
                     + "\n  Issuer: " + cert.getIssuerDN();
			}
            LOG.info(certificateChain);
        }
		// This will throw a CertificateException if it is not trusted.
		try {
			this.defaultTrustManager.checkServerTrusted(certificates, authType);
		} catch (CertificateException e) {
			LOG.error("Something wrong with the server certificate: (auth type: " + authType +")", e);
			throw e;
		}
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return this.defaultTrustManager.getAcceptedIssuers();
	}
}