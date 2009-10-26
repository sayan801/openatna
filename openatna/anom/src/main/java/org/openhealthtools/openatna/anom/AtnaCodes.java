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

import org.openhealthtools.openatna.anom.codes.CodeRegistry;

/**
 * org.openhealthtools.openatna.anom.Codes
 * <p/>
 * generates predefined codes
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 7, 2009: 8:43:29 AM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaCodes {

    private static AtnaCode sourceTypeCode(String code) {
        return CodeRegistry.getCode(AtnaCode.SOURCE_TYPE, code, null, "RFC-3881");
    }

    private static AtnaCode participantCode(String code) {
        return CodeRegistry.getCode(AtnaCode.PARTICIPANT_ROLE_TYPE, code, null, "RFC-3881");
    }

    /**
     * Audit Source Type Codes:
     * <p/>
     * <p/>
     * Value  Meaning
     * -----  ------------------------------------------------------
     * 1    End-user interface
     * 2    Data acquisition device or instrument
     * 3    Web server process tier in a multi-tier system
     * 4    Application server process tier in a multi-tier system
     * 5    Database server process tier in a multi-tier system
     * 6    Security server, e.g., a domain controller
     * 7    ISO level 1-3 network component
     * 8    ISO level 4-6 operating software
     * 9    External source, other or unknown type
     */


    public static AtnaCode sourceTypeEndUserInterface() {
        return sourceTypeCode("1");
    }

    public static AtnaCode sourceTypeInstrument() {
        return sourceTypeCode("2");
    }

    public static AtnaCode sourceTypeWebServerProcess() {
        return sourceTypeCode("3");
    }

    public static AtnaCode sourceTypeAppServerProcess() {
        return sourceTypeCode("4");
    }

    public static AtnaCode sourceTypeDatabaseServerProcess() {
        return sourceTypeCode("5");
    }

    public static AtnaCode sourceTypeSecurityServer() {
        return sourceTypeCode("6");
    }

    public static AtnaCode sourceTypeIsoNetworkComponent() {
        return sourceTypeCode("7");
    }

    public static AtnaCode sourceTypeIsoOperatingSoftware() {
        return sourceTypeCode("8");
    }

    public static AtnaCode sourceTypeUnknown() {
        return sourceTypeCode("9");
    }

    /**
     * Participant Object ID Type Codes
     * <p/>
     * Value      Meaning           Participant Object Type Codes
     * ----- ---------------------- -----------------------------
     * 1      Medical Record Number  1 - Person
     * 2      Patient Number         1 - Person
     * 3      Encounter Number       1 - Person
     * 4      Enrollee Number        1 - Person
     * 5      Social Security Number 1 - Person
     * 6      Account Number         1 - Person
     * 3 - Organization
     * 7      Guarantor Number       1 - Person
     * 3 - Organization
     * 8      Report Name            2 - System Object
     * 9      Report Number          2 - System Object
     * 10     Search Criteria        2 - System Object
     * 11     User Identifier        1 - Person
     * 2 - System Object
     * 12  URI                       2 - System Object
     */

    public static AtnaCode objectIdTypeMedicalRecordNumber() {
        return participantCode("1");
    }

    public static AtnaCode objectIdTypePatientNumber() {
        return participantCode("2");
    }

    public static AtnaCode objectIdTypeEncounterNumber() {
        return participantCode("3");
    }

    public static AtnaCode objectIdTypeEnrolleeNumber() {
        return participantCode("4");
    }

    public static AtnaCode objectIdTypeSSNumber() {
        return participantCode("5");
    }

    public static AtnaCode objectIdTypeAccountNumber() {
        return participantCode("6");
    }

    public static AtnaCode objectIdTypeGuarantorNumber() {
        return participantCode("7");
    }

    public static AtnaCode objectIdTypeReportName() {
        return participantCode("8");
    }

    public static AtnaCode objectIdTypeReportNumber() {
        return participantCode("9");
    }

    public static AtnaCode objectIdTypeSearchCriteria() {
        return participantCode("10");
    }

    public static AtnaCode objectIdTypeUserId() {
        return participantCode("11");
    }

    public static AtnaCode objectIdUri() {
        return participantCode("12");
    }

}