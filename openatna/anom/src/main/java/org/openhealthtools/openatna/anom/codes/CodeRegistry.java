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

package org.openhealthtools.openatna.anom.codes;

import java.util.*;

import org.openhealthtools.openatna.anom.AtnaCode;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 5, 2009: 11:31:10 AM
 * @date $Date:$ modified by $Author:$
 */

public class CodeRegistry {

    private static Map<String, Set<AtnaCode>> codes = new HashMap<String, Set<AtnaCode>>();

    public static void addCode(AtnaCode code) {
        Set<AtnaCode> l = codes.get(code.getCodeType());
        if (l == null) {
            l = new HashSet<AtnaCode>();
        }
        l.add(code);
        codes.put(code.getCodeType(), l);
    }

    public static List<AtnaCode> getCodes(String type) {
        Set<AtnaCode> l = codes.get(type);
        if (l != null) {
            return new ArrayList<AtnaCode>(l);
        }
        return new ArrayList<AtnaCode>();
    }

    public static AtnaCode getCode(String type, String code, String system, String systemName) {
        if (type == null || code == null) {
            return null;
        }
        Set<AtnaCode> l = codes.get(type);
        if (l == null) {
            return null;
        }
        for (AtnaCode atnaCode : l) {
            if (!atnaCode.getCode().equals(code)) {
                continue;
            }
            if (atnaCode.getCodeSystem() != null && system != null) {
                if (!atnaCode.getCodeSystem().equals(system)) {
                    continue;
                }
            }
            if (atnaCode.getCodeSystemName() != null && systemName != null) {
                if (!atnaCode.getCodeSystemName().equals(systemName)) {
                    continue;
                }
            }
            return atnaCode;
        }
        return null;
    }


}
