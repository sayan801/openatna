package org.openhealthtools.openatna.syslog.message;

import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.OutputStream;

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

        try {
            if (doc == null) {
                throw new SyslogException("Document is null. cannot write it out");
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "no");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, getExpectedEncoding());
            DOMSource doms = new DOMSource(doc);
            StreamResult sr = new StreamResult(out);
            t.transform(doms, sr);

        } catch (TransformerConfigurationException tce) {
            assert (false);
        } catch (TransformerException te) {
            throw new SyslogException(te);
        }
    }

    public Document getMessageObject() {
        return doc;
    }
}
