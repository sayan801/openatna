package org.openhealthtools.openatna.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * This class loads connection description configuration files and creates
 * a set of ConnectionDescription objects.
 *
 * @author Jim Firby
 */
public class DescriptionLoader {

    /* Logger for debugging messages */

    private static final Logger LOGGER = LoggerFactory.getLogger(DescriptionLoader.class);

    /**
     * Load connection descriptions from the given filename.  The filename
     * must point to an XML configuration file.
     *
     * @param filename The XML connection configuration file to load
     * @return A list of connection descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    public static List<IConnectionDescription> loadConnectionDescriptions(String filename)
            throws SAXException, IOException, ParserConfigurationException {
        return loadConnectionDescriptions(new File(filename));
    }

    /**
     * Load connection descriptions from the given File object.  The File
     * must point to an XML configuration file.
     *
     * @param file The Java File object pointing to the XML configuration file
     * @return A list of connection descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    public static List<IConnectionDescription> loadConnectionDescriptions(File file) throws SAXException, IOException, ParserConfigurationException {
        ArrayList<IConnectionDescription> allDescriptions = new ArrayList<>();
        addConnectionDescriptions(allDescriptions, file);
        return allDescriptions;
    }

    /**
     * Process a configuration file containing connection descriptions and add any
     * connections found to the list of connection descriptions passed in.
     *
     * @param descriptions The list of connection descriptions to be extended
     * @param file         The configuration file holding more connection descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    private static void addConnectionDescriptions(List<IConnectionDescription> descriptions, File file)
            throws SAXException, IOException, ParserConfigurationException {
        // Create a builder factory and a builder, and get the document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = factory.newDocumentBuilder().parse(file);
        // Get all the connection descriptions from the root node.
        //TODO check that the root node is NETOWRKCONFIGURATION.
        NodeList allDescriptionElements = doc.getDocumentElement().getChildNodes();
        // Process each one
        for (int elementIndex = 0; elementIndex < allDescriptionElements.getLength(); elementIndex++) {
            Node currentDescriptionElement = allDescriptionElements.item(elementIndex);
            String name = currentDescriptionElement.getNodeName();
            if (name.equalsIgnoreCase("SECURECONNECTION") || name.equalsIgnoreCase("STANDARDCONNECTION")) {
                // A connection description, process it
                IConnectionDescription currentDescription = processDescriptionNode(currentDescriptionElement, file);
                if (currentDescription != null) {
                    descriptions.add(currentDescription);
                }
            } else if (name.equalsIgnoreCase("INCLUDEFILE")) {
                // A top-level include file, it will contain more connection descriptions
                String filename = getAttributeValue(currentDescriptionElement, "name");
                if (filename == null) {
                    filename = getNodeAsText(currentDescriptionElement);
                }
                if (filename != null) {
                    try {
                        File includeFile = new File(file.getParentFile(), filename.trim());
                        addConnectionDescriptions(descriptions, includeFile);
                    } catch (Exception e) {
                        logIncludeFileError(filename, null, e);
                    }
                } else {
                    logMissingAttributeWarning(name, "name", null);
                }
            } else if (currentDescriptionElement instanceof Element) {
                // An XML element, if we don't know what kind, it probably shouldn't be there
                logUnexpectedTagWarning(name, null);
            }

        }
    }

    static IConnectionDescription processDescriptionNode(Node description, File parent) {
        StandardConnectionDescription connection = null;
        // Build the right sort of connection
        String name = description.getNodeName();
        if (name.equalsIgnoreCase("SECURECONNECTION")) {
            connection = new SecureConnectionDescription();
        } else if (name.equalsIgnoreCase("STANDARDCONNECTION")) {
            connection = new StandardConnectionDescription();
        } else {
            logUnexpectedTagWarning(name, null);
        }
        // Load all of its properties from the XML
        if (connection != null) {
            connection.setName(getAttributeValue(description, "name"));
            processDescription(connection, description.getChildNodes(), parent);
        }
        // Done
        return connection;
    }

    /**
     * Process a single connection description DOM object and update the connection
     * description object it is describing.
     *
     * @param description      The connection description object being described
     * @param descriptionNodes The XML DOM description being processed
     * @param parent           The file that holds this description
     */
    private static void processDescription(StandardConnectionDescription description, NodeList descriptionNodes, File parent) {
        description.invoke();
        // Loop over every item in the connection description XML
        for (int valueIndex = 0; valueIndex < descriptionNodes.getLength(); valueIndex++) {
            Node item = descriptionNodes.item(valueIndex);
            String itemName = item.getNodeName();
            // If this is a standard connection value, process is
            boolean handled = handleStandardConnectionValue(itemName, item, description);
            // If this is a secure connection value, process it
            if (!handled) {
                handled = handleSecureConnectionValue(itemName, item, description, parent);
            }
            // A connection actor value
            if (!handled) {
                if (itemName.equalsIgnoreCase("CODETYPE")) {
                    // A set of ebXML Repository Classification codes
                    processCodingScheme(description, item);
                } else if (itemName.equalsIgnoreCase("ENUMMAP")) {
                    // An enum map from Connect ENUM types to IHE Affinity Domain Codes.
                    processEnumMap(description, item);
                } else if (itemName.equalsIgnoreCase("STRINGMAP")) {
                    // An enum map from Connect string types to IHE Affinity Domain Codes.
                    processStringMap(description, item);
                } else if (itemName.equalsIgnoreCase("PROPERTYSET")) {
                    // A set of related properties needed to map to the IHS Affinity Domain.
                    processPropertySet(description, item);
                } else if (itemName.equalsIgnoreCase("IDENTIFIER")) {
                    // A set of related properties needed to map to the IHS Affinity Domain.
                    processIdentifier(description, item);
                } else if (itemName.equalsIgnoreCase("INCLUDEFILE")) {
                    // A nested file inclusion, process it
                    String filename = getAttributeValue(item, "name");
                    if (filename == null) {
                        filename = getNodeAsText(item);
                    }
                    if (filename != null) {
                        File includeFile = new File(parent.getParentFile(), filename);
                        processDescriptionIncludeFile(description, includeFile);
                    } else {
                        logMissingAttributeWarning(itemName, "name", description);
                    }
                } else if (itemName.equalsIgnoreCase("PROPERTY")) {
                    // A property to associate with this connection
                    String propertyName = getAttributeValue(item, "name");
                    String propertyValue = getAttributeValue(item, "value");
                    if (propertyValue == null) {
                        propertyValue = getNodeAsText(item);
                    }
                    if (propertyName != null) {
                        description.setProperty(propertyName, propertyValue);
                    } else {
                        logMissingAttributeWarning(itemName, "name", description);
                    }
                } else if (itemName.equalsIgnoreCase("OBJECTMAP")) {
                    //An object map from Misys Connect to a participating system
                    processObjectMap(description, item);
                } else if (itemName.equalsIgnoreCase("OBJECT")) {
                    //An object definition
                    processObjectEntry(description, item);
                } else if (itemName.equalsIgnoreCase("OBJECTLIST")) {
                    //An object list to define a list of objects
                    processObjectList(description, item);
                } else if (item instanceof Element) {
                    // An XML element, if we don't know what kind, it probably shouldn't be there
                    logUnexpectedTagWarning(itemName, description);
                }
            }
        }
        // Done, finish processing the description
        description.complete();
    }

    /**
     * Process a configuration file included within the description of a single connection.
     *
     * @param description The connection description object being described
     * @param file        The included configuration file describing that connection
     * @return True if the included file was processed without error
     */
    public static boolean processDescriptionIncludeFile(StandardConnectionDescription description, File file) {
        try {
            return processDescriptionIncludeFile(description, new FileInputStream(file), file);
        } catch (Exception e) {
            logIncludeFileError(file.getAbsolutePath(), description, e);
            return false;
        }
    }

    /**
     * Process a configuration input stream that describes a single connection.
     *
     * @param description The connection description being described by the input stream
     * @param stream      The input stream holding configuration information
     * @param parent      The file holding this description
     * @return True if the input stream is processed without error
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    public static boolean processDescriptionIncludeFile(StandardConnectionDescription description, InputStream stream, File parent)
            throws SAXException, IOException, ParserConfigurationException {
        // Create a builder factory and a builder, and get the document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = factory.newDocumentBuilder().parse(stream);
        // Process the children of this include as if they were in the connection description
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        processDescription(description, nodes, parent);
        return true;
    }

    /**
     * Process a configuration XML tag and see if it is a standard connection
     * description property.
     *
     * @param itemName    The tag name
     * @param item        The DOM node for the XML item
     * @param description The connection description being loaded
     * @return True if this tag was processes as a standard connection property
     */
    private static boolean handleStandardConnectionValue(String itemName, Node item, StandardConnectionDescription description) {
        boolean handled = true;
        if (itemName.equalsIgnoreCase("HOSTNAME")) {
            description.setHostname(getNodeAsText(item));
        } else if (itemName.equalsIgnoreCase("PORT")) {
            description.setPort(getNodeAsInt(item));
        } else if (itemName.equalsIgnoreCase("URLPATH")) {
            description.setUrlPath(getNodeAsText(item));
        } else if (itemName.equalsIgnoreCase("NAME")) {
            description.setName(getNodeAsText(item));
        } else {
            handled = false;
        }
        return handled;
    }

    /**
     * Process a configuration XML tag and see if it is a secure connection
     * description property.
     *
     * @param itemName    The tag name
     * @param item        The DOM node for the XML item
     * @param description The connection description being loaded
     * @return True if this tag was processes as a secure connection property
     */
    private static boolean handleSecureConnectionValue(String itemName, Node item, StandardConnectionDescription description, File parent) {
        boolean handled = true;
        // See if we are loading a secure connection
        SecureConnectionDescription secure = null;
        if (description instanceof SecureConnectionDescription) {
            secure = (SecureConnectionDescription) description;
        }
        // Now look at the tag name
        if (itemName.equalsIgnoreCase("KEYSTORE")) {
            String filename = getNodeAsText(item);
            if (filename != null) {
                // Mustafa: Check whether the filename string is already a global path, act accordingly!
                File keyFile;
                if (filename.startsWith("/"))
                    keyFile = new File(filename);
                else
                    keyFile = new File(parent.getParentFile(), filename);

                if (secure != null) {
                    secure.setKeyStore(keyFile.getAbsolutePath());
                } else {
                    LOGGER.error("KEYSTORE element in non secure connection.");
                }
            } else {
                logMissingAttributeWarning(itemName, "name", description);
            }
        } else if (itemName.equalsIgnoreCase("KEYPASS")) {
            if (secure != null) {
                secure.setKeyStorePassword(getNodeAsText(item));
            } else {
                LOGGER.error("KEYSTORE element in non secure connection.");
            }
        } else if (itemName.equalsIgnoreCase("TRUSTSTORE")) {
            String filename = getNodeAsText(item);
            if (filename != null) {
                // Mustafa: Check whether the filename string is already a global path, act accordingly!
                File keyFile;
                if (filename.startsWith("/"))
                    keyFile = new File(filename);
                else
                    keyFile = new File(parent.getParentFile(), filename);

                if (secure != null) {
                    secure.setTrustStore(keyFile.getAbsolutePath());
                } else {
                    LOGGER.error("KEYSTORE element in non secure connection.");
                }
            } else {
                logMissingAttributeWarning(itemName, "name", description);
            }
        } else if (itemName.equalsIgnoreCase("TRUSTPASS")) {
            if (secure != null) {
                secure.setTrustStorePassword(getNodeAsText(item));
            } else {
                LOGGER.error("KEYSTORE element in non secure connection.");
            }
        } else {
            handled = false;
        }
        // Generate warning if needed
        if (handled && (secure == null)) {
            logUnexpectedTagWarning(itemName, description);
        }
        return handled;
    }

    /**
     * Process a &lt;CodeType&gt; entry and add its contents to the
     * configuration coding scheme set.
     * <p/>
     * <pre>
     * XML: CodeType name="" classScheme=""
     *       Code code="" display="" codingScheme=""
     *       ...
     * </pre>
     *
     * @param connection The connection to add the codes to
     * @param codeType   The DOM element of the "CodeType" entry
     */
    private static void processCodingScheme(StandardConnectionDescription connection, Node codeType) {
        String typeName = getAttributeValue(codeType, "name");
        String classScheme = getAttributeValue(codeType, "classScheme");
        if (typeName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(codeType.getNodeName(), "name", connection);
        } else {
            // Get the coding scheme for this entry
            CodeSet scheme = connection.getCodeSet(typeName);
            if (scheme == null) {
                scheme = new CodeSet(typeName, classScheme);
                connection.addCodeSet(scheme);
            }
            // Now add the new entries
            NodeList codes = codeType.getChildNodes();
            for (int i = 0; i < codes.getLength(); i++) {
                Node codeNode = codes.item(i);
                if (codeNode.getNodeName().equalsIgnoreCase("CODE")) {
                    String code = getAttributeValue(codeNode, "code");
                    if (code == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(codeNode.getNodeName(), "code", connection);
                    } else {
                        // Grab the other attributes
                        String display = getAttributeValue(codeNode, "display");
                        if (display == null) {
                            if (classScheme != null) {
                                logMissingAttributeWarning(codeNode.getNodeName(), "display", connection);
                            }
                            display = code;
                        }
                        String codingScheme = getAttributeValue(codeNode, "codingScheme");
                        if (codingScheme == null) {
                            if (classScheme != null) {
                                logMissingAttributeWarning(codeNode.getNodeName(), "codingScheme", connection);
                            }
                            codingScheme = classScheme;
                        }
                        String ext = getAttributeValue(codeNode, "ext");
                        // Save away the scheme
                        scheme.addEntry(code, display, codingScheme, ext);
                    }
                } else if (codeNode instanceof Element) {
                    logUnexpectedTagWarning(codeNode.getNodeName(), connection);
                }
            }
        }
    }

    /**
     * Process an &lt;EnumMap&gt; entry and add its contents to the
     * configuration enum map set.
     * <p/>
     * <pre>
     * XML: EnumMap class=""
     *       Entry enum="" code=""
     *       ...
     * </pre>
     *
     * @param connection The connection to add the codes to
     * @param enumMap    The DOM element of the "EnumMap" entry
     */
    private static void processEnumMap(StandardConnectionDescription connection, Node enumMap) {
        String enumName = getAttributeValue(enumMap, "class");
        if (enumName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(enumMap.getNodeName(), "class", connection);
        } else {
            // Create an enum map for this entry
            EnumMap theMap;
            try {
                theMap = new EnumMap(enumName);
            } catch (ClassNotFoundException e) {
                logEnumMapError(enumName, connection, e);
                return;
            }
            connection.addEnumMap(theMap);
            // Now add the new entries
            NodeList entries = enumMap.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase("ENTRY")) {
                    String enumValue = getAttributeValue(entry, "enum");
                    if (enumValue == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), "enum", connection);
                    } else {
                        // Grab the other attributes
                        String codeValue = getAttributeValue(entry, "code");
                        if (codeValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), "code", connection);
                        } else {
                            // Save away the entry
                            theMap.addEntry(enumValue, codeValue);
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), connection);
                }
            }
        }
    }

    /**
     * Process an &lt;StringMap&gt; entry and add its contents to the
     * configuration string map set.
     * <p/>
     * <pre>
     * XML: StringMap name=""
     *       Entry string="" code=""
     *       ...
     * </pre>
     *
     * @param connection The connection to add the codes to
     * @param stringMap  The DOM element of the "StringMap" entry
     */
    private static void processStringMap(StandardConnectionDescription connection, Node stringMap) {
        String mapName = getAttributeValue(stringMap, "name");
        if (mapName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(stringMap.getNodeName(), "name", connection);
        } else {
            // Create a string map for this entry
            StringMap theMap = new StringMap(mapName);
            connection.addStringMap(theMap);
            // Now add the new entries
            NodeList entries = stringMap.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase("ENTRY")) {
                    String stringValue = getAttributeValue(entry, "string");
                    if (stringValue == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), "string", connection);
                    } else {
                        // Grab the other attributes
                        String codeValue = getAttributeValue(entry, "code");
                        if (codeValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), "code", connection);
                        } else {
                            // Save away the entry
                            theMap.addEntry(stringValue, codeValue);
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), connection);
                }
            }
        }
    }

    /**
     * Process a &lt;PropertySet&gt; entry and add its contents to the
     * connection configuration.
     * <p/>
     * <pre>
     * XML: PropertySet name=""
     *       Entry name="" value=""
     *       ...
     * </pre>
     *
     * @param connection  The connection to add the property set to
     * @param propertySet The DOM element of the "PropertySet" entry
     */
    private static void processPropertySet(StandardConnectionDescription connection, Node propertySet) {
        String setName = getAttributeValue(propertySet, "name");
        if (setName == null) {
            // No property set name, can't save values
            logMissingAttributeWarning(propertySet.getNodeName(), "name", connection);
        } else {
            // Create a property set for this entry
            PropertySet theSet = new PropertySet(setName);
            connection.addPropertySet(theSet);
            // Now add the new entries
            NodeList entries = propertySet.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase("ENTRY")) {
                    String stringValue = getAttributeValue(entry, "name");
                    if (stringValue == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), "name", connection);
                    } else {
                        // Grab the other attributes
                        String codeValue = getAttributeValue(entry, "value");
                        if (codeValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), "value", connection);
                        } else {
                            // Save away the entry
                            theSet.addValue(stringValue, codeValue);
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), connection);
                }
            }
        }
    }

    /**
     * Process an &lt;AssigningAuthority&gt; entry and add its contents to the
     * connection configuration.
     * <p/>
     * <pre>
     * XML: AssigningAuthority name="" type=""
     *       NamespaceId
     *       UniversalId
     *       UniversalIdType
     * </pre>
     *
     * @param connection The connection to add the assigning authority to
     * @param authority  The DOM element of the "AssigningAuthority" entry
     */
    private static void processIdentifier(StandardConnectionDescription connection, Node authority) {
        String setName = getAttributeValue(authority, "name");
        String type = getAttributeValue(authority, "type");

        if (setName == null) {
            // No authority name, can't save values
            logMissingAttributeWarning(authority.getNodeName(), "name", connection);
        } else {
            // Get the expected parts
            String namespaceId = null;
            String universalId = null;
            String universalIdType = null;
            NodeList entries = authority.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                String entryName = entry.getNodeName();
                if (entryName.equalsIgnoreCase("NAMESPACEID")) {
                    namespaceId = getNodeAsText(entry);
                } else if (entryName.equalsIgnoreCase("UNIVERSALID")) {
                    universalId = getNodeAsText(entry);
                } else if (entryName.equalsIgnoreCase("UNIVERSALIDTYPE")) {
                    universalIdType = getNodeAsText(entry);
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), connection);
                }
            }
            // Did we find enough parts?
            if ((namespaceId == null) && (universalId == null)) {
                logMissingElementWarning(authority.getNodeName(), "UniversalId", connection);
                return;
            }
            // We did, add the authority
            // Create a property set for this entry
            Identifier theAuthority = new Identifier(namespaceId, universalId, universalIdType);
            if (connection != null) {
                connection.addIdentifier(setName, type, theAuthority);
            }
        }
    }

    /**
     * Process an &lt;ObjectMap&gt; entry and add its contents to the
     * configuration object map set.
     * <p/>
     * <pre>
     * XML: ObjectMap name=""
     *       Entry type="" node="" connect="" system=""
     *       ...
     * </pre>
     * <p/>
     * The type of the entry takes values of operation (the request method name), node(the response node from
     * which to obtain the data) and field (the field of the clinical object). The type and node are optional,
     * while connect and system must exist.
     *
     * @param connection The connection to add the codes to
     * @param objectMap  The DOM element of the "ObjectMap" entry
     */
    private static void processObjectMap(StandardConnectionDescription connection, Node objectMap) {
        String mapName = getAttributeValue(objectMap, "name");
        if (mapName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(objectMap.getNodeName(), "name", connection);
        } else {
            // Create a object map for this entry
            ObjectMap theMap = new ObjectMap(mapName);
            connection.addObjectMap(theMap);
            // Now add the new entries
            NodeList entries = objectMap.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase("ENTRY")) {
                    String connectValue = getAttributeValue(entry, "connect");
                    if (connectValue == null) {
                        // No connect, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), "connect", connection);
                    } else {
                        // Grab the system attributes
                        String systemValue = getAttributeValue(entry, "system");
                        if (systemValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), "system", connection);
                        } else {
                            // Grab the type attributes
                            String typeValue = getAttributeValue(entry, "type");
                            //Grab the node attribute
                            String nodeValue = getAttributeValue(entry, "node");
                            if (nodeValue == null && (typeValue != null && typeValue.equals("field"))) {
                                //when type="field", nodeValue must exist.
                                logMissingAttributeWarning(entry.getNodeName(), "node", connection);
                            } else {
                                // Save away the entry
                                theMap.addEntry(typeValue, connectValue, systemValue, nodeValue);
                            }
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), connection);
                }
            }
        }
    }

    /**
     * Process an &lt;ObjectList&gt; entry and add its contents to the
     * connection configuration. If the attribute enabled="false", then this ObjectList
     * will not be processed.
     * <p/>
     * <pre>
     * XML: ObjectList name="" enabled=""
     *         Object
     *             Field name="" value="" type="" format=""
     *             ...
     *         Object
     *            Field name="" value=""  type="" format=""
     *             ...
     * </pre>
     * <p/>
     * Each ObjectList contains one or multiple ObjectEntry which includes
     * a list of entry of name, value pair
     *
     * @param connection The connection to add the codes to
     * @param objectList The DOM element of the "ObjectList" entry
     */
    private static void processObjectList(StandardConnectionDescription connection, Node objectList) {
        String enabled = getAttributeValue(objectList, "enabled");
        if (!Boolean.parseBoolean(enabled)) {
            return; //if enabled="false", do not process
        }
        String name = getAttributeValue(objectList, "name");
        if (name == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(objectList.getNodeName(), "name", connection);
        } else {
            // Create a object list for this entry
            ObjectList theList = new ObjectList(name);
            connection.addObjectList(theList);
            // Now add the new entries
            NodeList objectEntries = objectList.getChildNodes();
            for (int i = 0; i < objectEntries.getLength(); i++) {
                Node entry = objectEntries.item(i);
                if (entry.getNodeName().equalsIgnoreCase("OBJECT")) {
                    ObjectEntry theObject = new ObjectEntry();
                    theList.addListEntry(theObject);
                    // Now add the new fields
                    processFields(connection, entry, theObject);
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), connection);
                }
            } //end for
        }
    }

    /**
     * Process an &lt;Object&gt; entry and add its contents to the
     * connection configuration.  If the attribute enabled="false", then this ObjectList
     * will not be processed.
     * <p/>
     * <p/>
     * <pre>
     * XML:
     *         Object name="" enabled=""
     *             Field name="" value="" type="" format=""
     *             ...
     *         Object name=""
     *            Field name="" value=""  type="" format=""
     *             ...
     * </pre>
     * <p/>
     * Each ObjectList contains one or multiple ObjectEntry which includes
     * a list of entry of name, value pair
     *
     * @param connection The connection to add the codes to
     * @param object     The DOM element of the "Object" entry
     */
    private static void processObjectEntry(StandardConnectionDescription connection, Node object) {
        String enabled = getAttributeValue(object, "enabled");
        if (!Boolean.parseBoolean(enabled)) {
            return; //if enabled="false", do not process
        }
        String name = getAttributeValue(object, "name");
        if (name == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(object.getNodeName(), "name", connection);
        } else {
            // Create an object entry
            ObjectEntry theObject = new ObjectEntry(name);
            connection.addObject(theObject);
            // Now add the object fields
            processFields(connection, object, theObject);
        }
    }

    /**
     * Processes the field elements in an Object element, and add the contents to the object entry.
     * <pre>
     * XML:
     *         Object name=""
     *             Field name="" value="" type="" format=""
     *             ...
     * </pre>
     *
     * @param connection The connection to add the codes to
     * @param node       The DOM element of the "Object" entry
     * @param theObject  The ObjectEntry where to store all the processed fields.
     */
    private static void processFields(StandardConnectionDescription connection, Node node, ObjectEntry theObject) {
        // Now add the object fields
        NodeList fields = node.getChildNodes();
        for (int j = 0; j < fields.getLength(); j++) {
            Node field = fields.item(j);
            if (field.getNodeName().equalsIgnoreCase("FIELD")) {
                String fieldName = getAttributeValue(field, "name");
                if (fieldName == null) {
                    // No name, nothing to save
                    logMissingAttributeWarning(field.getNodeName(), "name", connection);
                } else {
                    // Grab the other attributes
                    String fieldValue = getAttributeValue(field, "value");
                    if (fieldValue == null) {
                        logMissingAttributeWarning(field.getNodeName(), "value", connection);
                    } else {
                        String fieldType = getAttributeValue(field, "type");
                        String fieldFormat = getAttributeValue(field, "format");
                        if (fieldType != null && fieldType.equalsIgnoreCase("date") && fieldFormat == null) {
                            //For date, need to have a valid date format
                            logMissingAttributeWarning(field.getNodeName(), "format", connection);
                        } else {
                            // Save away the field
                            ObjectEntry.Field fieldData;
                            fieldData = new ObjectEntry.Field(fieldName, fieldValue, fieldType, fieldFormat);
                            theObject.addField(fieldName, fieldData);
                        }
                    }
                }
            } else if (field instanceof Element) {
                logUnexpectedTagWarning(field.getNodeName(), connection);
            }
        } //end for
    }

    /**
     * Generate a log warning message that an unexpected tag was found in the connection
     * description.
     *
     * @param tagName     The name of the unexpected XML tag
     * @param description The connection description being loaded
     */
    private static void logUnexpectedTagWarning(String tagName, IConnectionDescription description) {
        String name = null;
        if (description != null) {
            name = description.getName();
        }
        if (name != null) {
            name = name.trim();
        }
        if ((name == null) || (name.equals(""))) {
            LOGGER.warn("Unexpected '" + tagName + "' element in connection description");
        } else {
            LOGGER.warn("Unexpected '" + tagName + "' element in connection description \"" + name + "\"");
        }
    }

    /**
     * Generate a log warning for a missing required attribute in a connection description.
     *
     * @param tagName     The name of the element requiring the attribute
     * @param attribute   The name of the missing attribute
     * @param description The connection description being loaded
     */
    private static void logMissingAttributeWarning(String tagName, String attribute, IConnectionDescription description) {
        String name = null;
        if (description != null) {
            name = description.getName();
        }
        if (name != null) {
            name = name.trim();
        }
        if ((name == null) || (name.equals(""))) {
            LOGGER.warn("Missing attribute '" + attribute + "' in '" + tagName + "' element in connection description");
        } else {
            LOGGER.warn("Missing attribute '" + attribute + "' in '" + tagName + "' element in connection description \"" + name + "\"");
        }
    }

    /**
     * Generate a log warning for a missing required component in a connection description.
     *
     * @param tagName     The name of the element requiring the attribute
     * @param element     The name of the missing component
     * @param description The connection description being loaded
     */
    private static void logMissingElementWarning(String tagName, String element, IConnectionDescription description) {
        String name = null;
        if (description != null) {
            name = description.getName();
        }
        if (name != null) {
            name = name.trim();
        }
        if ((name == null) || (name.equals(""))) {
            LOGGER.warn("Missing subelement '" + element + "' in '" + tagName + "' element in connection description");
        } else {
            LOGGER.warn("Missing subelement '" + element + "' in '" + tagName + "' element in connection description \"" + name + "\"");
        }
    }

    /**
     * Generate a log message when there is an error processing an included configuration file.
     *
     * @param filename    The name of the included configuration file
     * @param description The connection description being loaded
     * @param e           The exception describing the error
     */
    private static void logIncludeFileError(String filename, IConnectionDescription description, Exception e) {
        if ((filename == null) || (filename.equals(""))) {
            LOGGER.error("Error reading file included in connection description", e);
        } else if (description == null) {
            LOGGER.error("Error reading connection description include file: \"" + filename + "\"", e);
        } else {
            String name = description.getName();
            if (name != null) {
                name = name.trim();
            }
            LOGGER.warn("Error reading connection description \"" + name + "\" include file: \"" + filename + "\"", e);
        }
    }

    /**
     * Generate a log message when there is an error processing an enum map.
     *
     * @param enumClassName The name of the enum class the map is for
     * @param description   The connection description being loaded
     * @param e             The exception describing the error
     */
    private static void logEnumMapError(String enumClassName, IConnectionDescription description, Exception e) {
        if ((enumClassName == null) || (enumClassName.equals(""))) {
            LOGGER.error("Error reading file included in connection description", e);
        } else {
            String name = description.getName();
            if (name != null) {
                name = name.trim();
            }
            LOGGER.warn("Error reading connection description \"" + name + "\" enum map \"" + enumClassName + "\"", e);
        }
    }

    /**
     * Get an atribute value
     *
     * @param node The XML DOM node holding the attribute
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    private static String getAttributeValue(Node node, String name) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        Node attribute = attributes.getNamedItem(name);
        if (attribute == null) {
            return null;
        }
        return attribute.getNodeValue();
    }

    /**
     * Get the text included within an XML DOM element
     *
     * @param node The XML DOM node holding the text
     * @return The text
     */
    private static String getNodeAsText(Node node) {
        if (!node.hasChildNodes()) {
            return null;
        }
        Text nodeTextContents = (Text) node.getFirstChild();
        return nodeTextContents.getData();
    }

    /**
     * Get the text included within an XML DOM element as an integer
     *
     * @param node The XML DOM node holding the text
     * @return The text converted to an integer.
     */
    private static int getNodeAsInt(Node node) {
        Text nodeTextContents = (Text) node.getFirstChild();
        String value = nodeTextContents.getData();
        return Integer.parseInt(value);
    }
}
