package org.openhealthtools.openatna.syslog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author tuncay
 */
public class XMLUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLUtil.class);

    /**
     * Creates a new instance of XMLUtil
     */
    private XMLUtil() {
    }

    /**
     * returns null if Node is null
     */
    public static Node extractFromDOMTree(Node node) throws ParserConfigurationException {

        if (node == null) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document theDocument = db.newDocument();
        theDocument.appendChild(theDocument.importNode(node, true));
        return theDocument.getDocumentElement();
    }

    public static org.w3c.dom.Document parseContent(byte[] byteContent) throws ParserConfigurationException, SAXException, IOException {

        String content = new String(byteContent);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        StringReader lReader = new StringReader(content);
        InputSource inputSource = new InputSource(lReader);
        return docBuilder.parse(inputSource);
    }

    public static org.w3c.dom.Document parseContent(String content) throws ParserConfigurationException, SAXException, IOException {

        org.w3c.dom.Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        StringReader lReader = new StringReader(content);
        InputSource inputSource = new InputSource(lReader);
        doc = docBuilder.parse(inputSource);
        return doc;
    }

    public static String prettyPrint(Node node) throws TransformerException {

        StringWriter stringWriter = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
        return stringWriter.toString();
    }

    public static byte[] convertToByteArray(Node node) throws TransformerException {

        /** FIXME: We assume that Transfor deals with encoding/charset internally */
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(node), new StreamResult(bos));
        return bos.toByteArray();
    }

    public static Map<String, String> parseNamespaceBindings(String namespaceBindings) {

        if (namespaceBindings == null) {
            return null;
        }
        String namespacesParsed = namespaceBindings.substring(1, namespaceBindings.length() - 1);
        String[] bindings = namespacesParsed.split(",");
        Map<String, String> namespaces = new HashMap<>();
        for (String binding : bindings) {
            String[] pair = binding.trim().split("=");
            String prefix = pair[0].trim();
            String namespace = pair[1].trim();
            //Remove ' and '
            namespaces.put(prefix, namespace);
        }
        return namespaces;
    }

    public static Document marshall(Object object, String context, String schemaLocation) {

        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try {
            JAXBContext jc = JAXBContext.newInstance(context);
            Marshaller marshaller = jc.createMarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            marshaller.setSchema(schema);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            marshaller.marshal(object, doc);
            Locale.setDefault(oldLocale);
            return doc;
        } catch (Exception ex) {
            LOGGER.error("Exception: '{}'", ex.getMessage(), ex);
        }
        Locale.setDefault(oldLocale);
        return null;
    }

    public static Object unmarshall(String context, String schemaLocation, String content) {

        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try {
            JAXBContext jc = JAXBContext.newInstance(context);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            unmarshaller.setSchema(schema);

            Object obj = unmarshaller.unmarshal(new StringReader(content));
            Locale.setDefault(oldLocale);
            return obj;
        } catch (Exception ex) {
            LOGGER.error("Exception: '{}'", ex.getMessage(), ex);
        }
        Locale.setDefault(oldLocale);
        return null;
    }

    public static Object unmarshallWithoutValidation(String context, String schemaLocation, String content) {

        Locale oldLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en"));
        try {
            JAXBContext jc = JAXBContext.newInstance(context);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(schemaLocation));
            Object obj = unmarshaller.unmarshal(new StringReader(content));
            Locale.setDefault(oldLocale);
            return obj;
        } catch (Exception ex) {
            LOGGER.error("Exception: '{}'", ex.getMessage(), ex);
        }
        Locale.setDefault(oldLocale);
        return null;
    }

    public static void main(String... args) {

        try {
            String xmlString = "<RegistryResponse xmlns=\"urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1\" status=\"Success\"><Slot/></RegistryResponse>";
            XMLUtil.parseContent(xmlString.getBytes());
        } catch (Exception ex) {
            LOGGER.error("Exception: '{}'", ex.getMessage(), ex);
        }
    }
}
