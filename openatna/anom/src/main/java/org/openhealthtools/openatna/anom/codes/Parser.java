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

package org.openhealthtools.openatna.anom.codes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.openhealthtools.openatna.anom.AtnaCode;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 11:56:26 AM
 * @date $Date:$ modified by $Author:$
 */

public class Parser {

    private String[] codes;

    public Parser(String... codes) {
        this.codes = codes;
    }

    public void parse() throws ParserConfigurationException, SAXException, IOException {
        javax.xml.parsers.SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        javax.xml.parsers.SAXParser sp = spf.newSAXParser();
        Handler handler = new Handler();
        for (String code : codes) {
            File f = new File(code);
            org.xml.sax.InputSource input = new InputSource(new FileReader(f));
            input.setSystemId("file://" + f.getAbsolutePath());
            sp.parse(input, handler);
        }
    }

    private class Handler extends DefaultHandler {

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
                //String system = atts.getValue("", "codingSystem");
                AtnaCode ac = new AtnaCode(currType, code, null, systemName, display, null);
                System.out.println(ac);
                CodeRegistry.addCode(ac);
            }

        }
    }

}
