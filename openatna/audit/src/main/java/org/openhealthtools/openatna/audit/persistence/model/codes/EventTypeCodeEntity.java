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
@DiscriminatorValue("EVENT_TYPE")
public class EventTypeCodeEntity extends CodeEntity {

    private static final long serialVersionUID = -1L;

    public EventTypeCodeEntity() {
        super(CodeType.EVENT_TYPE);
    }

    public EventTypeCodeEntity(String code) {
        super(CodeType.EVENT_TYPE, code);
    }

    public EventTypeCodeEntity(String code, String codeSystem) {
        super(CodeType.EVENT_TYPE, code, codeSystem);
    }

    public EventTypeCodeEntity(String code, String codeSystem, String codeSystemName) {
        super(CodeType.EVENT_TYPE, code, codeSystem, codeSystemName);
    }

    public EventTypeCodeEntity(String code, String codeSystem, String codeSystemName, String displayName) {
        super(CodeType.EVENT_TYPE, code, codeSystem, codeSystemName, displayName);
    }

    public EventTypeCodeEntity(String code, String codeSystem, String codeSystemName,
                               String displayName, String originalText) {
        super(CodeType.EVENT_TYPE, code, codeSystem, codeSystemName, displayName, originalText);

    }
}
