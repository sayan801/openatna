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

package org.openhealthtools.openatna.syslog.message;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;

/**
 * W3C DOM implementation of the LogMessage interface.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 18, 2009: 6:19:03 PM
 * @date $Date:$ modified by $Author:$
 */

public class XmlLogMessage implements LogMessage<Document> {

    private Document doc;
    private String encoding = Constants.ENC_UTF8;

    public XmlLogMessage() {
    }

    public XmlLogMessage(Document doc) {
        this.doc = doc;
    }

    public String getExpectedEncoding() {
        return encoding;
    }

    public void read(InputStream in, String encoding) throws SyslogException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(in);
            String enc = doc.getXmlEncoding();
            if (enc != null) {
                this.encoding = enc;
            }
        } catch (Exception e) {
            throw new SyslogException(e);
        }
    }

    public void write(OutputStream out) throws SyslogException {
        if (doc == null) {
            throw new SyslogException("Document is null. cannot write it out");
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "no");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, getExpectedEncoding());
        } catch (TransformerConfigurationException tce) {
            assert (false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(out);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            throw new SyslogException(te);
        }
    }

    public Document getMessageObject() {
        return doc;
    }

}
