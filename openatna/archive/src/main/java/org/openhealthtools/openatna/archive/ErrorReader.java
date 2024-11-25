package org.openhealthtools.openatna.archive;

import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ErrorReader {

    public void begin(XMLEventReader reader) throws XMLStreamException {
        ReadUtils.dig(reader, DataConstants.ERRORS);
    }

    public List<ErrorEntity> readErrors(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<ErrorEntity> ret = new ArrayList<>();

        boolean is = ReadUtils.peek(reader, DataConstants.ERROR);
        while (is && ret.size() < max) {
            ret.add(readError(reader));
            reader.nextTag();
            is = ReadUtils.peek(reader, DataConstants.ERROR);
        }
        return ret;
    }

    public ErrorEntity readError(XMLEventReader reader) throws XMLStreamException {

        XMLEvent evt = ReadUtils.dig(reader, DataConstants.ERROR);
        List<Attribute> attrs = ReadUtils.getAttributes(evt);
        ErrorEntity se = new ErrorEntity();
        for (Attribute a : attrs) {
            String attr = a.getName().getLocalPart();
            if (attr.equalsIgnoreCase(DataConstants.SOURCE_IP)) {
                se.setSourceIp(a.getValue());
            } else if (attr.equalsIgnoreCase(DataConstants.ERROR_TIMESTAMP)) {
                try {
                    se.setErrorTimestamp(Archiver.parseDate(a.getValue()));
                } catch (ParseException e) {
                    throw new XMLStreamException(e);
                }
            }
        }
        while (true) {

            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                switch (el.getName().getLocalPart()) {
                    case DataConstants.ERROR_MESSAGE:
                        se.setErrorMessage(reader.getElementText());
                        break;
                    case DataConstants.ERROR_STACKTRACE:
                        se.setStackTrace(Base64.decode(reader.getElementText()));
                        break;
                    case DataConstants.ERROR_PAYLOAD:
                        se.setPayload(Base64.decode(reader.getElementText()));
                        break;
                    default:
                        reader.nextEvent();
                        break;
                }
            } else if (code.isEndElement()) {
                EndElement el = code.asEndElement();
                if (el.getName().getLocalPart().equals(DataConstants.ERROR)) {
                    // got to end of entity
                    break;
                }
                // move on one event
                reader.nextEvent();
            } else {
                // move on - it's a comment or space or something
                reader.nextEvent();
            }
        }
        return se;
    }
}
