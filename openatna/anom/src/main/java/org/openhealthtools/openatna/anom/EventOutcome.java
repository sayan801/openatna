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
 * Value Meaning
 * ---- ----------------------------------------------------
 * 0   Success
 * 4   Minor failure; action restarted, e.g., invalid password with first retry
 * 8   Serious failure; action terminated, e.g., invalid password with excess retries
 * 12   Major failure; action made unavailable, e.g., user account disabled due to excessive invalid log-on attempts
 * </p>
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:35:13 PM
 * @date $Date:$ modified by $Author:$
 */
public enum EventOutcome {

    SUCCESS(0),
    MINOR_FAILURE(4),
    SERIOUS_FAILURE(8),
    MAJOR_FAILURE(12);

    private int value;

    private EventOutcome(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static EventOutcome getOutcome(int value) {
        for (EventOutcome o : values()) {
            if (o.value() == value) {
                return o;
            }
        }
        return null;
    }
}
