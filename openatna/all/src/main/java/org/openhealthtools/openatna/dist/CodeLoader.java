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

import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.anom.codes.CodeRegistry;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.CodeDao;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.persistence.util.EntityConverter;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 24, 2009: 12:04:22 AM
 * @date $Date:$ modified by $Author:$
 */

public class CodeLoader {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.dist.CodeLoader");

    public static void main(String[] args) {
        URL codes = CodeLoader.class.getResource("/conf/atnacodes.xml");
        log.info("CodeLoader: codes are being read from:" + codes);
        CodeParser.parse(codes);
        List<AtnaCode> l = CodeRegistry.allCodes();
        SpringDaoFactory f = SpringDaoFactory.getFactory();
        CodeDao dao = f.codeDao();
        for (AtnaCode atnaCode : l) {
            CodeEntity ce = EntityConverter.createCode(atnaCode, EntityConverter.getCodeType(atnaCode));
            try {
                log.info("loading code:" + atnaCode);
                dao.save(ce);
            } catch (AtnaPersistenceException e) {
                log.info("Exception thrown while storing code:" + e.getMessage());
            }
        }
    }


}
