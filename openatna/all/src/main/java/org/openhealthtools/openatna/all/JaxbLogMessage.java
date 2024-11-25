package org.openhealthtools.openatna.all;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.process.AtnaLogMessage;
import org.openhealthtools.openatna.anom.JaxbIOFactory;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class JaxbLogMessage extends AtnaLogMessage {

    public JaxbLogMessage() {
        super(new JaxbIOFactory());
    }

    public JaxbLogMessage(AtnaMessage message) {
        super(message, new JaxbIOFactory());
    }
}
