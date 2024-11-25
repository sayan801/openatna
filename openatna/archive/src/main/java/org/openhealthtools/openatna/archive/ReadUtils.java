package org.openhealthtools.openatna.archive;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.*;

public class ReadUtils {

    private ReadUtils() {
    }

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
        if (evt != null) {
            return evt.isStartElement() ? evt : null;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Attribute> getAttributes(XMLEvent evt) {

        List<Attribute> ret = new ArrayList<>();
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

        Map<String, String> ret = new HashMap<>();
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

        XMLEvent evt;
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
        return start.getName().getLocalPart().equals(expected);
    }

    public static boolean peek(XMLEventReader reader, String... expected) throws XMLStreamException {

        XMLEvent evt;
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
        for (String s : expected) {
            if (start.getName().getLocalPart().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
