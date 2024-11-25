package org.openhealthtools.openatna.all;

import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.service.AuditService;

import java.io.IOException;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 */
public class Server {

    public static void main(String[] args) {

        AuditService service = AtnaFactory.auditService();

        try {
            service.start();
        } catch (IOException e) {
            throw new ATNAServerException("IO Error starting service:", e);
        }
    }
}
