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

package org.openhealthtools.openatna.anom;

import org.openhealthtools.openatna.anom.codes.CodeRegistry;

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


    private static AtnaCode newCode(String type, String code, String codeSystemName) {
        return CodeRegistry.getCode(type, code, null, codeSystemName);
    }

    private static AtnaCode newCode(String code) {
        return newCode(AtnaCode.EVENT_TYPE, code, "IHE Transactions");
    }

    /**
     * transaction Event type codes
     */
    public static AtnaCode eventTypePatientIdFeed() {
        return newCode("ITI-8");
    }

    public static AtnaCode eventTypePixUpdateNotification() {
        return newCode("ITI-10");
    }

    public static AtnaCode eventTypePatientDemographicsQuery() {
        return newCode("ITI-21");
    }

    public static AtnaCode eventTypePatientDemographicsAndVisitQuery() {
        return newCode("ITI-22");
    }

    public static AtnaCode eventTypeRegisterDocumentSet() {
        return newCode("ITI-14");
    }

    public static AtnaCode eventTypeProvideAndRegisterDocumentSet() {
        return newCode("ITI-15");
    }

    public static AtnaCode eventTypeRegistrySQLQuery() {
        return newCode("ITI-16");
    }

    public static AtnaCode eventTypeRetrieveDocument() {
        return newCode("ITI-17");
    }

    public static AtnaCode eventTypeRegistryStoredQuery() {
        return newCode("ITI-18");
    }

    public static AtnaCode eventTypeDistributeDocumentSetOnMedia() {
        return newCode("ITI-32");
    }

    public static AtnaCode eventTypePatientDemographicsSupplier() {
        return newCode("ITI-30");
    }

    public static AtnaCode eventTypeCrossGatewayQuery() {
        return newCode("ITI-38");
    }

    public static AtnaCode eventTypeCrossGatewayRetrieve() {
        return newCode("ITI-39");
    }

    public static AtnaCode eventTypeProvideAndRegisterDocumentSetB() {
        return newCode("ITI-41");
    }

    public static AtnaCode eventTypeRegisterDocumentSetB() {
        return newCode("ITI-42");
    }

    public static AtnaCode eventTypeRetrieveDocumentSet() {
        return newCode("ITI-43");
    }

    public static AtnaCode eventTypeRetrieveValueSet() {
        return newCode("ITI-48");
    }

    public static AtnaCode objectIdTypeSubmissionSet() {
        return newCode(AtnaCode.OBJECT_ID_TYPE, "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "IHE XDS Metadata");
    }


}
