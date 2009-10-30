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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openhealthtools.openatna.audit.impl.AuditServiceImpl;
import org.openhealthtools.openatna.audit.server.AuditRepositoryActor;
import org.openhealthtools.openatna.audit.server.PropertiesLoader;
import org.openhealthtools.openatna.audit.server.ServerConfiguration;

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
        if (!configured) {
            throw new RuntimeException("Could not configure AtnaServer");
        }
        List<AuditRepositoryActor> arrs = sc.getAuditRepositoryActors();
        if (arrs.size() == 0) {
            throw new RuntimeException("No Actors! Cannot go on.");
        }
        AuditRepositoryActor arr = arrs.get(0);
        AuditServiceImpl service = new AuditServiceImpl();
        service.setSyslogServer(arr.getServer());
        service.setServiceConfig(arr.getConfig());
        try {
            service.start();
        } catch (IOException e) {
            throw new RuntimeException("IO Error starting service:", e);
        }


    }
}
