/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
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
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.openhealthtools.openatna.audit.persistence.util.QueryString;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 7, 2009: 9:33:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class ReportConfig extends HashMap<String, Object> {

    public static final String REPORT_CONFIG = "reportConfig";
    public static final String PROPERTY = "property";
    public static final String KEY = "key";
    public static final String TYPE = "type";
    public static final String STRING = "string";
    public static final String BOOLEAN = "boolean";

    public static final String TITLE = "title";
    public static final String QUERY = "query";
    public static final String QUERY_LANGUAGE = "queryLanguage";
    public static final String GROUPING_FIELD = "groupingField";

    public static final String OUTPUT_DIRECTORY = "outputDirectory";
    public static final String OUTPUT_FILE_NAME = "outputFileName";
    // PDF, HTML etc
    public static final String OUTPUT_TYPE = "outputType";

    public static final String INPUT_DIRECTORY = "inputDirectory";
    public static final String REPORT_INSTANCE = "reportInstance";
    public static final String TARGET = "target";


    public static final String MESSAGES = "MESSAGES";
    public static final String CODES = "CODES";
    public static final String SOURCES = "SOURCES";
    public static final String PARTICIPANTS = "PARTICIPANTS";
    public static final String OBJECTS = "OBJECTS";
    public static final String NETWORK_ACCESS_POINTS = "NETWORK_ACCESS_POINTS";
    public static final String PROVISIONAL_MESSAGES = "PROVISIONAL_MESSAGES";
    public static final String ALL_SYSTEM = "ALL_SYSTEM";
    public static final String ALL = "ALL";

    public static final String HQL = "HQL";
    public static final String ATNA = "ATNA";

    public static final String HTML = "HTML";
    public static final String PDF = "PDF";

    public static final String[] outputTypes = {
            PDF,
            HTML
    };

    public static final String[] queryLanguages = {
            HQL,
            ATNA
    };

    public static final String[] targets = {
            MESSAGES,
            CODES,
            SOURCES,
            PARTICIPANTS,
            OBJECTS,
            NETWORK_ACCESS_POINTS
    };

    public ReportConfig() {

    }

    public String getTitle() {
        return (String) get(TITLE);
    }

    public void setTitle(String title) {
        put(TITLE, title);
    }

    public String getQuery() {
        return (String) get(QUERY);
    }

    public void setQuery(String query) {
        put(QUERY, query);
    }

    public String getQueryLanguage() {
        return (String) get(QUERY_LANGUAGE);
    }

    public void setQueryLanguage(String queryLanguage) {
        put(QUERY_LANGUAGE, queryLanguage);
    }

    public String getOutputDirectory() {
        return (String) get(OUTPUT_DIRECTORY);
    }

    public void setOutputDirectory(String input) {
        put(OUTPUT_DIRECTORY, input);
    }

    public String getInputDirectory() {
        return (String) get(INPUT_DIRECTORY);
    }

    public void setInputDirectory(String input) {
        put(INPUT_DIRECTORY, input);
    }

    public String getOutputType() {
        return (String) get(OUTPUT_TYPE);
    }

    public void setOutputType(String outputType) {
        put(OUTPUT_TYPE, outputType);
    }

    public String getOutputFileName() {
        return (String) get(OUTPUT_FILE_NAME);
    }

    public void setOutputFileName(String outputFileName) {
        put(OUTPUT_FILE_NAME, outputFileName);
    }

    public String getReportInstance() {
        return (String) get(REPORT_INSTANCE);
    }

    public void setReportInstance(String reportInstance) {
        put(REPORT_INSTANCE, reportInstance);
    }

    public void setGroupingField(String field) {
        put(GROUPING_FIELD, field);
    }

    public String getGroupingField() {
        return (String) get(GROUPING_FIELD);
    }

    public void setTarget(String entity) {
        put(TARGET, entity);
    }

    public String getTarget() {
        return (String) get(TARGET);
    }


    public static ReportConfig fromXml(InputStream in) throws IOException {
        ReportConfig conf = new ReportConfig();
        Document doc = newDocument(in);
        Element root = doc.getDocumentElement();
        if (!root.getTagName().equalsIgnoreCase(REPORT_CONFIG)) {
            throw new IOException("unknown XML root element:" + root.getTagName());
        }
        NodeList l = root.getChildNodes();
        for (int i = 0; i < l.getLength(); i++) {
            Node n = l.item(i);
            if (n instanceof Element) {
                Element p = (Element) n;
                if (p.getTagName().equalsIgnoreCase(PROPERTY)) {
                    String type = STRING;
                    String key = p.getAttribute(KEY);
                    if (key == null || key.length() == 0) {
                        continue;
                    }
                    String cls = p.getAttribute(TYPE);
                    if (cls != null) {
                        type = cls;
                    }

                    String val = p.getTextContent().trim();
                    if (val != null && val.length() > 0) {
                        Object v = val;
                        if (type.equals(BOOLEAN)) {
                            v = Boolean.valueOf(val);
                        }
                        conf.put(key, v);
                    }
                }
            }
        }
        return conf;
    }

    public static void toXml(ReportConfig conf, OutputStream out) throws IOException {
        Document doc = newDocument();
        Element root = doc.createElement(REPORT_CONFIG);
        Set<String> keys = conf.keySet();
        for (String key : keys) {
            Object val = conf.get(key);
            String type;
            String v;
            if (val instanceof String) {
                type = STRING;
                v = (String) val;
            } else if (val instanceof Boolean) {
                type = BOOLEAN;
                v = val.toString();
            } else {
                continue;
            }
            Element prop = doc.createElement(PROPERTY);
            prop.setAttribute(KEY, key);
            prop.setAttribute(TYPE, type);
            prop.setTextContent(v.trim());
            root.appendChild(prop);
        }
        doc.appendChild(root);
        transform(doc, out, true);

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
            if (indent) {
                t.setOutputProperty(OutputKeys.INDENT, "yes");
            } else {
                t.setOutputProperty(OutputKeys.INDENT, "no");
            }
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


    public static void main(String[] args) {
        Query query = new Query();
        query.notNull(Query.Target.EVENT_OUTCOME);
        ReportConfig config = new ReportConfig();
        config.setOutputDirectory(System.getProperty("user.home") + File.separator + "ATNAreports");
        config.setOutputFileName("report");
        config.setQuery(QueryString.create(query));

        config.setGroupingField("eventId");
        config.setTitle("Messages");
        config.setOutputType("PDF");
        try {
            ReportConfig.toXml(config, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
