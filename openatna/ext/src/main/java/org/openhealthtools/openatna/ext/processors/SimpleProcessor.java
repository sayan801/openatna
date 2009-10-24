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

package org.openhealthtools.openatna.ext.processors;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.process.AtnaProcessor;
import org.openhealthtools.openatna.audit.process.ProcessContext;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 4:24:15 PM
 * @date $Date:$ modified by $Author:$
 */

public class SimpleProcessor implements AtnaProcessor {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.ext.processors.SimpleProcessor");

    public void process(ProcessContext context) throws Exception {
        log.info("process context has state " + context.getState() + " at " + new Date());
    }

    public void error(ProcessContext context) {
    }
}
