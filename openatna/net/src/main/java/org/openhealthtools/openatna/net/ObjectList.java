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

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the contain class for object list used to define a list of objects. The main use of this
 * class is to provide default data for document builder.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ObjectList {
    private String name = null;
    List<ObjectEntry> objects = new ArrayList<ObjectEntry>();

    /**
     * Creates an ObjectList
     *
     * @param name
     */
    public ObjectList(String name) {
        this.name = name;
    }

    /**
     * Gets the size of this object.
     *
     * @return The size of this ObjectList.
     */
    public int size() {
        return objects.size();
    }

    /**
     * @return The name of this ObjectList
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an object entry to this object list
     *
     * @param entry
     */
    public void addListEntry(ObjectEntry entry) {
        objects.add(entry);
    }

    /**
     * Gets the list of objectentry.
     *
     * @param index
     * @return The ObjectEntry
     */
    public ObjectEntry getListEntry(int index) {
        return objects.get(index);
    }

    /**
     * This classes defines an Object in the ObjectEntry.
     */
    /*   public static class ObjectEntry {
        private HashMap<String, String> values = new HashMap<String, String>();

        public ObjectEntry() {}

        public boolean containsValue(String valueName) {
            if (valueName == null) return false;
            return values.containsKey(valueName);
        }

        public String getValue(String valueName) {
            if (valueName == null) return null;
            return values.get(valueName);
        }

        public void addValue(String valueName, String value) {
            if (valueName == null) return;
            if (value == null) {
                values.remove(valueName);
            } else {
                values.put(valueName, value);
            }
        }
    }*/
}
