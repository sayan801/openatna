/*
 * Copyright (c) 2010 University of Cardiff and others.
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

import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.service.AuditService;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 7:09:08 PM
 * @date $Date:$ modified by $Author:$
 */

public class Server {

    public static void main(String[] args) {
        AuditService service = AtnaFactory.auditService();
        try {
            service.start();
        } catch (IOException e) {
            throw new RuntimeException("IO Error starting service:", e);
        }
    }
}
