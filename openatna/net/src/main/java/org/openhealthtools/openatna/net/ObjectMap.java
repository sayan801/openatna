/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.net;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * This class is the contain class for object mapping between Misys Connect and participating system.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ObjectMap {
    private String name = null;
    /**
     * Used to store entries<name, ObjectMap.Entry> by connect
     */
    private Hashtable<String, Entry> entries = null;

    /**
     * Creates an ObjectMap
     *
     * @param name
     */
    public ObjectMap(String name) {
        this.name = name;
        entries = new Hashtable<String, Entry>();
    }

    /**
     * Adds an entry to this ObjectMap
     *
     * @param type        the type of the entry
     * @param connectName the name in Connect to be mapped from.
     * @param systemName  the name in the systemName to be mapped to.
     */
    public void addEntry(String type, String connectName, String systemName, String nodeName) {
        //Constrains:
        //1. connectName and systemName must not be null
        //2. when type="field", nodeName must exist
        if ((connectName != null) && (systemName != null)) { //1.
            if (type == null || !type.equals("field") || (type.equals("field") && nodeName != null)) { //2.
                Entry entry = new Entry(type, connectName, systemName, nodeName);
                entries.put(connectName, entry);
            }
        }
    }

    /**
     * @return The name of the string value types this class maps
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the object map entry from a given connect name.
     *
     * @param connectName The name used in Connect
     * @return The entry of the object map
     */
    public Entry getEntry(String connectName) {
        if (!entries.containsKey(connectName)) {
            return null;
        }

        return entries.get(connectName);

    }

    /**
     * Gets the request operation name, i.e., the request method to the system.
     *
     * @return the request operation name
     */
    public String getOperationName() {
        Set<Map.Entry<String, ObjectMap.Entry>> entrySet = entries.entrySet();
        for (Map.Entry<String, ObjectMap.Entry> entry : entrySet) {
            ObjectMap.Entry value = entry.getValue();
            String type = value.getType();
            if (type != null && type.equals("operation")) {
                return value.getSystem();
            }
        }
        return null;
    }

    /**
     * Gets the a map of connect name and its value from the system for those entries whose type is node.
     * <p/>
     * <pre>
     * XML: ObjectMap name=""
     *       Entry type="" node="" connect="" system=""
     *       ...
     * </pre>
     *
     * @return A map of connect and system for type="node".
     */
    public HashMap<String, String> getNodeMap() {
        HashMap<String, String> ret = new HashMap<String, String>();
        Set<Map.Entry<String, ObjectMap.Entry>> entrySet = entries.entrySet();
        for (Map.Entry<String, ObjectMap.Entry> entry : entrySet) {
            ObjectMap.Entry value = entry.getValue();
            String type = value.getType();
            if (type != null && type.equals("node")) {
                ret.put(value.getConnect(), value.getSystem());
            }
        }
        return ret;
    }

    /**
     * Gets the entry set of this ObjectMap.
     *
     * @return The set of entries in this ObjectMap.
     */
    public Set<Map.Entry<String, ObjectMap.Entry>> getEntries() {
        return entries.entrySet();
    }

    /**
     * The container class for an entry in the Object Map.
     */
    public class Entry {
        /**
         * The category of the entry
         */
        private String type;
        /**
         * The name used by Connect
         */
        private String connect;
        /**
         * The name used by participating system, to which Connect name is mapped to
         */
        private String system;
        /**
         * The  node from which to grab the system value
         */
        private String node;

        public Entry(String type, String connect, String system, String node) {
            this.type = type;
            this.connect = connect;
            this.system = system;
            this.node = node;
        }

        public String getType() {
            return this.type;
        }

        public String getConnect() {
            return this.connect;
        }

        public String getSystem() {
            return this.system;
        }

        public String getNode() {
            return this.node;
        }
    }
}
