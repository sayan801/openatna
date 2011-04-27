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

package org.openhealthtools.openatna.anom.codes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaCode;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 11:56:26 AM
 * @date $Date:$ modified by $Author:$
 */

public class CodeParser {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.anom.codes.CodeParser");


    public static void parse(String codes) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(codes);
        parse(s);

    }

    public static void parse(Collection<String> codes) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            javax.xml.parsers.SAXParser sp = spf.newSAXParser();
            Handler handler = new Handler();
            for (String code : codes) {
                try {
                    URL url = new URL(code);
                    InputSource input = new InputSource(url.openStream());
                    input.setSystemId(code.toString());
                    sp.parse(input, handler);
                } catch (Exception e) {
                    log.warn("Error parsing codes at:" + code);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading system codes", e);
        }
    }

    private static class Handler extends DefaultHandler {

        private String currType = null;

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if (qName.equals("CodeType")) {
                currType = atts.getValue("", "name");
            } else if (qName.equals("Code")) {
                String code = atts.getValue("", "code");
                if (currType == null || code == null) {
                    throw new SAXException("Invalid XML. No type or code defined.");
                }
                String display = atts.getValue("", "display");
                String systemName = atts.getValue("", "codingScheme");
                String system = atts.getValue("", "codeSystem");
                AtnaCode ac = new AtnaCode(currType, code, system, systemName, display, null);
                CodeRegistry.addCode(ac);
            }

        }
    }


}
