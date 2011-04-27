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

package org.openhealthtools.openatna.anom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Audit Source
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public class AtnaSource implements Serializable {

    private static final long serialVersionUID = -5837184418641690005L;

    private Set<AtnaCode> sourceTypeCodes = new HashSet<AtnaCode>();
    private String sourceId;
    private String enterpriseSiteId;

    public AtnaSource(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<AtnaCode> getSourceTypeCodes() {
        return new ArrayList<AtnaCode>(sourceTypeCodes);
    }

    public AtnaSource addSourceTypeCode(AtnaCode value) {
        sourceTypeCodes.add(value);
        return this;
    }

    public AtnaSource removeSourceTypeCode(AtnaCode value) {
        sourceTypeCodes.remove(value);
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public AtnaSource setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getEnterpriseSiteId() {
        return enterpriseSiteId;
    }

    public AtnaSource setEnterpriseSiteId(String enterpriseSiteId) {
        this.enterpriseSiteId = enterpriseSiteId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaSource)) {
            return false;
        }

        AtnaSource that = (AtnaSource) o;

        if (enterpriseSiteId != null ? !enterpriseSiteId.equals(that.enterpriseSiteId) : that.enterpriseSiteId != null) {
            return false;
        }
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) {
            return false;
        }
        if (sourceTypeCodes != null ? !sourceTypeCodes.equals(that.sourceTypeCodes) : that.sourceTypeCodes != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceTypeCodes != null ? sourceTypeCodes.hashCode() : 0;
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (enterpriseSiteId != null ? enterpriseSiteId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder().append("[")
                .append(getClass().getName())
                .append(" source id=")
                .append(getSourceId())
                .append(" enterprise site id=")
                .append(getEnterpriseSiteId())
                .append(" source type codes=")
                .append(getSourceTypeCodes())
                .append("]")
                .toString();
    }
}
