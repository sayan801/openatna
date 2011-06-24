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

import java.io.Serializable;
import java.util.Arrays;

/**
 * Object Detail
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public class AtnaObjectDetail implements Serializable {

    private static final long serialVersionUID = -4400971126353837669L;

    private String type;
    private byte[] value;

    public String getType() {
        return type;
    }

    public AtnaObjectDetail setType(String type) {
        this.type = type;
        return this;
    }

    public byte[] getValue() {
        return value;
    }

    public AtnaObjectDetail setValue(byte[] value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AtnaObjectDetail that = (AtnaObjectDetail) o;

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (!Arrays.equals(value, that.value)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? Arrays.hashCode(value) : 0);
        return result;
    }

    public String toString() {
        byte[] bytes = getValue();
        if (bytes == null) {
            bytes = new byte[0];
        }
        return new StringBuilder().append("[")
                .append(getClass().getName())
                .append(" type=")
                .append(getType())
                .append(" value=")
                .append(new String(bytes))
                .append("]")
                .toString();
    }
}
