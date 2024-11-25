/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */
package org.openhealthtools.openatna.anom.codes;

import org.openhealthtools.openatna.anom.AtnaCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 11:56:26 AM
 * @date $Date:$ modified by $Author:$
 */
public class CodeParser {

    static Logger log = LoggerFactory.getLogger("org.openhealthtools.openatna.anom.codes.CodeParser");

    private CodeParser() {
        super();
    }

    public static void parse(String codes) {
        ArrayList<String> s = new ArrayList<>();
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
                    input.setSystemId(code);
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

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

            if ("CodeType".equals(qName)) {
                currType = atts.getValue("", "name");
            } else if ("Code".equals(qName)) {
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
