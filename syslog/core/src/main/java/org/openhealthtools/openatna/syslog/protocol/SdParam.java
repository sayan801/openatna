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

package org.openhealthtools.openatna.syslog.protocol;

import java.io.Serializable;

import org.openhealthtools.openatna.syslog.SyslogException;


/**
 * A structured data parameter.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 14, 2009: 6:17:17 PM
 * @date $Date:$ modified by $Author:$
 */

public class SdParam implements Serializable {

    private String name;
    private String value;

    public SdParam(String name, String value) throws SyslogException {
        if (name == null || name.length() == 0) {
            throw new SyslogException("no name defined");
        }
        if (value == null || value.length() == 0) {
            throw new SyslogException("no value defined");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SdParam sdParam = (SdParam) o;

        if (name != null ? !name.equals(sdParam.name) : sdParam.name != null) {
            return false;
        }
        if (value != null ? !value.equals(sdParam.value) : sdParam.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
