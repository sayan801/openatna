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

package org.openhealthtools.openatna.all.test.ssl;

import java.util.ArrayList;
import java.util.List;


/**
 * interface for classes that can retrieve details required for signing a jar or authetication.
 *
 * @author Andrew Harrison
 * @version $Revision: 72 $
 * @created Mar 16, 2007: 3:13:50 PM
 * @date $Date: 2007-03-23 09:52:50 +0000 (Fri, 23 Mar 2007) $ modified by $Author: scmabh $
 * @todo Put your notes here...
 */
public class KeystoreDetails {

    private String keystoreLocation = "";
    private String keystorePassword = "";
    private String alias = "";
    private String keyPassword = null;
    private String keystoreType = "JKS";
    private String algType = "SunX509";
    private String authority = "";
    private List<String> authorizedDNs = new ArrayList<String>();

    /**
     * create a KeystoreDetails for accessing a certificate
     *
     * @param keystoreLocation
     * @param keystorePassword
     * @param alias
     * @param keyPassword
     */
    public KeystoreDetails(String keystoreLocation, String keystorePassword, String alias, String keyPassword) {
        this.keystoreLocation = keystoreLocation;
        this.keystorePassword = keystorePassword;
        this.alias = alias;
        this.keyPassword = keyPassword;
    }

    public KeystoreDetails(String keystoreLocation, String keystorePassword, String alias) {
        this.keystoreLocation = keystoreLocation;
        this.keystorePassword = keystorePassword;
        this.alias = alias;
        this.keyPassword = keystorePassword;
    }

    /**
     * constructor used when loading details from file.
     */
    public KeystoreDetails() {
    }

    public String getKeystoreLocation() {
        return keystoreLocation;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getAlias() {
        return alias;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getKeystoreType() {
        return keystoreType;
    }

    public void setKeystoreType(String keystoreType) {
        this.keystoreType = keystoreType;
    }

    public String getAlgType() {
        return algType;
    }

    public void setAlgType(String algType) {
        this.algType = algType;
    }

    /**
     * combination of host (domain or IP) and port separated by a colon.
     *
     * @return
     */
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void addAuthorizedDN(String dn) {
        if (!authorizedDNs.contains(dn)) {
            authorizedDNs.add(dn);
        }
    }

    public List<String> getAuthorizedDNs() {
        return authorizedDNs;
    }

    public void setAuthorizedDNs(List<String> authorizedDNs) {
        this.authorizedDNs = authorizedDNs;
    }

}
