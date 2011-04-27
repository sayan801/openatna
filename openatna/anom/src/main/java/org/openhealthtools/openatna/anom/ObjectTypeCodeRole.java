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

/**
 * <p>
 * Value Meaning              Participant Object Type Codes
 * ----- -------------------- ----------------------------------
 * 1   Patient              1 - Person
 * 2   Location             3 - Organization
 * 3   Report               2 - System Object
 * 4   Resource             1 - Person
 * 3 - Organization
 * 5   Master file          2 - System Object
 * 6   User                 1 - Person
 * 2 - System Object (non-human user)
 * 7   List                 2 - System Object
 * 8   Doctor               1 - Person
 * 9   Subscriber           3 - Organization
 * 10  Guarantor            1 - Person
 * 3 - Organization
 * 11  Security User Entity 1 - Person
 * 2 - System Object
 * 12  Security User Group  2 - System Object
 * 13  Security Resource    2 - System Object
 * 14  Security Granularity 2 - System Object
 * Definition
 * 15  Provider             1 - Person
 * 3 - Organization
 * 16  Data Destination     2 - System Object
 * 17  Data Repository      2 - System Object
 * 18  Schedule             2 - System Object
 * 19  Customer             3 - Organization
 * 20  Job                  2 - System Object
 * 21  Job Stream           2 - System Object
 * 22  Table                2 - System Object
 * 23  Routing Criteria     2 - System Object
 * 24  Query                2 - System Object
 * </p>
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:42:01 PM
 * @date $Date:$ modified by $Author:$
 */
public enum ObjectTypeCodeRole {

    /**
     * Value Meaning              Participant Object Type Codes
     * ----- -------------------- ----------------------------------
     * 1   Patient              1 - Person
     * 2   Location             3 - Organization
     * 3   Report               2 - System Object
     * 4   Resource             1 - Person
     * 3 - Organization
     * 5   Master file          2 - System Object
     * 6   User                 1 - Person
     * 2 - System Object (non-human user)
     * 7   List                 2 - System Object
     * 8   Doctor               1 - Person
     * 9   Subscriber           3 - Organization
     * 10  Guarantor            1 - Person
     * 3 - Organization
     * 11  Security User Entity 1 - Person
     * 2 - System Object
     * 12  Security User Group  2 - System Object
     * 13  Security Resource    2 - System Object
     * 14  Security Granularity 2 - System Object
     * Definition
     * 15  Provider             1 - Person
     * 3 - Organization
     * 16  Data Destination     2 - System Object
     * 17  Data Repository      2 - System Object
     * 18  Schedule             2 - System Object
     * 19  Customer             3 - Organization
     * 20  Job                  2 - System Object
     * 21  Job Stream           2 - System Object
     * 22  Table                2 - System Object
     * 23  Routing Criteria     2 - System Object
     * 24  Query                2 - System Object
     */

    PATIENT(1),
    LOCATION(2),
    REPORT(3),
    RESOURCE(4),
    MASTER_FILE(5),
    USER(6),
    LIST(7),
    DOCTOR(8),
    SUBSCRIBER(9),
    GUARANTOR(10),
    SECURITY_USER_ENTITY(11),
    SECURITY_USER_GROUP(12),
    SECURITY_RESOURCE(13),
    SECURITY_GRANULARITY_DEFINITION(14),
    PROVIDER(15),
    DATA_DESTINATION(16),
    DATA_REPOSITORY(17),
    SCHEDULE(18),
    CUSTOMER(19),
    JOB(20),
    JOB_STREAM(21),
    TABLE(22),
    ROUTING_CRITERIA(23),
    QUERY(24);

    private int value;

    private ObjectTypeCodeRole(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ObjectTypeCodeRole getRole(int role) {
        for (ObjectTypeCodeRole o : values()) {
            if (o.value() == role) {
                return o;
            }
        }
        return null;
    }
}
