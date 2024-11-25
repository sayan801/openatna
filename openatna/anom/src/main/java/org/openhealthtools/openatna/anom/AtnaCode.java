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

package org.openhealthtools.openatna.anom;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Immutable Coded Value
 */
public class AtnaCode implements Serializable {

    public static final String EVENT_ID = "EventId";
    public static final String EVENT_TYPE = "EventType";
    public static final String OBJECT_ID_TYPE = "ObjectIdType";
    public static final String SOURCE_TYPE = "SourceType";
    public static final String PARTICIPANT_ROLE_TYPE = "ParticipantRoleType";
    private static final long serialVersionUID = -6551206900480914431L;
    private final String code;
    private final String codeSystem;
    private final String codeSystemName;
    private final String displayName;
    private final String originalText;
    private final String codeType;

    /**
     * @param codeType
     * @param code
     * @param codeSystem
     * @param codeSystemName
     * @param displayName
     * @param originalText
     */
    public AtnaCode(String codeType, String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        this.codeType = codeType;
        this.code = code;
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.displayName = displayName;
        this.originalText = originalText;
    }

    public static AtnaCode eventIdCode(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        return new AtnaCode(EVENT_ID, code, codeSystem, codeSystemName, displayName, originalText);
    }

    public static AtnaCode eventTypeCode(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        return new AtnaCode(EVENT_TYPE, code, codeSystem, codeSystemName, displayName, originalText);
    }

    public static AtnaCode objectIdTypeCode(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        return new AtnaCode(OBJECT_ID_TYPE, code, codeSystem, codeSystemName, displayName, originalText);
    }

    public static AtnaCode sourceTypeCode(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        return new AtnaCode(SOURCE_TYPE, code, codeSystem, codeSystemName, displayName, originalText);
    }

    public static AtnaCode participantRoleTypeCode(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        return new AtnaCode(PARTICIPANT_ROLE_TYPE, code, codeSystem, codeSystemName, displayName, originalText);
    }

    public String getCodeType() {
        return codeType;
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtnaCode)) {
            return false;
        }

        AtnaCode atnaCode = (AtnaCode) o;

        if (!code.equals(atnaCode.code)) {
            return false;
        }
        if (codeSystem != null ? !codeSystem.equals(atnaCode.codeSystem) : atnaCode.codeSystem != null) {
            return false;
        }
        if (codeSystemName != null ? !codeSystemName.equals(atnaCode.codeSystemName) : atnaCode.codeSystemName != null) {
            return false;
        }
        if (!codeType.equals(atnaCode.codeType)) {
            return false;
        }
        if (displayName != null ? !displayName.equals(atnaCode.displayName) : atnaCode.displayName != null) {
            return false;
        }
        return originalText != null ? originalText.equals(atnaCode.originalText) : atnaCode.originalText == null;
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (codeSystem != null ? codeSystem.hashCode() : 0);
        result = 31 * result + (codeSystemName != null ? codeSystemName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (originalText != null ? originalText.hashCode() : 0);
        result = 31 * result + codeType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("codeSystem", codeSystem)
                .append("codeSystemName", codeSystemName)
                .append("displayName", displayName)
                .append("originalText", originalText)
                .append("codeType", codeType)
                .toString();
    }
}
