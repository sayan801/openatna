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

package org.openhealthtools.openatna.anom;

/**
 * org.openhealthtools.openatna.anom.IheCodes
 * <p/>
 * returns IHE standard codes.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 7, 2009: 9:52:35 AM
 * @date $Date:$ modified by $Author:$
 */

public class IheCodes {


    private static AtnaCode newCode(String code, String displayName, String codeSystemName) {
        return new AtnaCode(code, null, codeSystemName, displayName);
    }

    private static AtnaCode newCode(String code, String displayName) {
        return new AtnaCode(code, null, "IHE Transactions", displayName);
    }

    /**
     * transaction Event type codes
     */
    public static AtnaCode eventTypePatientIdFeed() {
        return newCode("ITI-8", "Patient Identity Feed");
    }

    public static AtnaCode eventTypePixUpdateNotification() {
        return newCode("ITI-10", "PIX Update Notification");
    }

    public static AtnaCode eventTypePatientDemographicsQuery() {
        return newCode("ITI-21", "Patient Demographics Query");
    }

    public static AtnaCode eventTypePatientDemographicsAndVisitQuery() {
        return newCode("ITI-22", "Patient Demographics and Visit Query");
    }

    public static AtnaCode eventTypeRegisterDocumentSet() {
        return newCode("ITI-14", "Register Document Set");
    }

    public static AtnaCode eventTypeProvideAndRegisterDocumentSet() {
        return newCode("ITI-15", "Provide and Register Document Set");
    }

    public static AtnaCode eventTypeRegistrySQLQuery() {
        return newCode("ITI-16", "Registry SQL Query");
    }

    public static AtnaCode eventTypeRetrieveDocument() {
        return newCode("ITI-17", "Retrieve Document");
    }

    public static AtnaCode eventTypeRegistryStoredQuery() {
        return newCode("ITI-18", "Registry Stored Query");
    }

    public static AtnaCode eventTypeDistributeDocumentSetOnMedia() {
        return newCode("ITI-32", "Distribute Document Set on Media");
    }

    public static AtnaCode eventTypePatientDemographicsSupplier() {
        return newCode("ITI-30", "Patient Identity Management");
    }

    public static AtnaCode eventTypeCrossGatewayQuery() {
        return newCode("ITI-38", "Cross Gateway Query");
    }

    public static AtnaCode eventTypeCrossGatewayRetrieve() {
        return newCode("ITI-39", "Cross Gateway Retrieve");
    }

    public static AtnaCode eventTypeProvideAndRegisterDocumentSetB() {
        return newCode("ITI-41", "Provide and Register Document Set-b");
    }

    public static AtnaCode eventTypeRegisterDocumentSetB() {
        return newCode("ITI-42", "Register Document Set-b");
    }

    public static AtnaCode eventTypeRetrieveDocumentSet() {
        return newCode("ITI-43", "Retrieve Document Set");
    }

    public static AtnaCode eventTypeRetrieveValueSet() {
        return newCode("ITI-48", "Retrieve Value Set");
    }

    public static AtnaCode objectIdTypeSubmissionSet() {
        return newCode("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "SubmissionSet ClassificationNode", "IHE XDS Metadata");
    }


}
