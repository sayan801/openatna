/**
 * Copyright (c) 2009-2011 University of Cardiff and others.
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

package org.openhealthtools.openatna.audit.persistence.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Mar 3, 2010: 3:03:04 PM
 */

public class CodesUtils {

    public static boolean equivalent(CodeEntity ce1, CodeEntity ce2) {
        // if types don't match then we're done
        if (!ce1.getType().equals(ce2.getType())) {
            return false;
        }
        String code1 = ce1.getCode();
        String code2 = ce2.getCode();
        // if codes don't match then we're done
        if (!code1.equals(code2)) {
            return false;
        }
        String codeSystem1 = ce1.getCodeSystem();
        String codeSystem2 = ce2.getCodeSystem();
        String codeSystemName1 = ce1.getCodeSystemName();
        String codeSystemName2 = ce2.getCodeSystemName();
        // both only define a code. We're done
        if (codeSystem1 == null && codeSystem2 == null &&
                codeSystemName1 == null && codeSystemName2 == null) {
            return true;
        }
        if (codeSystem1 != null && codeSystem2 != null) {
            // match on code and code system is enough - no name needed
            if (codeSystem1.equals(codeSystem2)) {
                return true;
            }
        }
        if (codeSystemName1 != null && codeSystemName2 != null) {
            // match on code and code system name will have to do if one does not define a code system
            if (codeSystemName1.equals(codeSystemName2)) {
                return true;
            }
        }
        // now it gets flakey
        if (codeSystemName1 == null && "RFC-3881".equals(codeSystemName2)) {
            // an atna code
            return true;
        }
        if (codeSystemName2 == null && "RFC-3881".equals(codeSystemName1)) {
            // an atna code
            return true;
        }
        return false;

    }

    public static boolean equivalent(Set<? extends CodeEntity> ce1s, Set<? extends CodeEntity> ce2s) {
        if (ce1s.size() != ce2s.size()) {
            return false;
        }
        if (ce1s.size() == 0) {
            return true;
        }
        Set<? extends CodeEntity> copy = new HashSet(ce2s);

        for (CodeEntity ce1 : ce1s) {
            boolean matched = false;
            Iterator<? extends CodeEntity> it = copy.iterator();
            while (it.hasNext()) {
                CodeEntity entity = it.next();
                if (equivalent(ce1, entity)) {
                    matched = true;
                    it.remove();
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }
}
