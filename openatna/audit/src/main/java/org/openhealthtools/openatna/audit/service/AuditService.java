package org.openhealthtools.openatna.audit.service;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.server.ServerConfiguration;

import java.io.IOException;

/**
 * @author Andrew Harrison
 */
public interface AuditService {

    /**
     * start the service
     *
     * @throws IOException
     */
    void start() throws IOException;

    /**
     * stop the service
     *
     * @throws IOException
     */
    void stop() throws IOException;

    /**
     * get the syslog server that will receive the messages.
     * This should be fully configured, including have the LogMessage set.
     *
     * @return
     */
    ServerConfiguration getServerConfig();

    /**
     * set the fully configured syslog server
     *
     * @param config
     */
    void setServerConfig(ServerConfiguration config);

    /**
     * process an AtnaMessage
     *
     * @return
     */
    boolean process(AtnaMessage message) throws Exception;

    /**
     * get the ServiceConfig
     *
     * @return
     */
    ServiceConfiguration getServiceConfig();

    /**
     * set the ServiceConfig
     *
     * @param serviceConfig
     */
    void setServiceConfig(ServiceConfiguration serviceConfig);
}
