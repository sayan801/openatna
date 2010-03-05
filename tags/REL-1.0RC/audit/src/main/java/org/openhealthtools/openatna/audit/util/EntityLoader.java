/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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

package org.openhealthtools.openatna.audit.util;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.persistence.util.DataReader;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 12:21:03 PM
 * @date $Date:$ modified by $Author:$
 */

public class EntityLoader {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.util.EntityLoader");

    private EntityLoader() {
    }

    public static void main(String[] args) {
        URL codes = CodeLoader.class.getResource("/conf/entities.xml");
        log.info("EntityLoader: entities are being read from:" + codes);
        try {
            InputStream in = codes.openStream();
            DataReader reader = new DataReader(in);
            reader.parse();
        } catch (Exception e) {
            log.error("Could not load data!", e);
        }

    }


}
