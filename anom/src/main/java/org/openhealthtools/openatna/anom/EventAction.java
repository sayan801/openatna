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
 * Value Meaning               Examples
 * ----- --------------------- ----------------------------------
 * C   Create                Create a new database object, such as Placing an Order.
 * R   Read/View/Print/Query Display or print data, such as a Doctor Census
 * U   Update                Update data, such as Revise Patient Information
 * D   Delete                Delete items, such as a doctor master file record
 * E   Execute               Perform a system or application function such as log-on, program
 * execution, or use of an object's method
 * </p>
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public enum EventAction {

    CREATE("C"),
    READ("R"),
    UPDATE("U"),
    DELETE("D"),
    EXECUTE("E");


    private String value;

    private EventAction(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static EventAction getAction(String value) {
        for (EventAction o : values()) {
            if (o.value().equals(value)) {
                return o;
            }
        }
        return null;
    }

}
