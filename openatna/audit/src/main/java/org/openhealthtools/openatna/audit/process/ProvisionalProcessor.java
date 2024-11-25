/**
 * Copyright (c) 2009-2011 University of Cardiff and others
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * <p>
 * Contributors:
 * University of Cardiff - initial API and implementation
 * -
 */

package org.openhealthtools.openatna.audit.process;

import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.ProvisionalMessage;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.AuditException;
import org.openhealthtools.openatna.audit.persistence.dao.ProvisionalDao;
import org.openhealthtools.openatna.audit.persistence.model.ProvisionalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 9:16:14 PM
 * @date $Date:$ modified by $Author:$
 */

public class ProvisionalProcessor implements AtnaProcessor {

    private static final Logger log = LoggerFactory.getLogger("org.openhealthtools.openatna.audit.process.ProvisionalProcessor");

    public void process(ProcessContext context) throws Exception {

        AtnaMessage msg = context.getMessage();
        if (msg == null) {
            throw new AuditException("no message", null, AuditException.AuditError.NULL_MESSAGE);
        }
        if (msg instanceof ProvisionalMessage) {
            log.debug("This is a provisional message!!!");
            ProvisionalEntity pe = new ProvisionalEntity();
            pe.setContent(((ProvisionalMessage) msg).getContent());
            ProvisionalDao pd = AtnaFactory.provisionalDao();
            synchronized (this) {
                pd.save(pe);
                context.setState(ProcessContext.State.ABORTED);
            }
        }
    }

    public void error(ProcessContext context) {
    }
}
