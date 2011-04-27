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
 * ----- --------------------------------
 * 1   Machine Name, including DNS name
 * 2   IP Address
 * 3   Telephone Number
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:40:14 PM
 * @date $Date:$ modified by $Author:$
 */
public enum NetworkAccessPoint {

    MACHINE_NAME(1),
    IP_ADDRESS(2),
    TELEPHONE_NUMBER(3);

    private int value;

    private NetworkAccessPoint(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static NetworkAccessPoint getAccessPoint(int value) {
        for (NetworkAccessPoint o : values()) {
            if (o.value() == value) {
                return o;
            }
        }
        return null;
    }
}
