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

package org.openhealthtools.openatna.anom.jaxb21;

import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.jaxb21.schema.CodedValueType;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:16:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAtnaCode implements AtnaCode {

    private CodedValueType cvt;

    public JaxbAtnaCode(CodedValueType cvt) {
        this.cvt = cvt;
    }

    public JaxbAtnaCode(String code) {
        cvt = new CodedValueType();
        cvt.setCode(code);
    }

    public String getCode() {
        return cvt.getCode();
    }

    public String getCodeSystem() {
        return cvt.getCodeSystem();
    }

    public AtnaCode setCodeSystem(String value) {
        cvt.setCodeSystem(value);
        return this;
    }

    public String getCodeSystemName() {
        return cvt.getCodeSystemName();
    }

    public AtnaCode setCodeSystemName(String value) {
        cvt.setCodeSystemName(value);
        return this;
    }

    public String getDisplayName() {
        return cvt.getDisplayName();
    }

    public AtnaCode setDisplayName(String value) {
        cvt.setDisplayName(value);
        return this;
    }

    public String getOriginalText() {
        return cvt.getOriginalText();
    }

    public AtnaCode setOriginalText(String value) {
        cvt.setOriginalText(value);
        return this;
    }

    public CodedValueType getCodedValueType() {
        return cvt;
    }
}
