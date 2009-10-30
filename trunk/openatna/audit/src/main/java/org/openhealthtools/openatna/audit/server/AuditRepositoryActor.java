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

package org.openhealthtools.openatna.audit.server;

import org.openhealthtools.openatna.audit.ServiceConfig;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 30, 2009: 10:36:30 AM
 * @date $Date:$ modified by $Author:$
 */

public class AuditRepositoryActor {

    private AtnaServer server;
    private ServiceConfig config;

    public AuditRepositoryActor() {
    }

    public AuditRepositoryActor(AtnaServer server, ServiceConfig config) {
        this.server = server;
        this.config = config;
    }

    public AtnaServer getServer() {
        return server;
    }

    public void setServer(AtnaServer server) {
        this.server = server;
    }

    public ServiceConfig getConfig() {
        return config;
    }

    public void setConfig(ServiceConfig config) {
        this.config = config;
    }
}
