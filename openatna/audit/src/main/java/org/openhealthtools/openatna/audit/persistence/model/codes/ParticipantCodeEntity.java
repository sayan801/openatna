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
@DiscriminatorValue("ACTIVE_PARTICIPANT")
public class ParticipantCodeEntity extends CodeEntity {

    private static final long serialVersionUID = -1L;

    public ParticipantCodeEntity() {
        super(CodeType.ACTIVE_PARTICIPANT);
    }

    public ParticipantCodeEntity(String code) {
        super(CodeType.ACTIVE_PARTICIPANT, code);
    }

    public ParticipantCodeEntity(String code, String codeSystem) {
        super(CodeType.ACTIVE_PARTICIPANT, code, codeSystem);
    }

    public ParticipantCodeEntity(String code, String codeSystem, String codeSystemName) {
        super(CodeType.ACTIVE_PARTICIPANT, code, codeSystem, codeSystemName);
    }

    public ParticipantCodeEntity(String code, String codeSystem, String codeSystemName, String displayName) {
        super(CodeType.ACTIVE_PARTICIPANT, code, codeSystem, codeSystemName, displayName);
    }

    public ParticipantCodeEntity(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        super(CodeType.ACTIVE_PARTICIPANT, code, codeSystem, codeSystemName, displayName, originalText);
    }
}
