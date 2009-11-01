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

package org.openhealthtools.openatna.audit.service;

import java.io.IOException;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.server.ServerConfiguration;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 29, 2009: 6:15:48 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AuditService {

    String PROPERTY_DAO_FACTORY = AuditService.class.getName() + ".dao.factory";
    String PROPERTY_PERSISTENCE_POLICIES = AuditService.class.getName() + ".persistence.policies";

    /**
     * start the service
     *
     * @throws IOException
     */
    public void start() throws IOException;

    /**
     * stop the service
     *
     * @throws IOException
     */
    public void stop() throws IOException;

    /**
     * get the syslog server that will receive the messages.
     * This should be fully configured, including have the LogMessage set.
     *
     * @return
     */
    public ServerConfiguration getServerConfig();

    /**
     * set the fully configured syslog server
     *
     * @param config
     */
    public void setServerConfig(ServerConfiguration config);

    /**
     * process an AtnaMessage
     *
     * @return
     */
    public void process(AtnaMessage message);

    /**
     * get the ServiceConfig
     *
     * @return
     */
    public ServiceConfiguration getServiceConfig();

    /**
     * set the ServiceConfig
     *
     * @param serviceConfig
     */
    public void setServiceConfig(ServiceConfiguration serviceConfig);
}
