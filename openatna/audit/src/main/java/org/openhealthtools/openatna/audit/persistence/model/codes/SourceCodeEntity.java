package org.openhealthtools.openatna.audit.persistence.model.codes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
@Entity
@DiscriminatorValue("AUDIT_SOURCE")
public class SourceCodeEntity extends CodeEntity {

    private static final long serialVersionUID = -1L;

    public SourceCodeEntity() {
        super(CodeType.AUDIT_SOURCE);
    }

    public SourceCodeEntity(String code) {
        super(CodeType.AUDIT_SOURCE, code);
    }

    public SourceCodeEntity(String code, String codeSystem) {
        super(CodeType.AUDIT_SOURCE, code, codeSystem);
    }

    public SourceCodeEntity(String code, String codeSystem, String codeSystemName) {
        super(CodeType.AUDIT_SOURCE, code, codeSystem, codeSystemName);
    }

    public SourceCodeEntity(String code, String codeSystem, String codeSystemName, String displayName) {
        super(CodeType.AUDIT_SOURCE, code, codeSystem, codeSystemName, displayName);
    }

    public SourceCodeEntity(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        super(CodeType.AUDIT_SOURCE, code, codeSystem, codeSystemName, displayName, originalText);
    }
}
