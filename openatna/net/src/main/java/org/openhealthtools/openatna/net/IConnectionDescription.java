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

import java.util.List;
import java.util.Set;


/**
 * The basic connection description interface.
 * <p/>
 * It should be used to get information about a specific
 * connection.  You can get descriptions from the
 * connection factory, or from a specific connection.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory, IConnection
 */
public interface IConnectionDescription {

    /**
     * Used to get the other host in this connection.
     *
     * @return The IP or name of the other host.
     */
    public String getHostname();

    /**
     * Used to get the port on the remote host we connect to.
     *
     * @return The port as an integer, -1 if specific port is not set.
     */
    public int getPort();

    /**
     * Used to get the URL of the service we connect to, not
     * including the hostName or port.
     *
     * @return The url of the service, or null if not specified
     */
    public String getUrlPath();

    /**
     * Used to determine the name of this connection.
     *
     * @return Name of this connection.
     */
    public String getName();

    /**
     * Used to determine if this is an SSL/TLS connection.
     *
     * @return True if it is.
     */
    public boolean isSecure();

    /**
     * Used to get a useful, human-readable description of this
     * connection for debugging and log messages.
     *
     * @return A human-readable description of this connection
     */
    public String getDescription();

    /**
     * Get all the code type names.
     *
     * @return a set of coding type names
     */
    public Set<String> getAllCodeTypeNames();

    /**
     * Get an ebXML coding scheme that is defined for this connection.
     *
     * @param typeName The name of the coding/classification scheme
     * @return The coding scheme definition
     */
    public CodeSet getCodeSet(String typeName);

    /**
     * Get a named property that is defined for this connection.
     *
     * @param name The name of the property
     * @return The value of the property
     */
    public String getProperty(String name);

    /**
     * Get a property set that is defined for this connection.
     *
     * @param name The name of the property set
     * @return The property set
     */
    public PropertySet getPropertySet(String name);

    /**
     * Get an enum map that is defined for this connection.
     *
     * @param enumClass The enum class being mapped
     * @return The enum map definition
     */
    public EnumMap getEnumMap(Class enumClass);

    /**
     * Get a string map that is defined for this connection.
     *
     * @param name The string value type being mapped
     * @return The string map definition
     */
    public StringMap getStringMap(String name);

    /**
     * Get a hierarchical identifier that is defined for this connection.
     *
     * @param name The name of the identifier
     * @return The hierarchical identifier
     */
    public Identifier getIdentifier(String name);

    /**
     * Get all identifiers of a given type.
     *
     * @param type the type of identifier
     * @return The list of Identifiers whose type is matched with the given type.
     *         Returns an empty list if nothing is found by this type.
     */
    public List<Identifier> getAllIdentifiersByType(String type);

    /**
     * Get an object map that is defined for this connection
     *
     * @param name The string name of the object
     */
    public ObjectMap getObjectMap(String name);

    /**
     * Get an object list that is defined for this connection.
     *
     * @param name The string name of the object list
     * @return The object list
     */
    public ObjectList getObjectList(String name);

    /**
     * Get an object that is defined for this connection.
     *
     * @param name The string name of the object
     * @return The object
     */
    public ObjectEntry getObject(String name);
}
