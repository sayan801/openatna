package org.openhealthtools.openatna.archive;

import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.List;

public class ErrorWriter {

    public void begin(XMLStreamWriter writer) throws XMLStreamException {

        writer.writeStartDocument();
        writer.writeStartElement(DataConstants.ERRORS);
    }

    public void writeErrors(List<? extends ErrorEntity> errors, XMLStreamWriter writer) throws XMLStreamException {

        for (ErrorEntity error : errors) {
            writer.writeStartElement(DataConstants.ERROR);
            if (error.getSourceIp() != null) {
                writer.writeAttribute(DataConstants.SOURCE_IP, error.getSourceIp());
            }
            if (error.getErrorTimestamp() != null) {
                writer.writeAttribute(DataConstants.ERROR_TIMESTAMP, Archiver.formatDate(error.getErrorTimestamp()));
            }
            if (error.getErrorMessage() != null) {
                writer.writeStartElement(DataConstants.ERROR_MESSAGE);
                writer.writeCharacters(error.getErrorMessage());
                writer.writeEndElement();
            }
            if (error.getStackTrace() != null) {
                writer.writeStartElement(DataConstants.ERROR_STACKTRACE);
                writer.writeCData(Base64.encode(error.getStackTrace()));
                writer.writeEndElement();
            }
            if (error.getPayload() != null) {
                writer.writeStartElement(DataConstants.ERROR_PAYLOAD);
                writer.writeCharacters(Base64.encode(error.getPayload()));
                writer.writeEndElement();
            }

            writer.writeEndElement();
        }
    }

    public void finish(XMLStreamWriter writer) throws IOException {

        try {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            throw new IOException(e.getMessage());
        }
    }
}
