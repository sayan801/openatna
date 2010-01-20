/*
 * Copyright (c) 2009-2010 University of Cardiff and others.
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

package org.openhealthtools.openatna.audit.persistence.model.codes;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Version;
import org.openhealthtools.openatna.audit.persistence.model.PersistentEntity;


/**
 * Entity for coded value types.
 * NOTE: equality between codes is a slightly fuzzy business.
 * Codes need to be unique, but both the code system and code system name are optional.
 * This makes determining equality slightly tricky. Added to this, two null values in the DB
 * do not equate, so two Codes with the same code, and null for the system and system name are not equal.
 */


@Entity
@Table(name = "codes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "codetype",
        discriminatorType = DiscriminatorType.STRING
)
public abstract class CodeEntity extends PersistentEntity {

    public static enum CodeType {
        ACTIVE_PARTICIPANT,
        AUDIT_SOURCE,
        EVENT_ID,
        EVENT_TYPE,
        PARTICIPANT_OBJECT_ID_TYPE
    }

    private Long id;
    private Integer version;

    private CodeType type;

    private String code;
    private String codeSystem;
    private String codeSystemName;
    private String displayName;
    private String originalText;

    private CodeEntity() {
    }

    protected CodeEntity(CodeType type) {
        this.type = type;
    }

    protected CodeEntity(CodeType type, String code) {
        this.type = type;
        this.code = code;
    }

    protected CodeEntity(CodeType type, String code, String codeSystem) {
        this.type = type;
        this.code = code;
        this.codeSystem = codeSystem;
    }

    protected CodeEntity(CodeType type, String code, String codeSystem, String codeSystemName) {
        this.type = type;
        this.code = code;
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
    }

    protected CodeEntity(CodeType type, String code, String codeSystem, String codeSystemName,
                         String displayName) {
        this.type = type;
        this.code = code;
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.displayName = displayName;
    }

    protected CodeEntity(CodeType type, String code, String codeSystem, String codeSystemName,
                         String displayName, String originalText) {
        this.type = type;
        this.code = code;
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.displayName = displayName;
        this.originalText = originalText;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the originalText property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getOriginalText() {
        return originalText;
    }

    /**
     * Sets the value of the originalText property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOriginalText(String value) {
        this.originalText = value;
    }

    /**
     * Gets the value of the codeSystem property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * Sets the value of the codeSystem property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodeSystem(String value) {
        this.codeSystem = value;
    }

    /**
     * Gets the value of the codeSystemName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * Sets the value of the codeSystemName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodeSystemName(String value) {
        this.codeSystemName = value;
    }

    public CodeType getType() {
        return type;
    }

    public void setType(CodeType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeEntity)) {
            return false;
        }
        CodeEntity that = (CodeEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) {
            return false;
        }
        if (codeSystem != null ? !codeSystem.equals(that.codeSystem) : that.codeSystem != null) {
            return false;
        }
        if (codeSystemName != null ? !codeSystemName.equals(that.codeSystemName) : that.codeSystemName != null) {
            return false;
        }
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) {
            return false;
        }
        if (originalText != null ? !originalText.equals(that.originalText) : that.originalText != null) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (codeSystem != null ? codeSystem.hashCode() : 0);
        result = 31 * result + (codeSystemName != null ? codeSystemName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (originalText != null ? originalText.hashCode() : 0);
        return result;
    }


    public String toString() {
        return new StringBuilder("[").append(getClass().getName())
                .append(" id=")
                .append(getId())
                .append(", version=")
                .append(getVersion())
                .append(", type=")
                .append(getType())
                .append(", code=")
                .append(getCode())
                .append(", code system=")
                .append(getCodeSystem())
                .append(", code system name=")
                .append(getCodeSystemName())
                .append(", original text=")
                .append(getOriginalText())
                .append(", display name=")
                .append(getDisplayName())
                .append("]")
                .toString();
    }


}
