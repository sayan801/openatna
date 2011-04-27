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
 * Value Meaning
 * ----- --------------------------------------
 * 1   Origination / Creation
 * 2   Import / Copy from original
 * 3   Amendment
 * 4   Verification
 * 5   Translation
 * 6   Access / Use
 * 7   De-identification
 * 8   Aggregation, summarization, derivation
 * 9   Report
 * 10   Export / Copy to target
 * 11   Disclosure
 * 12   Receipt of disclosure
 * 13   Archiving
 * 14   Logical deletion
 * 15   Permanent erasure / Physical destruction
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:50:03 PM
 * @date $Date:$ modified by $Author:$
 */
public enum ObjectDataLifecycle {

    ORIGINATION(1),
    IMPORT(2),
    AMENDMENT(3),
    VERIFICATION(4),
    TRANSLATION(5),
    ACCESS(6),
    DE_IDENTIFICATION(7),
    AGGREGATION(8),
    REPORT(9),
    EXPORT(10),
    DISCLOSURE(11),
    RECEIPT_OF_DISCLOSURE(12),
    ARCHIVING(13),
    LOGICAL_DELETION(14),
    PERMANENT_ERASURE(15);

    private int value;

    ObjectDataLifecycle(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ObjectDataLifecycle getLifecycle(int value) {
        for (ObjectDataLifecycle o : values()) {
            if (o.value() == value) {
                return o;
            }
        }
        return null;
    }

}
