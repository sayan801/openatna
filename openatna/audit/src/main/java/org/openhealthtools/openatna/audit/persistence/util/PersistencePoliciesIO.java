package org.openhealthtools.openatna.audit.persistence.util;

import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility that reads and writes PersistencePolicies to XML
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class PersistencePoliciesIO {

    public static final String POLICIES = "PersistencePolicies";
    public static final String ALLOW_NEW_CODES = "allowNewCodes";
    public static final String ALLOW_NEW_NETWORK_POINTS = "allowNewNetworkAccessPoints";
    public static final String ALLOW_NEW_PARTICIPANTS = "allowNewParticipants";
    public static final String ALLOW_NEW_SOURCES = "allowNewSources";
    public static final String ALLOW_NEW_OBJECTS = "allowNewObjects";
    public static final String ALLOW_UNKNOWN_DETAIL_TYPES = "allowUnknownDetailTypes";
    public static final String ALLOW_MODIFY_MESSAGES = "allowModifyMessages";
    public static final String ERROR_ON_DUPLICATE_INSERT = "errorOnDuplicateInsert";
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistencePoliciesIO.class);

    private PersistencePoliciesIO() {
    }

    public static void write(OutputStream out, PersistencePolicies policies) throws IOException {
        Document doc = newDocument();
        if (doc != null) {
            Element el = write(doc, policies);
            doc.appendChild(el);
            transform(doc, out, true);
        }
    }

    public static PersistencePolicies read(InputStream in) throws IOException {

        Document doc = newDocument(in);
        if (doc != null) {
            Element root = doc.getDocumentElement();
            return read(root);
        } else {
            LOGGER.error("IOException: XML Document is null");
            throw new IOException("IOException: XML Document is null");
        }
    }


    public static Element write(Document doc, PersistencePolicies policies) {

        Element el = doc.createElement(POLICIES);
        el.appendChild(element(ALLOW_NEW_CODES, policies.isAllowNewCodes(), doc));
        el.appendChild(element(ALLOW_MODIFY_MESSAGES, policies.isAllowModifyMessages(), doc));
        el.appendChild(element(ALLOW_NEW_NETWORK_POINTS, policies.isAllowNewNetworkAccessPoints(), doc));
        el.appendChild(element(ALLOW_NEW_OBJECTS, policies.isAllowNewObjects(), doc));
        el.appendChild(element(ALLOW_NEW_PARTICIPANTS, policies.isAllowNewParticipants(), doc));
        el.appendChild(element(ALLOW_NEW_SOURCES, policies.isAllowNewSources(), doc));
        el.appendChild(element(ALLOW_UNKNOWN_DETAIL_TYPES, policies.isAllowUnknownDetailTypes(), doc));
        return el;
    }

    public static PersistencePolicies read(Element parent) throws IOException {

        if (!parent.getTagName().equals(POLICIES)) {
            throw new IOException("unknown element. Got " + parent.getTagName() + " but expected " + POLICIES);
        }
        PersistencePolicies pp = new PersistencePolicies();
        NodeList ch = parent.getChildNodes();
        try {
            for (int i = 0; i < ch.getLength(); i++) {
                Node n = ch.item(i);
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getTagName().equals(ALLOW_MODIFY_MESSAGES)) {
                        pp.setAllowModifyMessages(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getTagName().equals(ALLOW_NEW_CODES)) {
                        pp.setAllowNewCodes(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getTagName().equals(ALLOW_NEW_NETWORK_POINTS)) {
                        pp.setAllowNewNetworkAccessPoints(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getTagName().equals(ALLOW_NEW_OBJECTS)) {
                        pp.setAllowNewObjects(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getTagName().equals(ALLOW_NEW_PARTICIPANTS)) {
                        pp.setAllowNewParticipants(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getTagName().equals(ALLOW_NEW_SOURCES)) {
                        pp.setAllowNewSources(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getTagName().equals(ALLOW_UNKNOWN_DETAIL_TYPES)) {
                        pp.setAllowUnknownDetailTypes(Boolean.valueOf(el.getTextContent().trim()));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception: '{}'", e.getMessage(), e);
            throw new IOException(e.getMessage());
        }
        return pp;
    }

    private static Element element(String name, boolean b, Document doc) {
        Element el = doc.createElement(name);
        el.setTextContent(Boolean.toString(b));
        return el;

    }

    private static Document newDocument(InputStream stream) throws IOException {

        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(stream);
        } catch (ParserConfigurationException e) {
            LOGGER.error("ParserConfigurationException: '{}'", e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.error("SAXException: '{}'", e.getMessage(), e);
        }
        return doc;
    }

    private static Document newDocument() throws IOException {

        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            LOGGER.error("ParserConfigurationException: '{}'", e.getMessage(), e);
        }
        return doc;
    }

    private static StreamResult transform(Document doc, OutputStream out, boolean indent) throws IOException {

        StreamResult sr = new StreamResult(out);
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource doms = new DOMSource(doc);
            transformer.transform(doms, sr);

        } catch (TransformerConfigurationException tce) {
            LOGGER.error("TransformerConfigurationException: '{}'", tce.getMessage(), tce);
            assert (false);
        } catch (TransformerException e) {
            LOGGER.error("TransformerException: '{}'", e.getMessage(), e);
            throw new IOException(e.getMessage());
        }
        return sr;
    }

    public static void main(String[] args) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PersistencePoliciesIO.write(outputStream, new PersistencePolicies());
            LOGGER.info("Persistence Policy:\n{}", outputStream.toString());
        } catch (IOException e) {
            LOGGER.error("IOException: '{}'", e.getMessage(), e);
        }
    }
}
