/**
 * Copyright (c) 2009-2011 University of Cardiff and others.
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

package org.openhealthtools.openatna.archive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Mar 12, 2010: 12:39:58 PM
 */

public class ReadUtils {


    public static XMLEvent dig(XMLEventReader reader, String... tags) throws XMLStreamException {
        XMLEvent evt = null;
        for (String tag : tags) {
            evt = reader.nextTag();
            if (evt.isStartElement()) {
                StartElement start = evt.asStartElement();
                String name = start.getName().getLocalPart();
                if (!name.equals(tag)) {
                    return evt;
                }
            }
        }
        return evt.isStartElement() ? evt : null;
    }

    @SuppressWarnings("unchecked")
    public static List<Attribute> getAttributes(XMLEvent evt) {
        List<Attribute> ret = new ArrayList<Attribute>();
        if (evt.isStartElement()) {
            StartElement start = evt.asStartElement();
            Iterator<Attribute> iterator = start.getAttributes();
            while (iterator.hasNext()) {
                ret.add(iterator.next());
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getAttributeMap(XMLEvent evt) {
        Map<String, String> ret = new HashMap<String, String>();
        if (evt.isStartElement()) {
            StartElement start = evt.asStartElement();
            Iterator<Attribute> iterator = start.getAttributes();
            while (iterator.hasNext()) {
                Attribute a = iterator.next();
                ret.put(a.getName().getLocalPart(), a.getValue());
            }
        }
        return ret;
    }

    public static boolean peek(XMLEventReader reader, String expected) throws XMLStreamException {

        XMLEvent evt = null;
        while (true) {
            evt = reader.peek();
            if (evt.isEndElement() || evt.isEndDocument()) {
                return false;
            } else if (evt.isStartElement()) {
                break;
            } else {
                reader.nextEvent();
            }
        }
        StartElement start = evt.asStartElement();
        if (start.getName().getLocalPart().equals(expected)) {
            return true;
        }
        return false;
    }

    public static boolean peek(XMLEventReader reader, String... expected) throws XMLStreamException {

        XMLEvent evt = null;
        while (true) {
            evt = reader.peek();
            if (evt.isEndElement() || evt.isEndDocument()) {
                return false;
            } else if (evt.isStartElement()) {
                break;
            } else {
                reader.nextEvent();
            }
        }
        StartElement start = evt.asStartElement();
        for (int i = 0; i < expected.length; i++) {
            String s = expected[i];
            if (start.getName().getLocalPart().equals(s)) {
                return true;
            }
        }

        return false;
    }
}
