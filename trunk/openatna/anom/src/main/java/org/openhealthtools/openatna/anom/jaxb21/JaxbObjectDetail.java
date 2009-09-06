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

import org.openhealthtools.openatna.anom.AtnaObjectDetail;
import org.openhealthtools.openatna.anom.jaxb21.schema.TypeValuePairType;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 7:22:53 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbObjectDetail implements AtnaObjectDetail {

    private TypeValuePairType pair;

    public JaxbObjectDetail(TypeValuePairType pair) {
        this.pair = pair;
    }

    public JaxbObjectDetail() {
        this.pair = new TypeValuePairType();
    }

    public String getType() {
        return pair.getType();
    }

    public AtnaObjectDetail setType(String value) {
        pair.setType(value);
        return this;
    }

    public byte[] getValue() {
        return pair.getValue();
    }

    public AtnaObjectDetail setValue(byte[] value) {
        pair.setValue(value);
        return this;
    }

    public TypeValuePairType getPair() {
        return pair;
    }
}
