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

package org.openhealthtools.openatna.dist;

import java.io.IOException;
import java.io.File;

import org.openhealthtools.openatna.audit.server.ServerConfiguration;
import org.openhealthtools.openatna.audit.server.AtnaServer;
import org.openhealthtools.openatna.audit.server.PropertiesLoader;
import org.openhealthtools.openatna.audit.AuditService;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 7:09:08 PM
 * @date $Date:$ modified by $Author:$
 */

public class Server {

    public static void main(String[] args) {
        ServerConfiguration sc = ServerConfiguration.getInstance();
        File actors = PropertiesLoader.getActorsFile();
        if (actors == null) {
            throw new RuntimeException("no Actors file found. Cannot continue.");
        }
        boolean configured = sc.loadActors(actors);
        if(!configured) {
            throw new RuntimeException("Could not configure AtnaServer");
        }
        AtnaServer server = sc.getActor(AtnaServer.class);
        if (server == null) {
            throw new RuntimeException("No AtnaServer was created! Cannot go on.");
        }
        AuditService service = new AuditService();
        service.setSyslogServer(server);
        service.setPersistencePolicies(server.getPersistencePolicies());
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
