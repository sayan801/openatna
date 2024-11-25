package org.openhealthtools.openatna.audit.persistence.model.codes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openhealthtools.openatna.audit.persistence.model.PersistentEntity;

import javax.persistence.*;

/**
 * Entity for coded value types.
 * NOTE: equality between codes is a slightly fuzzy business.
 * Codes need to be unique, but both the code system and code system name are optional.
 * This makes determining equality slightly tricky. Added to this, two null values in the DB do not equate,
 * so two Codes with the same code, and null for the system and system name are not equal.
 */
@Entity
@Table(name = "codes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "codetype",
        discriminatorType = DiscriminatorType.STRING
)
public abstract class CodeEntity extends PersistentEntity {

    private Long id;
    private Integer version;
    private CodeType type;
    private String code;
    private String codeSystem;
    private String codeSystemName;
    private String displayName;
    private String originalText;

    protected CodeEntity() {
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * @return possible object is {@link String }
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is {@link String }
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return possible object is {@link String }
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is {@link String }
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the originalText property.
     *
     * @return possible object is {@link String }
     */
    public String getOriginalText() {
        return originalText;
    }

    /**
     * Sets the value of the originalText property.
     *
     * @param value allowed object is {@link String }
     */
    public void setOriginalText(String value) {
        this.originalText = value;
    }

    /**
     * Gets the value of the codeSystem property.
     *
     * @return possible object is {@link String }
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * Sets the value of the codeSystem property.
     *
     * @param value allowed object is {@link String }
     */
    public void setCodeSystem(String value) {
        this.codeSystem = value;
    }

    /**
     * Gets the value of the codeSystemName property.
     *
     * @return possible object is {@link String }
     */
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * Sets the value of the codeSystemName property.
     *
     * @param value allowed object is {@link String }
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
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CodeEntity that = (CodeEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(version, that.version)
                .append(type, that.type)
                .append(code, that.code)
                .append(codeSystem, that.codeSystem)
                .append(codeSystemName, that.codeSystemName)
                .append(displayName, that.displayName)
                .append(originalText, that.originalText)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(version)
                .append(type)
                .append(code)
                .append(codeSystem)
                .append(codeSystemName)
                .append(displayName)
                .append(originalText)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("version", version)
                .append("type", type)
                .append("code", code)
                .append("codeSystem", codeSystem)
                .append("codeSystemName", codeSystemName)
                .append("displayName", displayName)
                .append("originalText", originalText)
                .toString();
    }

    public enum CodeType {
        ACTIVE_PARTICIPANT,
        AUDIT_SOURCE,
        EVENT_ID,
        EVENT_TYPE,
        PARTICIPANT_OBJECT_ID_TYPE
    }
}
