package org.openhealthtools.openatna.audit.server;

import org.openhealthtools.openatna.net.ConnectionFactory;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Loads XML actor and connection files.
 * This throws RuntimeExceptions if something goes pear shaped - no point in carrying on if errors occur here.
 *
 * @author Andrew Harrison
 */
public class ServerConfiguration {

    private final Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    private final Set<AtnaServer> servers = new HashSet<>();
    private String actorDir;
    private String actorFile;

    public String getActorDir() {
        return actorDir;
    }

    public void setActorDir(String actorDir) {
        this.actorDir = actorDir;
    }

    public String getActorFile() {
        return actorFile;
    }

    public void setActorFile(String actorFile) {
        this.actorFile = actorFile;
    }

    public List<AtnaServer> getServers() {
        return new ArrayList(servers);
    }

    private File getActorsFile() {

        if (actorDir == null || actorFile == null) {
            throw new RuntimeException("ERROR. Please set both actorDir and actorFile.");
        }
        String actorsFile = new File(actorDir).getAbsolutePath();
        actorsFile = actorsFile.replace(File.separator + "." + File.separator, File.separator);
        File actors = new File(actorsFile);
        if (!actors.exists()) {
            logger.warn("Could not find actors dir:" + actors.getAbsolutePath());
            return null;
        }
        File configFile = new File(actors, actorFile);
        if (!configFile.exists()) {
            logger.warn("Could not find actors file:" + configFile.getAbsolutePath());
            return null;
        }
        return configFile;
    }

    public boolean load() {
        return loadActors(getActorsFile());
    }

    public boolean loadActors(File configFile) {

        boolean okay = true;
        Document configuration;
        try {
            configuration = createDocument(configFile);
        } catch (Exception e) {
            throw new RuntimeException("Error loading config file:"
                    + configFile.getAbsolutePath(), e);
        }
        // Get the list of XML elements in the configuration file
        NodeList configurationElements = configuration.getDocumentElement().getChildNodes();
        // Load all the connection definitions first
        for (int elementIndex = 0; elementIndex < configurationElements.getLength(); elementIndex++) {

            Node element = configurationElements.item(elementIndex);

            if (element instanceof Element) {
                // See what type of element it is
                String name = element.getNodeName();
                if (name.equalsIgnoreCase("CONNECTIONFILE")) {
                    // An included connection file, load it
                    if (!processConnectionFile((Element) element, configFile)) {
                        okay = false;
                    }
                } else if (name.equalsIgnoreCase("SECURECONNECTION")
                        || name.equalsIgnoreCase("STANDARDCONNECTION")) {
                    // An included connection, load it
                    if (!ConnectionFactory.loadConnectionDescriptionsFromXmlNode(element, configFile)) {
                        throw new RuntimeException("Error loading configuration file \""
                                + configFile.getAbsolutePath()
                                + "\" in configFile:" + configFile.getAbsolutePath());
                    }
                }
            }
        }
        // If all the connection files loaded okay, define the various actors
        if (okay) {
            for (int elementIndex = 0; elementIndex < configurationElements.getLength(); elementIndex++) {

                Node element = configurationElements.item(elementIndex);
                if (element instanceof Element) {
                    // See what type of element it is
                    String name = element.getNodeName();
                    if (name.equalsIgnoreCase("ACTORFILE")) {
                        if (!processActorFile((Element) element, configFile)) {
                            okay = false;
                        }
                    } else if (name.equalsIgnoreCase("ACTOR")) {
                        // An IHE actor definition
                        if (!processActorDefinition((Element) element)) {
                            okay = false;
                        }
                    }
                }
            }
        }
        return okay;
    }

    private boolean processActorDefinition(Element parent) {

        boolean okay;
        String type = parent.getAttribute("type");
        String name = parent.getAttribute("name");
        if (name == null || type == null) {
            throw new RuntimeException("No name or type for actor defined!");
        }
        if ("SECURENODE".equalsIgnoreCase(type) && "ARR".equalsIgnoreCase(name)) {
            okay = processArr(parent);
        } else {
            logger.warn("Unknown actor type or name. Expecting name=arr and type=SecureNode but got name='{}' and type='{}'", name, type);
            okay = false;
        }
        return okay;
    }

    private boolean processArr(Element parent) {

        IConnectionDescription tcp = null;
        IConnectionDescription udp = null;

        NodeList children = parent.getChildNodes();
        int threads = 5;
        boolean nio = false;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                Element el = (Element) n;
                if (el.getTagName().equalsIgnoreCase("TCP")) {
                    String conn = el.getAttribute("connection");
                    if (conn == null) {
                        throw new RuntimeException("No connection defined for Audit Record Repository");
                    }
                    tcp = ConnectionFactory.getConnectionDescription(conn);
                    if (tcp == null) {
                        throw new RuntimeException("No connection defined for Audit Record Repository");
                    }

                } else if (el.getTagName().equalsIgnoreCase("UDP")) {
                    String conn = el.getAttribute("connection");
                    if (conn == null) {
                        throw new RuntimeException("No connection defined for Audit Record Repository");
                    }
                    udp = ConnectionFactory.getConnectionDescription(conn);
                    if (udp == null) {
                        throw new RuntimeException("No connection defined for Audit Record Repository");
                    }

                } else if (el.getTagName().equalsIgnoreCase("EXECUTIONTHREADS")) {
                    String t = el.getTextContent().trim();
                    if (t.length() > 0) {
                        try {
                            threads = Integer.parseInt(t);
                        } catch (NumberFormatException e) {
                            logger.warn("Could not parse number of execution threads. Using default");
                        }
                        if (threads < 1) {
                            threads = 5;
                        }
                    }
                } else if (el.getTagName().equalsIgnoreCase("NIO")) {
                    String t = el.getTextContent().trim();
                    if (t.length() > 0) {
                        if (t.equalsIgnoreCase("true") || t.equalsIgnoreCase("1") || t.equalsIgnoreCase("yes")) {
                            nio = true;
                        }
                    }
                }
            }
        }
        if (tcp != null && udp != null) {
            AtnaServer server = new AtnaServer(tcp, udp, threads, nio);
            servers.add(server);
            return true;
        } else {
            logger.warn("No connections defined for server. This ARR will be not able to receive Syslog Messages.\n"
                    + "The service will shut down unless it is being run from inside a separate execution thread.");
            return false;
        }

    }

    private boolean processConnectionFile(Element element, File configFile) {

        boolean okay;
        // Get out the file name
        String filename = element.getAttribute("file");
        if (filename == null) {
            filename = element.getAttribute("name");
        }
        if (filename == null) {
            filename = element.getTextContent().trim();
        }
        if (filename != null) {
            // Got the connection file name, load it
            File includeFile = new File(configFile.getParentFile(), filename);
            if (ConnectionFactory.loadConnectionDescriptionsFromFile(includeFile)) {
                okay = true;
            } else {
                throw new RuntimeException("Error loading connection file \""
                        + filename + "\" from config file:" + configFile.getAbsolutePath());
            }
        } else {
            // No connection file name given
            throw new RuntimeException("No connection file specified in config file:" + configFile.getAbsolutePath());
        }
        // Done
        return okay;
    }

    private boolean processActorFile(Element element, File configFile) {

        boolean okay;
        // Get out the file name
        String filename = element.getAttribute("file");
        if (filename == null) {
            filename = element.getAttribute("name");
        }
        if (filename == null) {
            filename = element.getTextContent().trim();
        }
        if (filename != null) {
            // Got the actor file name, load it
            File includeFile = new File(configFile.getParentFile(), filename);
            if (loadActors(includeFile)) {
                okay = true;
            } else {
                throw new RuntimeException("Error loading actor file \""
                        + filename + "\" in config file:" + configFile.getAbsolutePath());
            }
        } else {
            // No connection file name given
            throw new RuntimeException("No connection file specified in config file:" + configFile.getAbsolutePath());
        }
        // Done
        return okay;
    }

    private Document createDocument(File configFile) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        return factory.newDocumentBuilder().parse(configFile);
    }
}
