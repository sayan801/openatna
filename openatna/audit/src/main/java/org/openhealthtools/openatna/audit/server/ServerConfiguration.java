/*
 * Copyright (c) 2009 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.audit.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.ServiceConfiguration;
import org.openhealthtools.openatna.net.ConnectionFactory;
import org.openhealthtools.openatna.net.IConnectionDescription;

/**
 * Loads XML actor and connection files.
 * This throws RuntimeExceptions if something goes pear shaped - no point in carrying
 * on if errors occur here.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 10:24:00 AM
 * @date $Date:$ modified by $Author:$
 */

public class ServerConfiguration {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.config.ServerConfiguration");

    private Set<AtnaServer> servers = new HashSet<AtnaServer>();
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
        String actorsFile = new File(actorDir).getAbsolutePath();
        actorsFile = actorsFile.replace(File.separator + "." + File.separator, File.separator);
        File actors = new File(actorsFile);
        if (!actors.exists()) {
            log.warn("Could not find actors dir:" + actors.getAbsolutePath());
            return null;
        }
        File configFile = new File(actors, actorFile);
        if (!configFile.exists()) {
            log.warn("Could not find actors file:" + configFile.getAbsolutePath());
            return null;
        }
        return configFile;
    }

    public boolean load() {
        return loadActors(getActorsFile());
    }

    public boolean loadActors(File configFile) {
        boolean okay = true;
        Document configuration = null;
        try {
            configuration = createDocument(configFile);
        } catch (Exception e) {
            throw new RuntimeException("Error loading config file:" +
                    configFile.getAbsolutePath(), e);
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
                    if (!processConnectionFile((Element) element, configFile)) okay = false;
                } else if (name.equalsIgnoreCase("SECURECONNECTION") || name.equalsIgnoreCase("STANDARDCONNECTION")) {
                    // An included connection, load it
                    if (!ConnectionFactory.loadConnectionDescriptionsFromXmlNode(element, configFile)) {
                        throw new RuntimeException("Error loading configuration file \"" + configFile.getAbsolutePath() +
                                "\" in configFile:" + configFile.getAbsolutePath());
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
                        if (!processActorFile((Element) element, configFile)) okay = false;
                    } else if (name.equalsIgnoreCase("ACTOR")) {
                        // An IHE actor definition
                        if (!processActorDefinition((Element) element)) okay = false;
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
            log.warn("Unknown actor type or name. Expecting name=arr and type=SecureNode but got name=" +
                    name + " type=" + type);
            okay = false;
        }
        return okay;
    }

    private boolean processArr(Element parent) {
        IConnectionDescription tcp = null;
        IConnectionDescription udp = null;
        ServiceConfiguration sc = new ServiceConfiguration();

        NodeList children = parent.getChildNodes();
        int threads = 5;
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
                            log.warn("Could not parse number of execution threads. Using default");
                        }
                        if (threads < 1) {
                            threads = 5;
                        }
                    }

                } /*else if (el.getTagName().equalsIgnoreCase("SERVICECONFIG")) {
                    NodeList childers = el.getChildNodes();
                    for (int j = 0; j < childers.getLength(); j++) {
                        Node node = childers.item(j);
                        if (node instanceof Element) {
                            Element child = (Element) node;
                            if (child.getTagName().equalsIgnoreCase("PERSISTENCEPOLICIES")) {
                                try {
                                    PersistencePolicies pp = PersistencePoliciesIO.read(child);
                                    sc.setPersistencePolicies(pp);
                                } catch (IOException e) {
                                    log.warn("exception thrown trying to read persistence policies", e);
                                }
                            } else if (child.getTagName().equalsIgnoreCase("LOGMESSAGE")) {
                                String logType = child.getTextContent().trim();
                                if (logType == null || logType.length() == 0) {
                                    throw new RuntimeException("No log message implementation specified. This is required.");
                                }
                                try {
                                    Class<? extends LogMessage> logClass = (Class<? extends LogMessage>) Class.forName(logType, true, getClass().getClassLoader());
                                    sc.setLogMessageClass(logClass);
                                } catch (Exception e) {
                                    throw new RuntimeException("Unable to load log message implementation=|" + logType + "|", e);
                                }

                            } else if (child.getTagName().equalsIgnoreCase("DAOFACTORY")) {
                                String dao = child.getTextContent().trim();
                                if (dao != null && dao.length() > 0) {
                                    try {
                                        Class<? extends DaoFactory> cls = (Class<? extends DaoFactory>) Class.forName(dao, true, getClass().getClassLoader());
                                        DaoFactory f = cls.newInstance();
                                        sc.setDaoFactory(f);
                                    } catch (Exception e) {
                                        log.warn("Could not load DaoFactory implementation " + dao);
                                    }
                                }
                            } else if (child.getTagName().equalsIgnoreCase("CODESURL")) {
                                String url = child.getTextContent().trim();
                                if (url != null && url.length() > 0) {
                                    try {

                                        sc.addCodeUrl(url);
                                    } catch (Exception e) {
                                        log.warn("Could not load Codes URL " + url);
                                    }
                                }
                            } else if (child.getTagName().equalsIgnoreCase("PROCESSORCHAIN")) {
                                String val = child.getAttribute("validate");
                                if (val != null && val.length() > 0) {
                                    boolean v = Boolean.valueOf(val);
                                    sc.setValidationProcessor(v);
                                }
                                NodeList ps = child.getChildNodes();
                                for (int k = 0; k < childers.getLength(); k++) {
                                    Node p = ps.item(k);
                                    if (p instanceof Element) {
                                        Element pro = (Element) p;
                                        if (pro.getTagName().equalsIgnoreCase("PROCESSOR")) {
                                            String procls = pro.getTextContent().trim();
                                            String ph = pro.getAttribute("phase");
                                            ProcessorChain.PHASE phase = ProcessorChain.PHASE.POST_VERIFY;
                                            if (ph != null) {
                                                ProcessorChain.PHASE phase1 = ProcessorChain.PHASE.valueOf(ph);
                                                if (phase1 != null) {
                                                    phase = phase1;
                                                }
                                            }
                                            try {
                                                Class<? extends AtnaProcessor> cls = (Class<? extends AtnaProcessor>) Class.forName(procls, true, getClass().getClassLoader());
                                                AtnaProcessor proc = cls.newInstance();
                                                sc.addProcessor(proc, phase);
                                                log.info("added application processor:" + proc);
                                            } catch (Exception e) {
                                                log.warn("Could not load Processor implementation " + procls);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }  */
            }
        }
        AtnaServer server = null;
        if (tcp != null && udp != null) {
            server = new AtnaServer(tcp, udp, threads);
            servers.add(server);
            return true;
        } else {
            log.warn("No connections defined for server. This ARR will be not able to receive Syslog Messages.\n" +
                    "The service will shut down unless you it is being run from inside a separate execution thread.");
            return false;
        }

    }

    private boolean processConnectionFile(Element element, File configFile) {
        boolean okay = false;
        // Get out the file name
        String filename = element.getAttribute("file");
        if (filename == null) filename = element.getAttribute("name");
        if (filename == null) filename = element.getTextContent().trim();
        if (filename != null) {
            // Got the connection file name, load it
            File includeFile = new File(configFile.getParentFile(), filename);
            if (ConnectionFactory.loadConnectionDescriptionsFromFile(includeFile)) {
                okay = true;
            } else {
                throw new RuntimeException("Error loading connection file \"" +
                        filename + "\" from config file:" + configFile.getAbsolutePath());
            }
        } else {
            // No connection file name given
            throw new RuntimeException("No connection file specified in config file:" + configFile.getAbsolutePath());
        }
        // Done
        return okay;
    }

    private boolean processActorFile(Element element, File configFile) {
        boolean okay = false;
        // Get out the file name
        String filename = element.getAttribute("file");
        if (filename == null) filename = element.getAttribute("name");
        if (filename == null) filename = element.getTextContent().trim();
        if (filename != null) {
            // Got the actor file name, load it
            File includeFile = new File(configFile.getParentFile(), filename);
            if (loadActors(includeFile)) {
                okay = true;
            } else {
                throw new RuntimeException("Error loading actor file \"" +
                        filename + "\" in config file:" + configFile.getAbsolutePath());
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
