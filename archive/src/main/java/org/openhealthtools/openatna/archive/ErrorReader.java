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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 27, 2010: 5:56:09 PM
 */

public class ErrorReader {

    public void begin(XMLEventReader reader) throws XMLStreamException {
        ReadUtils.dig(reader, DataConstants.ERRORS);
    }


    public List<ErrorEntity> readErrors(int max, XMLEventReader reader) throws XMLStreamException {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        List<ErrorEntity> ret = new ArrayList<ErrorEntity>();

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
                    se.setErrorTimestamp(Archiver.archiveFormat.parse(a.getValue()));
                } catch (ParseException e) {
                    throw new XMLStreamException(e);
                }
            }
        }
        while (true) {
            XMLEvent code = reader.peek();
            if (code.isStartElement()) {
                StartElement el = code.asStartElement();
                if (el.getName().getLocalPart().equals(DataConstants.ERROR_MESSAGE)) {
                    se.setErrorMessage(reader.getElementText());
                } else if (el.getName().getLocalPart().equals(DataConstants.ERROR_STACKTRACE)) {
                    se.setStackTrace(Base64.decode(reader.getElementText()));
                } else if (el.getName().getLocalPart().equals(DataConstants.ERROR_PAYLOAD)) {
                    se.setPayload(Base64.decode(reader.getElementText()));
                } else {
                    reader.nextEvent();
                }
            } else if (code.isEndElement()) {
                EndElement el = code.asEndElement();
                if (el.getName().getLocalPart().equals(DataConstants.ERROR)) {
                    //System.out.println("EntityReader.readMessage end element found:" + el.toString());
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