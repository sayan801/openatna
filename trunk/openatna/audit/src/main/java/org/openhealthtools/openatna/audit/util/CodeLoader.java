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

package org.openhealthtools.openatna.audit.util;

import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.anom.codes.CodeRegistry;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.EntityConverter;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 12:04:22 AM
 * @date $Date:$ modified by $Author:$
 */

public class CodeLoader {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.util.CodeLoader");

    private CodeLoader() {
    }

    public static void main(String[] args) {
        URL codes = CodeLoader.class.getResource("/conf/atnacodes.xml");
        log.info("CodeLoader: codes are being read from:" + codes);
        CodeParser.parse(codes.toString());
        List<AtnaCode> l = CodeRegistry.allCodes();
        CodeDao dao = AtnaFactory.codeDao();
        for (AtnaCode atnaCode : l) {
            CodeEntity ce = EntityConverter.createCode(atnaCode, EntityConverter.getCodeType(atnaCode));
            PersistencePolicies pp = new PersistencePolicies();
            pp.setErrorOnDuplicateInsert(false);
            pp.setAllowNewCodes(true);
            try {
                if (dao.save(ce, pp)) {
                    log.info("loading code:" + atnaCode);
                }
            } catch (AtnaPersistenceException e) {
                log.info("Exception thrown while storing code:" + e.getMessage());
            }
        }
    }

}
