package org.openhealthtools.openatna.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * A static helper class for creating connections. <p/>
 * <p/>
 * The connection factory is a static class that allows for generic connections to be made based on the name of
 * the configuration for that connection.<p/>
 * <p/>
 * There are two classes of connections that can be made, HTTP connections and socket connections.
 * Use the getConnection function to get socket connections and use the getHttpConnection function to get
 * a configured apache org HttpClient.
 * Be sure to only use relative URLs in your methods that you call on the HttpClient because it will be preconfigured
 * with the host and port.  <p/>
 * <p/>
 * Note that this class con not be relied on to remain thread safe.
 * One possible solution is to force instantiation of the class, the other is to synchronize the get function.
 * I am waiting to decide until I start working on the server connection version.
 * (Not required for Connectathon, but will be useful for if we ever want to be a PIX server, etc.) <p/>
 *
 * @author Josh Flachsbart
 */
public class ConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    /* Cached set of known descriptions */
    private static HashMap<String, IConnectionDescription> allDescriptions = null;

    private ConnectionFactory() {
    }

    /**
     * Looks up the description of a connection by its name. <p />
     * <p/>
     * This function looks up all the connections from a configuration
     * file and returns the one named.  If the name is not found it
     * throws an illegal argument exception. <p />
     *
     * @param connectionName Name of the connection to return.
     * @return The description of the named connection.
     * @throws IllegalArgumentException If the name is not found in the connection configurations.
     */
    public static synchronized IConnectionDescription getConnectionDescription(String connectionName) {

        IConnectionDescription foundDescription = null;
        if (allDescriptions != null && connectionName != null) {

            String name = connectionName.toLowerCase().trim();
            foundDescription = allDescriptions.get(name);
        }
        return foundDescription;
    }

    /**
     * Clear our all of the currently loaded connection descriptions.
     * No connection descriptions will exist until some are loaded.
     */
    public static synchronized void clearConnectionDescriptions() {
        allDescriptions = null;
    }

    /**
     * Load set of connection descriptions from an XML file.
     *
     * @param filename The name of the XML file
     * @return True if the file was loaded successfully
     */
    public static boolean loadConnectionDescriptionsFromFile(String filename) {
        if (filename == null) {
            return false;
        }
        return loadConnectionDescriptionsFromFile(new File(filename));
    }

    /**
     * Load set of connection descriptions from an XML file.
     *
     * @param file The XML file
     * @return True if the file was loaded successfully
     */
    public static synchronized boolean loadConnectionDescriptionsFromFile(File file) {
        // If we don't really have a filename, return immediately
        if (file == null) {
            return false;
        }
        // Load the connection descriptions from the XML file
        List<IConnectionDescription> newDescriptions = null;
        try {
            newDescriptions = DescriptionLoader.loadConnectionDescriptions(file);
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.error("Internal XML parser error reading connection description file.", e);
            return false;
        } catch (IOException e) {
            LOGGER.error("File problem reading connection description file.", e);
            return false;
        }
        // Add each description to the cached set
        for (IConnectionDescription description : newDescriptions) {
            String name = description.getName();
            if (name == null) {
                LOGGER.warn("Connection description with no name referenced in '" + file.getAbsolutePath() + "'");
            } else {
                if (allDescriptions == null) {
                    allDescriptions = new HashMap<>();
                }
                // Convert to lowercase to make lookup case insensitive
                allDescriptions.put(name.toLowerCase().trim(), description);
            }
        }
        // Done
        return true;
    }

    /**
     * Load a single connection description from an XML node.
     *
     * @param node The XML node holding the description
     * @param file The XML file that generated the node
     * @return True if the node was processes successfully
     */
    public static synchronized boolean loadConnectionDescriptionsFromXmlNode(Node node, File file) {
        // If we don't really have a filename, return immediately
        if (file == null) {
            return false;
        }
        // Load the connection description from the XML node
        IConnectionDescription newDescription = DescriptionLoader.processDescriptionNode(node, file);
        // Add the description to the cached set
        if (newDescription != null) {
            String name = newDescription.getName();
            if (name == null) {
                LOGGER.warn("Connection description with no name referenced in '" + file.getAbsolutePath() + "'");
            } else {
                if (allDescriptions == null) {
                    allDescriptions = new HashMap<>();
                }
                // Convert to lowercase to make lookup case insensitive
                allDescriptions.put(name.toLowerCase().trim(), newDescription);
            }
        }
        // Done
        return true;
    }

    /**
     * The standard way of making a Socket connection.
     * <p>
     * Calling this will generate a socket connection and initialize
     * it according to the connection description that you hand it.
     * You should get the connection description from the connection
     * factory as well using the getConnectionDescription function.
     * This will get the description from the appropriate
     * configuration files and allow the installers to configure the
     * program accordingly without needing to change the program.
     * </p>
     * <p>
     * Here is an example of using the Socket connection:
     * <pre>
     *     IConnectionDescription cd = ConnectionFactory.getConnectionDescription("Lab XDS Server");
     *     IConnection connection = ConnectionFactory.getConnection(cd);
     *     OutputStream os = connection.getOutputStream();
     *     ... write to the stream here ...
     *     os.close();
     *     connection.close();
     *     </pre>
     * </p>
     *
     * @param connectionDescription The description of the connection that needs to be made.
     * @return The fully initalized connection.
     */
    public static IConnection getConnection(IConnectionDescription connectionDescription) {

        IConnection connection;

        if (connectionDescription.isSecure()) {
            connection = new SecureConnection(connectionDescription);
        } else {
            connection = new StandardConnection(connectionDescription);
        }

        connection.connect();

        return connection;
    }

    /**
     * The standard way of making a Mail connection.
     * <p>
     * Calling this will generate a Mail connection and initialize
     * it according to the connection description that you hand it.
     * You should get the connection description from the connection
     * factory as well using the getConnectionDescription function.
     * This will get the description from the appropriate
     * configuration files and allow the installers to configure the
     * program accordingly without needing to change the program.
     * </p>
     * <p>
     * Here is an example of using the SMTP portion of a mail connection:
     * <pre>
     *     IConnectionDescription cd = ConnectionFactory.getConnectionDescription("Lab Mail Server");
     *     MailConnection mailConnection = ConnectionFactory.getMailConnection(connection);
     *     MimeMessage mailMessage = mailConnection.getNewMessage();
     *     // Build message...
     *     mailConnection.sendMessage(mailMessage);
     *     </pre>
     * </p>
     *
     * @param connectionDescription The description of the connection that needs to be made.
     * @return The fully initalized connection.
     */
    public static MailConnection getMailConnection(IConnectionDescription connectionDescription) {

        // TODO deal with different kinds of mail connections...
        return new MailConnection(connectionDescription);
    }

    public static IServerConnection getServerConnection(IConnectionDescription connectionDescription) {

        IServerConnection serverConnection;

        if (connectionDescription.isSecure()) {
            serverConnection = new SecureServerConnection(connectionDescription);
        } else {
            serverConnection = new StandardServerConnection(connectionDescription);
        }

        serverConnection.connect();

        return serverConnection;
    }

    public static IUdpServerConnection getUdpServerConnection(IConnectionDescription connectionDescription) {
        IUdpServerConnection udpServerConnection = new UdpServerConnection(connectionDescription);

        udpServerConnection.connect();

        return udpServerConnection;
    }

    /**
     * The standard way of making an HTTP connection.
     * <p>
     * Calling this will generate an HttpClient http connection and
     * initialize it according to the connection description that
     * you hand it.  You should get the connection description from
     * the connection factory as well using the getConnectionDescription
     * function.  This will get the description from the appropriate
     * configuration files and allow the installers to configure the
     * program accordingly without needing to change the program.
     * </p>
     * <p>
     * Here is an example of using the HTTP connection:
     *     <pre>
     *     IConnectionDescription cd = ConnectionFactory.getConnectionDescription("Lab XDS Server");
     *     HttpClient client = ConnectionFactory.getHttpConnection(cd);
     *     PostMethod post = new PostMethod("/repository.asp");
     *     ... add things to post method as needed, including data ...
     *     client.executeMethod(post);
     *     post.releaseConnection();
     *     </pre>
     * </p>
     * <p>
     * Note that it is critical that the method only use a relative URL, and not a fully
     * qualified URL.  Fully qualifying the URL will make it use a new connection, not
     * the connection that the factory has initialized in the client.
     * </p>
     * @param connectionDescription The description of the connection that needs to be made.
     * @return The fully initalized connection.
     */
    /*public static HttpClient getHttpConnection(IConnectionDescription connectionDescription) {
         HttpClient connection = new HttpClient();

         if (connectionDescription.isSecure())
             connection.getHostConfiguration().setHost(
                     connectionDescription.getHostname(),
                     connectionDescription.getPort(),
                     getProtocol(connectionDescription));
         else
             connection.getHostConfiguration().setHost(
                     connectionDescription.getHostname(),
                     connectionDescription.getPort());

         return connection;
     }*/

    /** Used to generate an internal protocol for the apache http connection.
     *
     * We could make this the default protocol, but I'm not sure if that would
     * mess other things up in other parts of the code, if other people decide
     * to use this stuff as well.  */
    /*private static Protocol getProtocol(IConnectionDescription cd) {
         SecureConnectionDescription scd;
         if (cd instanceof SecureConnectionDescription)
             scd = (SecureConnectionDescription) cd;
         else
             return null;

         // Get the data:
         String protocol = "https";

         // Put it all together.
         SecureSocketFactory factory = new SecureSocketFactory(scd);
         return new Protocol(protocol, factory, scd.getPort());
     }*/
}