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
 * Value   Meaning
 * ----- -------------
 * 1       Person
 * 2       System Object
 * 3       Organization
 * 4       Other
 * </p>
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:17:13 PM
 * @date $Date:$ modified by $Author:$
 */
public enum ObjectType {
    PERSON(1),
    SYSTEM_OBJECT(2),
    ORGANIZATION(3),
    OTHER(4);

    private int value;

    ObjectType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ObjectType getType(int type) {
        for (ObjectType o : values()) {
            if (o.value() == type) {
                return o;
            }
        }
        return null;
    }

}
