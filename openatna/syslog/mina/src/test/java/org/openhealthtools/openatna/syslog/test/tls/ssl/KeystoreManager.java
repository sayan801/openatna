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

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Nov 20, 2008: 7:58:07 PM
 * @date $Date:$ modified by $Author:$
 * @todo Put your notes here...
 */
public class KeystoreManager {

    static Logger log = LoggerFactory.getLogger("org.wspeer.security.KeystoreManager");

    private static X509TrustManager sunTrustManager = null;

    static {
        loadDefaultTrustManager();
    }

    private KeystoreDetails defaultKeyDetails;
    private HashMap<String, KeystoreDetails> allKeys = new HashMap<>();
    private HashMap<String, KeystoreDetails> allStores = new HashMap<>();
    private File keysDir;
    private File certsDir;
    private String home;

    public KeystoreManager(String home) {
        if (home != null) {
            this.home = home;
            loadKeys(this.home);
        }
    }

    private static void loadDefaultTrustManager() {
        try {
            File certs;
            String definedcerts = System.getProperty("javax.net.ssl.trustStore");
            String pass = System.getProperty("javax.net.ssl.trustStorePassword");
            if (definedcerts != null) {
                certs = new File(definedcerts);
            } else {
                String common = System.getProperty("java.home") +
                        File.separator +
                        "lib" +
                        File.separator +
                        "security" +
                        File.separator;
                String cacerts = common + "cacerts";
                String jssecacerts = common + "jssecacerts";
                certs = new File(jssecacerts);
                if (!certs.exists() || certs.length() == 0) {
                    certs = new File(cacerts);
                }

            }
            if (pass == null) {
                pass = "changeit";
            }
            if (certs != null) {
                KeyStore ks = KeyStore.getInstance("jks");
                ks.load(new FileInputStream(certs), pass.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
                tmf.init(ks);
                TrustManager tms[] = tmf.getTrustManagers();
                for (int i = 0; i < tms.length; i++) {
                    if (tms[i] instanceof X509TrustManager) {
                        log.info(" found default trust manager.");
                        sunTrustManager = (X509TrustManager) tms[i];
                        break;
                    }
                }
            }
        } catch (KeyStoreException e) {
            log.error("Exception thrown trying to create default trust manager:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception thrown trying to create default trust manager:" + e.getMessage());
        } catch (CertificateException e) {
            log.error("Exception thrown trying to create default trust manager:" + e.getMessage());
        } catch (NoSuchProviderException e) {
            log.error("Exception thrown trying to create default trust manager:" + e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("Exception thrown trying to create default trust manager:" + e.getMessage());
        } catch (IOException e) {
            log.error("Exception thrown trying to create default trust manager:" + e.getMessage());
        }
    }

    public static X509TrustManager getDefaultTrustManager() {
        return sunTrustManager;
    }

    private static String trimPort(String host) {
        int colon = host.indexOf(":");
        if (colon > 0 && colon < host.length() - 1) {
            try {
                int port = Integer.parseInt(host.substring(colon + 1, host.length()), host.length());
                host = host.substring(0, colon);
                log.info("KeystoreManager.trimPort up to colon:" + host);
                log.info("KeystoreManager.trimPort port:" + port);

                return host;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    private static String getAnyPort(String auth) {
        int star = auth.indexOf("*");
        if (star == auth.length() - 1) {
            int colon = auth.indexOf(":");
            if (colon == star - 1) {
                auth = auth.substring(0, colon);
                return auth;
            }
        }
        return null;
    }

    private void loadKeys(String home) {
        File sec = new File(home);
        if (!sec.exists()) {
            return;
        }

        keysDir = new File(sec, "keys");
        if (!keysDir.exists()) {
            keysDir.mkdir();
        }
        certsDir = new File(sec, "certs");
        if (!certsDir.exists()) {
            certsDir.mkdir();
        }
        File[] keyfiles = keysDir.listFiles();
        if (keyfiles != null) {
            for (File keyfile : keyfiles) {
                try {
                    KeystoreDetails kd = load(new FileInputStream(keyfile));
                    if (kd.getAuthority() != null && kd.getAuthority().trim().equalsIgnoreCase("default")) {
                        defaultKeyDetails = kd;
                    }
                    allKeys.put(keyfile.getName(), kd);

                } catch (IOException e) {
                    log.info(" exception thrown while loading details from " + keyfile.getAbsolutePath());
                    continue;
                }
            }
        }
        keyfiles = certsDir.listFiles();
        if (keyfiles != null) {
            for (File keyfile : keyfiles) {
                try {
                    KeystoreDetails kd = load(new FileInputStream(keyfile));
                    allStores.put(keyfile.getName(), kd);

                } catch (IOException e) {
                    log.info(" exception thrown while loading details from " + keyfile.getAbsolutePath());
                    continue;
                }
            }
        }
    }

    public void addKeyDetails(String fileName, KeystoreDetails details) throws IOException {
        storeAsKey(details, fileName);
        allKeys.put(fileName, details);
    }

    public void addTrustDetails(String fileName, KeystoreDetails details) throws IOException {
        storeAsCert(details, fileName);
        allStores.put(fileName, details);
    }

    public void deleteKeyDetails(String fileName) {
        allKeys.remove(fileName);
        deleteKey(fileName);
    }

    public void deleteTrustDetails(String fileName) {
        allStores.remove(fileName);
        deleteCert(fileName);
    }

    public KeystoreDetails getKeyDetails(String fileName) {
        return allKeys.get(fileName);
    }

    public KeystoreDetails getTrustStoreDetails(String fileName) {
        return allStores.get(fileName);
    }

    public void setDefaultKeystoreDetails(KeystoreDetails details) {
        defaultKeyDetails = details;
    }

    public KeystoreDetails getDefaultKeyDetails() {
        return defaultKeyDetails;
    }

    public File getKeysDirectory() {
        return keysDir;
    }

    public File getCertsDirectory() {
        return certsDir;
    }

    public KeystoreDetails getKeyFileDetails(String fileName) {
        return allKeys.get(fileName);
    }

    public KeystoreDetails getStoreFileDetails(String fileName) {
        return allStores.get(fileName);
    }

    public String[] getKeyfileNames() {
        return allKeys.keySet().toArray(new String[allKeys.keySet().size()]);
    }

    public String[] getTrustfileNames() {
        return allStores.keySet().toArray(new String[allStores.keySet().size()]);
    }

    public KeystoreDetails getKeyFileForHost(String host) {
        KeystoreDetails def = null;
        for (KeystoreDetails keystoreDetails : allKeys.values()) {
            log.info("KeystoreManager.getKeyFileForHost getting next key authority: '{}'", keystoreDetails.getAuthority());
            String auth = keystoreDetails.getAuthority();
            if (auth != null) {
                if (auth.endsWith("*")) {
                    String s = trimPort(host);
                    if (s != null) {
                        log.info("KeystoreManager.getKeyFileForHost trimmed port: '{}'", s);
                        String a = getAnyPort(auth);
                        if (a != null) {
                            log.info("KeystoreManager.getKeyFileForHost trimmed auth: '{}'", a);
                            auth = a;
                            host = s;
                        }
                    }
                }
                if (auth.equals(host)) {
                    return keystoreDetails;
                } else if (auth.equalsIgnoreCase("default")) {
                    def = keystoreDetails;
                }
            }
        }
        return def;
    }

    public KeystoreDetails getTrustFileForHost(String host) {

        KeystoreDetails def = null;
        for (KeystoreDetails keystoreDetails : allStores.values()) {
            String auth = keystoreDetails.getAuthority();
            if (auth != null) {
                if (auth.endsWith("*")) {
                    String s = trimPort(host);
                    if (s != null) {
                        String a = getAnyPort(auth);
                        if (a != null) {
                            auth = a;
                            host = s;
                        }
                    }
                }
                if (auth.equals(host)) {
                    return keystoreDetails;
                } else if (auth.equalsIgnoreCase("default")) {
                    def = keystoreDetails;
                }
            }
        }
        return def;
    }


    public KeystoreDetails load(InputStream in) throws IOException {
        Properties props = new Properties();
        props.load(in);
        String keystoreLocation = props.getProperty("keystoreLocation");
        if (keystoreLocation == null || keystoreLocation.length() == 0) {
            throw new IOException("no location defined");
        }
        String keystorePassword = props.getProperty("keystorePassword");
        if (keystorePassword == null || keystorePassword.length() == 0) {
            throw new IOException("no keystore password defined");
        }
        String alias = props.getProperty("alias");
        String keyPassword = props.getProperty("keyPassword");
        if (keyPassword == null || keyPassword.length() == 0) {
            keyPassword = keystorePassword;
        }
        String keystoreType = props.getProperty("keystoreType");
        if (keystoreType == null || keystoreType.length() == 0) {
            keystoreType = "JKS";
        }
        String algType = props.getProperty("algType");
        if (algType == null || algType.length() == 0) {
            algType = "SunX509";
        }
        String authority = props.getProperty("authority");
        if (authority == null) {
            authority = "";
        }

        String dns = props.getProperty("authorizedDNs");
        List<String> authorizedDNs = new ArrayList<String>();
        if (dns != null && dns.length() > 0) {
            String[] dn = dns.split("&");
            for (String s : dn) {
                String decoded = URLDecoder.decode(s, "UTF-8");
                if (decoded.length() > 0) {
                    authorizedDNs.add(decoded);
                }
            }
        }
        KeystoreDetails details = new KeystoreDetails(keystoreLocation, keystorePassword, alias, keyPassword);
        details.setAlgType(algType);
        details.setKeystoreType(keystoreType);
        details.setAuthority(authority);
        for (String authorizedDN : authorizedDNs) {
            details.addAuthorizedDN(authorizedDN);
        }
        return details;
    }

    public void storeAsKey(KeystoreDetails details, String name) throws IOException {
        store(details, name, true);
    }

    public void storeAsCert(KeystoreDetails details, String name) throws IOException {
        store(details, name, false);
    }

    public boolean deleteKey(String name) {
        return delete(name, true);
    }

    public boolean deleteCert(String name) {
        return delete(name, false);
    }

    private boolean delete(String name, boolean key) {
        File f = key ? getKeysDirectory() : getCertsDirectory();
        f = new File(f, name);
        return f.delete();
    }

    private void store(KeystoreDetails details, String name, boolean key) throws IOException {
        Properties props = new Properties();
        props.setProperty("keystoreLocation", details.getKeystoreLocation());
        props.setProperty("keystorePassword", details.getKeystorePassword());
        props.setProperty("alias", details.getAlias());
        if (details.getKeyPassword() == null) {
            details.setKeyPassword("");
        }
        props.setProperty("keyPassword", details.getKeyPassword());
        props.setProperty("keystoreType", details.getKeystoreType());
        props.setProperty("algType", details.getAlgType());
        if (details.getAuthority() != null) {
            props.setProperty("authority", details.getAuthority());
        }
        List<String> authorizedDNs = details.getAuthorizedDNs();
        if (authorizedDNs.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String dn : authorizedDNs) {
                sb.append(URLEncoder.encode(dn, "UTF-8")).append("&");
            }
            props.setProperty("authorizedDNs", sb.toString());
        }
        File f = key ? getKeysDirectory() : getCertsDirectory();
        f = new File(f, name);
        FileOutputStream out = new FileOutputStream(f);
        props.store(out, "Details for " + details.getAlias() + " keystore access.");
        out.close();
    }


}
