/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
 *
 */
package org.openhealthtools.openatna.net;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.utils.Pair;


/**
 * A description of an insecure connection. <p />
 * <p/>
 * This should only be obtained throught the ConnectionFactory's
 * getConnectionDescription function.
 *
 * @author Josh Flachsbart
 */
public class StandardConnectionDescription implements IConnectionDescription {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.net.StandardConnectionDescription");

    /* Standard properties of all connections */
    private String name = null;
    private String hostName = null;
    private int port = -1;
    private String urlPath = null;

    /* Used by both this and secure connection descriptions */
    protected boolean complete = false;
    /*Used to track the nest level that this description is invoket*/
    protected int invokedLevel = 0;

    /* Loaded via XML configuration files */
    private Hashtable<String, String> properties = null;
    private Hashtable<String, PropertySet> propertySets = null;
    private Hashtable<String, CodeSet> codingSchemes = null;
    private Hashtable<Class, EnumMap> enumMaps = null;
    private Hashtable<String, StringMap> stringMaps = null;
    //Pair<String(type), Identifier>
    private Hashtable<String, Pair> identifiers = null;
    private Hashtable<String, ObjectMap> objectMaps = null;
    private Hashtable<String, ObjectEntry> objects = null;
    private Hashtable<String, ObjectList> objectLists = null;

    /**
     * Used by the factory to make a connection with a specified port. <p />
     * <p/>
     * After creation, all of the elements of the connection description
     * must be set by the factory.  This should not be instantiated outside
     * of the factory, nor should any of the setters be used after creation.
     */
    public StandardConnectionDescription() {
        log.debug("Creating standard description.");
    }

    /**
     * Increment the invocation level of this connection description is called.
     *
     * @return the invocation nested level
     */
    public int invoke() {
        return invokedLevel++;
    }

    /**
     * Called by the factory to note that the initalization is complete. <p />
     * <p/>
     * This also checks to make sure that all the instantiation has happened
     * properly.  If not it returns false.
     */
    public boolean complete() {
        invokedLevel--;
        //If there is still an invoked level, the description process is not yet finished.
        if (invokedLevel >= 1) {
            return false;
        }

        if ((hostName != null)) {//&& (name != null) ) {
            complete = true;
        } else {
            log.warn("Attempt to complete invalid standard connection description.");
        }
        return complete;
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setHostname(String hostName) {
        if (!complete) {
            this.hostName = hostName;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setPort(int port) {
        if (!complete) {
            this.port = port;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setUrlPath(String urlPath) {
        if (!complete) {
            this.urlPath = urlPath;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setName(String name) {
        if (!complete) {
            this.name = name;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }


    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getHostname()
      */
    public String getHostname() {
        return hostName;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getName()
      */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getPort()
      */
    public int getPort() {
        return port;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getUrlPath()
      */
    public String getUrlPath() {
        return urlPath;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#isSecure()
      */
    public boolean isSecure() {
        return false;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getDescription()
      */
    public String getDescription() {
        StringBuffer sb = new StringBuffer();
        if (name != null) {
            // "name (host:port TLS)"
            sb.append(name);
            if (hostName != null) {
                sb.append('(');
                sb.append(hostName);
                if (port >= 0) {
                    sb.append(':');
                    sb.append(port);
                }
                if (isSecure()) {
                    sb.append(" TLS");
                }
                sb.append(')');
            }
        } else if (hostName != null) {
            // "host:port (TLS)"
            sb.append(hostName);
            if (port >= 0) {
                sb.append(':');
                sb.append(port);
            }
            if (isSecure()) {
                sb.append(" (TLS)");
            }
        }
        // Done
        if (sb.length() <= 0) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Load configuration information for a single connection from an XML configuration file.
     * This method is used only for for MESA and unit tests.
     *
     * @param filename The name of the configuration file to load
     * @return True if the file was loaded without error
     */
    public boolean loadConfiguration(String filename) {
        return DescriptionLoader.processDescriptionIncludeFile(this, new File(filename));
    }

    public Set<String> getAllCodeTypeNames() {
        if (codingSchemes != null) {
            return codingSchemes.keySet();
        } else {
            log.debug("Looking up non-existent coding scheme: " + name);
            return null;
        }

    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getCodeSet()
      */
    public CodeSet getCodeSet(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((codingSchemes != null) && (codingSchemes.containsKey(key))) {
            return codingSchemes.get(key);
        } else {
            log.debug("Looking up non-existent coding scheme: " + name);
            return null;
        }
    }

    /**
     * Add a new coding set to this connection description.  This method is used when loading
     * configuration files.
     *
     * @param codingScheme The CodeSet to add
     */
    public void addCodeSet(CodeSet codingScheme) {
        if (codingScheme != null) {
            String schemeName = codingScheme.getCodeType();
            if (schemeName != null) {
                if (codingSchemes == null) {
                    codingSchemes = new Hashtable<String, CodeSet>();
                }
                codingSchemes.put(schemeName.toLowerCase(), codingScheme);
            } else {
                log.debug("Adding coding scheme with no name to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getProperty()
      */
    public String getProperty(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((properties != null) && (properties.containsKey(key))) {
            return properties.get(key);
        } else {
            log.debug("Looking up non-existent property: " + name);
            return null;
        }
    }

    /**
     * Set the value of a property for this connection.  This method is typically used only
     * in initialization and testing.
     *
     * @param name  The name of the property
     * @param value The value of the property
     */
    public void setProperty(String name, String value) {
        if (name != null) {
            if (value != null) {
                if (properties == null) {
                    properties = new Hashtable<String, String>();
                }
                properties.put(name.toLowerCase(), value);
            } else {
                if (properties != null) {
                    properties.remove(name.toLowerCase());
                }
            }
        }
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getPropertySet()
      */
    public PropertySet getPropertySet(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((propertySets != null) && (propertySets.containsKey(key))) {
            return propertySets.get(key);
        } else {
            log.debug("Looking up non-existent property set: " + name);
            return null;
        }
    }

    /**
     * Add a new property set to this connection description.  This method is used when loading
     * configuration files.
     *
     * @param set The property set to add
     */
    public void addPropertySet(PropertySet set) {
        if (set != null) {
            String setName = set.getName();
            if (setName != null) {
                if (propertySets == null) {
                    propertySets = new Hashtable<String, PropertySet>();
                }
                propertySets.put(setName.toLowerCase(), set);
            } else {
                log.debug("Adding property set with no name to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getEnumMap()
      */
    public EnumMap getEnumMap(Class enumClass) {
        if (enumClass == null) {
            return null;
        }
        if ((enumMaps != null) && (enumMaps.containsKey(enumClass))) {
            return enumMaps.get(enumClass);
        } else {
            log.debug("Looking up non-existent enum map: " + enumClass.getSimpleName());
            return null;
        }
    }

    /**
     * Add a new enum map to this connection description.  This method is used when loading
     * configuration files.
     *
     * @param enumMap The enum map to add
     */
    public void addEnumMap(EnumMap enumMap) {
        if (enumMap != null) {
            Class enumClass = enumMap.getEnumClass();
            if (enumClass != null) {
                if (enumMaps == null) {
                    enumMaps = new Hashtable<Class, EnumMap>();
                }
                enumMaps.put(enumClass, enumMap);
            } else {
                log.debug("Adding enum map with no enum class to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getStringMap()
      */
    public StringMap getStringMap(String name) {
        if (name == null) {
            return null;
        }
        if ((stringMaps != null) && (stringMaps.containsKey(name))) {
            return stringMaps.get(name);
        } else {
            log.debug("Looking up non-existent string map: " + name);
            return null;
        }
    }

    /**
     * Add a new string map to this connection description.  This method is used when loading
     * configuration files.
     *
     * @param stringMap The string map to add
     */
    public void addStringMap(StringMap stringMap) {
        if (stringMap != null) {
            String mapName = stringMap.getName();
            if (mapName != null) {
                if (stringMaps == null) {
                    stringMaps = new Hashtable<String, StringMap>();
                }
                stringMaps.put(mapName, stringMap);
            } else {
                log.debug("Adding string map with no name to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getObjectMap()
      */
    public ObjectMap getObjectMap(String name) {
        if (name == null) {
            return null;
        }
        if ((objectMaps != null) && (objectMaps.containsKey(name))) {
            return objectMaps.get(name);
        } else {
            log.debug("Looking up non-existent object map: " + name);
            return null;
        }
    }

    /**
     * Add a new object map to this connection description. This method is used when loading
     * configuration files.
     *
     * @param objectMap The object map to add
     */
    public void addObjectMap(ObjectMap objectMap) {
        if (objectMap != null) {
            String mapName = objectMap.getName();
            if (mapName != null) {
                if (objectMaps == null) {
                    objectMaps = new Hashtable<String, ObjectMap>();
                }
                objectMaps.put(mapName, objectMap);
            } else {
                log.debug("Adding object map with no name to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
    * @see org.openhealthtools.openatna.net.IConnectionDescription#getObject()
    */
    public ObjectEntry getObject(String name) {
        if (name == null) {
            return null;
        }
        if ((objects != null) && (objects.containsKey(name))) {
            return objects.get(name);
        } else {
            log.debug("Looking up non-existent object: " + name);
            return null;
        }
    }

    /**
     * Add a new object to this connection description. This method is used when loading
     * configuration files.
     *
     * @param object The object to add
     */
    public void addObject(ObjectEntry object) {
        if (object != null) {
            String mapName = object.getName();
            if (mapName != null) {
                if (objects == null) {
                    objects = new Hashtable<String, ObjectEntry>();
                }
                objects.put(mapName, object);
            } else {
                log.debug("Adding object with no name to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
    * @see org.openhealthtools.openatna.net.IConnectionDescription#getObjectList()
    */
    public ObjectList getObjectList(String name) {
        if (name == null) {
            return null;
        }
        if ((objectLists != null) && (objectLists.containsKey(name))) {
            return objectLists.get(name);
        } else {
            log.debug("Looking up non-existent object list: " + name);
            return null;
        }
    }

    /**
     * Add a new object list to this connection description. This method is used when loading
     * configuration files.
     *
     * @param objectList The object list to add
     */
    public void addObjectList(ObjectList objectList) {
        if (objectList != null) {
            String mapName = objectList.getName();
            if (mapName != null) {
                if (objectLists == null) {
                    objectLists = new Hashtable<String, ObjectList>();
                }
                objectLists.put(mapName, objectList);
            } else {
                log.debug("Adding object list with no name to connection: " + name);
            }
        }
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnectionDescription#getAssigningAuthority()
      */
    public Identifier getIdentifier(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((identifiers != null) && (identifiers.containsKey(key))) {
            return (Identifier) identifiers.get(key)._second;
        } else {
            log.debug("Looking up non-existent identifier: " + name);
            return null;
        }
    }


    /**
     * Get all identifiers of a given type.
     *
     * @param type the type of identifier
     * @return The list of Identifiers whose type is matched with the given type.
     *         Returns an empty list if nothing is found by this type.
     */
    public List<Identifier> getAllIdentifiersByType(String type) {
        List<Identifier> ret = new ArrayList<Identifier>();
        if (type == null || identifiers == null) {
            return ret;
        }

        String lowType = type.toLowerCase();
        Set<String> keys = identifiers.keySet();
        for (String key : keys) {
            //_first is type; _second is Identifier
            String tempType = (String) identifiers.get(key)._first;
            if (tempType == null) {
                continue;
            }
            if (lowType.equalsIgnoreCase(tempType)) {
                ret.add((Identifier) identifiers.get(key)._second);
            }
        }
        return ret;
    }


    /**
     * Add a new identifier with a given type to this connection description.
     * This method is used when loading configuration files.
     *
     * @param name      The required name of this Identifier
     * @param type      The optional type of this Identifier
     * @param authority The identifier to add
     */
    public void addIdentifier(String name, String type, Identifier authority) {
        if (authority != null) {
            if (name != null) {
                if (identifiers == null) {
                    identifiers = new Hashtable<String, Pair>();
                }
                identifiers.put(name.toLowerCase(), new Pair(type, authority));
            } else {
                log.debug("Adding identifier with no name to connection: " + this.name);
            }
        }
    }
}
