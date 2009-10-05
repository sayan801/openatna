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

    private static AtnaCode newCode(String code, String displayName) {
        return new AtnaCode(code, null, "RFC-3881", displayName, null);
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
        return newCode("1", "End User Display Device");
    }

    public static AtnaCode sourceTypeInstrument() {
        return newCode("2", "Data Acquisition Device");
    }

    public static AtnaCode sourceTypeWebServerProcess() {
        return newCode("3", "Web Server Process");
    }

    public static AtnaCode sourceTypeAppServerProcess() {
        return newCode("4", "Application Server Process");
    }

    public static AtnaCode sourceTypeDatabaseServerProcess() {
        return newCode("5", "Database Server Process");
    }

    public static AtnaCode sourceTypeSecurityServer() {
        return newCode("6", "Security Server");
    }

    public static AtnaCode sourceTypeIsoNetworkComponent() {
        return newCode("7", "Network Component");
    }

    public static AtnaCode sourceTypeIsoOperatingSoftware() {
        return newCode("8", "Operating Software");
    }

    public static AtnaCode sourceTypeUnknown() {
        return newCode("9", "External Source");
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
        return newCode("1", "Medical Record Number");
    }

    public static AtnaCode objectIdTypePatientNumber() {
        return newCode("2", "Patient Number");
    }

    public static AtnaCode objectIdTypeEncounterNumber() {
        return newCode("3", "Encounter Number");
    }

    public static AtnaCode objectIdTypeEnrolleeNumber() {
        return newCode("4", "Enrollee Number");
    }

    public static AtnaCode objectIdTypeSSNumber() {
        return newCode("5", "Social Security Number ");
    }

    public static AtnaCode objectIdTypeAccountNumber() {
        return newCode("6", "Account Number");
    }

    public static AtnaCode objectIdTypeGuarantorNumber() {
        return newCode("7", "Guarantor Number");
    }

    public static AtnaCode objectIdTypeReportName() {
        return newCode("8", "Report Name");
    }

    public static AtnaCode objectIdTypeReportNumber() {
        return newCode("9", "Report Nunber");
    }

    public static AtnaCode objectIdTypeSearchCriteria() {
        return newCode("10", "Search Criteria");
    }

    public static AtnaCode objectIdTypeUserId() {
        return newCode("11", "User Identifier");
    }

    public static AtnaCode objectIdUri() {
        return newCode("12", "URI");
    }
}
