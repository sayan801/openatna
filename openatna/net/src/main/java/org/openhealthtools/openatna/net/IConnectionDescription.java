package org.openhealthtools.openatna.net;

import java.util.List;
import java.util.Set;

/**
 * The basic connection description interface.
 * <p/>
 * It should be used to get information about a specific connection.
 * You can get descriptions from the connection factory, or from a specific connection.
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
    String getHostname();

    /**
     * Used to get the port on the remote host we connect to.
     *
     * @return The port as an integer, -1 if specific port is not set.
     */
    int getPort();

    /**
     * Used to get the URL of the service we connect to, not
     * including the hostName or port.
     *
     * @return The url of the service, or null if not specified
     */
    String getUrlPath();

    /**
     * Used to determine the name of this connection.
     *
     * @return Name of this connection.
     */
    String getName();

    /**
     * Used to determine if this is an SSL/TLS connection.
     *
     * @return True if it is.
     */
    boolean isSecure();

    /**
     * Used to get a useful, human-readable description of this
     * connection for debugging and log messages.
     *
     * @return A human-readable description of this connection
     */
    String getDescription();

    /**
     * Get all the code type names.
     *
     * @return a set of coding type names
     */
    Set<String> getAllCodeTypeNames();

    /**
     * Get an ebXML coding scheme that is defined for this connection.
     *
     * @param typeName The name of the coding/classification scheme
     * @return The coding scheme definition
     */
    CodeSet getCodeSet(String typeName);

    /**
     * Get a named property that is defined for this connection.
     *
     * @param name The name of the property
     * @return The value of the property
     */
    String getProperty(String name);

    /**
     * Get a property set that is defined for this connection.
     *
     * @param name The name of the property set
     * @return The property set
     */
    PropertySet getPropertySet(String name);

    /**
     * Get an enum map that is defined for this connection.
     *
     * @param enumClass The enum class being mapped
     * @return The enum map definition
     */
    EnumMap getEnumMap(Class enumClass);

    /**
     * Get a string map that is defined for this connection.
     *
     * @param name The string value type being mapped
     * @return The string map definition
     */
    StringMap getStringMap(String name);

    /**
     * Get a hierarchical identifier that is defined for this connection.
     *
     * @param name The name of the identifier
     * @return The hierarchical identifier
     */
    Identifier getIdentifier(String name);

    /**
     * Get all identifiers of a given type.
     *
     * @param type the type of identifier
     * @return The list of Identifiers whose type is matched with the given type.
     * Returns an empty list if nothing is found by this type.
     */
    List<Identifier> getAllIdentifiersByType(String type);

    /**
     * Get an object map that is defined for this connection
     *
     * @param name The string name of the object
     */
    ObjectMap getObjectMap(String name);

    /**
     * Get an object list that is defined for this connection.
     *
     * @param name The string name of the object list
     * @return The object list
     */
    ObjectList getObjectList(String name);

    /**
     * Get an object that is defined for this connection.
     *
     * @param name The string name of the object
     * @return The object
     */
    ObjectEntry getObject(String name);
}
