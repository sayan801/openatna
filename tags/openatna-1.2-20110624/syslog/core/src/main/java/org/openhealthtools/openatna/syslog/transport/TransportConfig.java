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

package org.openhealthtools.openatna.syslog.transport;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for configuring servers. Not required by SyslogServers
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 12:32:02 PM
 * @date $Date:$ modified by $Author:$
 */

public abstract class TransportConfig {

    private String name;

    private Map<String, Object> properties = new HashMap<String, Object>();

    public TransportConfig(String name) {
        this.name = name;
    }

    public TransportConfig(String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }


}
