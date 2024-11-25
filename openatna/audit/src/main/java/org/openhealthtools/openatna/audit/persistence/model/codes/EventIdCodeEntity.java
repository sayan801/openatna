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
@DiscriminatorValue("EVENT_ID")
public class EventIdCodeEntity extends CodeEntity {

    private static final long serialVersionUID = -1L;

    public EventIdCodeEntity() {
        super(CodeType.EVENT_ID);
    }

    public EventIdCodeEntity(String code) {
        super(CodeType.EVENT_ID, code);
    }

    public EventIdCodeEntity(String code, String codeSystem) {
        super(CodeType.EVENT_ID, code, codeSystem);
    }

    public EventIdCodeEntity(String code, String codeSystem, String codeSystemName) {
        super(CodeType.EVENT_ID, code, codeSystem, codeSystemName);
    }

    public EventIdCodeEntity(String code, String codeSystem, String codeSystemName, String displayName) {
        super(CodeType.EVENT_ID, code, codeSystem, codeSystemName, displayName);
    }

    public EventIdCodeEntity(String code, String codeSystem, String codeSystemName,
                             String displayName, String originalText) {
        super(CodeType.EVENT_ID, code, codeSystem, codeSystemName, displayName, originalText);
    }
}
