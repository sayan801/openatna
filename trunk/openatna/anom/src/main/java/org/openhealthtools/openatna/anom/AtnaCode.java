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

package org.openhealthtools.openatna.anom;

import java.io.Serializable;

/**
 * Immutable Coded Value
 *
 * @author Andrew Harrison
 * @author Toby Sarver
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public class AtnaCode implements Serializable {

    private static final long serialVersionUID = -6551206900480914431L;

    protected String code;
    protected String codeSystem;
    protected String codeSystemName;
    protected String displayName;
    protected String originalText;

    /**
     * @param code
     * @param codeSystem
     * @param codeSystemName
     * @param displayName
     * @param originalText
     */
    public AtnaCode(String code, String codeSystem,
                    String codeSystemName, String displayName, String originalText) {
        super();
        this.code = code;
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.displayName = displayName;
        this.originalText = originalText;
    }

    public String getCode() {
        return code;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getOriginalText() {
        return originalText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtnaCode)) return false;

        AtnaCode atnaCode = (AtnaCode) o;

        if (code != null ? !code.equals(atnaCode.code) : atnaCode.code != null) return false;
        if (codeSystem != null ? !codeSystem.equals(atnaCode.codeSystem) : atnaCode.codeSystem != null) return false;
        if (codeSystemName != null ? !codeSystemName.equals(atnaCode.codeSystemName) : atnaCode.codeSystemName != null) return false;
        if (displayName != null ? !displayName.equals(atnaCode.displayName) : atnaCode.displayName != null) return false;
        if (originalText != null ? !originalText.equals(atnaCode.originalText) : atnaCode.originalText != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (codeSystem != null ? codeSystem.hashCode() : 0);
        result = 31 * result + (codeSystemName != null ? codeSystemName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (originalText != null ? originalText.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" code=").append(getCode())
                .append(" code system=").append(getCodeSystem())
                .append(" code system name=").append(getCodeSystemName())
                .append(" display name=").append(getDisplayName())
                .append(" original text=").append(getOriginalText())
                .append("]")
                .toString();
    }
}
