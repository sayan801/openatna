/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.all;

import java.io.IOException;

import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.openhealthtools.openexchange.config.ConfigProcessorFactory;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.config.SpringFacade;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 21, 2009: 7:09:08 PM
 * @date $Date:$ modified by $Author:$
 */

public class Server {

    public static void main(String[] args) {
    	
		//pre config process
        ConfigProcessorFactory.getConfigProcessor().preProcess();

        AuditService service = AtnaFactory.auditService();
        try {
            service.start();
        } catch (IOException e) {
            throw new RuntimeException("IO Error starting service:", e);
        }
        
        //post config process
        ConfigProcessorFactory.getConfigProcessor().postProcess(null);
    }
}
