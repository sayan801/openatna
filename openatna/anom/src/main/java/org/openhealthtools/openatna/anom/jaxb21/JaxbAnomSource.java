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

import org.openhealthtools.openatna.anom.AnomCode;
import org.openhealthtools.openatna.anom.AnomSource;
import org.openhealthtools.openatna.anom.jaxb21.schema.AuditSourceIdentificationType;
import org.openhealthtools.openatna.anom.jaxb21.schema.CodedValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 6:41:00 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbAnomSource implements AnomSource {

    private AuditSourceIdentificationType source;

    public JaxbAnomSource(AuditSourceIdentificationType source) {
        this.source = source;
    }

    public JaxbAnomSource(String sourceId) {
        this.source = new AuditSourceIdentificationType();
        this.source.setAuditSourceID(sourceId);
    }

    public List<AnomCode> getSourceTypeCodes() {
        List<AnomCode> codes = new ArrayList<AnomCode>();
        List<CodedValueType> l = source.getAuditSourceTypeCode();
        for (CodedValueType codedValueType : l) {
            codes.add(new JaxbAnomCode(codedValueType));
        }
        return codes;
    }

    public AnomSource addSourceTypeCode(AnomCode value) {
        if (value instanceof JaxbAnomCode) {
            JaxbAnomCode code = (JaxbAnomCode) value;
            source.getAuditSourceTypeCode().add(code.getCodedValueType());
        }
        return this;
    }

    public AnomSource removeSourceTypeCode(AnomCode value) {
        if (value instanceof JaxbAnomCode) {
            JaxbAnomCode code = (JaxbAnomCode) value;
            source.getAuditSourceTypeCode().remove(code.getCodedValueType());
        }
        return this;
    }

    public String getEnterpriseSiteID() {
        return source.getAuditEnterpriseSiteID();
    }

    public AnomSource setEnterpriseSiteID(String value) {
        source.setAuditEnterpriseSiteID(value);
        return this;
    }

    public String getSourceID() {
        return source.getAuditSourceID();
    }

    public AnomSource setSourceID(String value) {
        source.setAuditSourceID(value);
        return this;
    }

    public AuditSourceIdentificationType getSource() {
        return source;
    }
}
