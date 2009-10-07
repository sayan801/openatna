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

package org.openhealthtools.openatna.persistence.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.openhealthtools.openatna.persistence.dao.PersistencePolicies;

/**
 * Utility that reads and writes PersistencePolicies to XML
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 7, 2009: 7:40:47 PM
 * @date $Date:$ modified by $Author:$
 */

public class PersistencePoliciesIO {

    public static final String POLICIES = "PersistencePolicies";

    public static void write(OutputStream out, PersistencePolicies policies) throws IOException {
        Document doc = newDocument();
        Element el = write(doc, policies);
        doc.appendChild(el);
        transform(doc, out, true);
    }

    public static PersistencePolicies read(InputStream in) throws IOException {
        Document doc = newDocument(in);
        Element root = doc.getDocumentElement();
        return read(root);
    }


    public static Element write(Document doc, PersistencePolicies policies) {
        Element el = doc.createElement(POLICIES);
        el.appendChild(element(PersistencePolicies.ALLOW_NEW_CODES,
                policies.isAllowNewCodes(), doc));
        el.appendChild(element(PersistencePolicies.ALLOW_MODIFY_MESSAGES,
                policies.isAllowModifyMessages(), doc));
        el.appendChild(element(PersistencePolicies.ALLOW_NEW_NETWORK_POINTS,
                policies.isAllowNewNetworkAccessPoints(), doc));
        el.appendChild(element(PersistencePolicies.ALLOW_NEW_OBJECTS,
                policies.isAllowNewObjects(), doc));
        el.appendChild(element(PersistencePolicies.ALLOW_NEW_PARTICIPANTS,
                policies.isAllowNewParticipants(), doc));
        el.appendChild(element(PersistencePolicies.ALLOW_NEW_SOURCES,
                policies.isAllowNewSources(), doc));
        el.appendChild(element(PersistencePolicies.ALLOW_UNKNOWN_DETAIL_TYPES,
                policies.isAllowUnknownDetailTypes(), doc));
        return el;
    }

    public static PersistencePolicies read(Element parent) throws IOException {
        if (!parent.getLocalName().equals(POLICIES)) {
            throw new IOException("unknown element. Got " + parent.getLocalName() +
                    " but expected " + POLICIES);
        }
        PersistencePolicies pp = new PersistencePolicies();
        NodeList ch = parent.getChildNodes();
        try {
            for (int i = 0; i < ch.getLength(); i++) {
                Node n = ch.item(i);
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getLocalName().equals(PersistencePolicies.ALLOW_MODIFY_MESSAGES)) {
                        pp.setAllowModifyMessages(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getLocalName().equals(PersistencePolicies.ALLOW_NEW_CODES)) {
                        pp.setAllowNewCodes(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getLocalName().equals(PersistencePolicies.ALLOW_NEW_NETWORK_POINTS)) {
                        pp.setAllowNewNetworkAccessPoints(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getLocalName().equals(PersistencePolicies.ALLOW_NEW_OBJECTS)) {
                        pp.setAllowNewObjects(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getLocalName().equals(PersistencePolicies.ALLOW_NEW_PARTICIPANTS)) {
                        pp.setAllowNewParticipants(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getLocalName().equals(PersistencePolicies.ALLOW_NEW_SOURCES)) {
                        pp.setAllowNewSources(Boolean.valueOf(el.getTextContent().trim()));
                    } else if (el.getLocalName().equals(PersistencePolicies.ALLOW_UNKNOWN_DETAIL_TYPES)) {
                        pp.setAllowUnknownDetailTypes(Boolean.valueOf(el.getTextContent().trim()));
                    }
                }
            }

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return pp;
    }


    private static Element element(String name, boolean b, Document doc) {
        Element el = doc.createElement(name);
        el.setTextContent(Boolean.valueOf(b).toString());
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
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return doc;
    }


    private static Document newDocument() throws IOException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static StreamResult transform(Document doc, OutputStream out, boolean indent) throws IOException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            if (indent)
                t.setOutputProperty(OutputKeys.INDENT, "yes");
            else
                t.setOutputProperty(OutputKeys.INDENT, "no");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        } catch (TransformerConfigurationException tce) {
            assert (false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(out);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            throw new IOException(te.getMessage());
        }
        return sr;
    }

}
