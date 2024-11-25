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
@DiscriminatorValue("PARTICIPANT_OBJECT_ID_TYPE")
public class ObjectIdTypeCodeEntity extends CodeEntity {

    private static final long serialVersionUID = -1L;

    public ObjectIdTypeCodeEntity() {
        super(CodeType.PARTICIPANT_OBJECT_ID_TYPE);
    }

    public ObjectIdTypeCodeEntity(String code) {
        super(CodeType.PARTICIPANT_OBJECT_ID_TYPE, code);
    }

    public ObjectIdTypeCodeEntity(String code, String codeSystem) {
        super(CodeType.PARTICIPANT_OBJECT_ID_TYPE, code, codeSystem);
    }

    public ObjectIdTypeCodeEntity(String code, String codeSystem, String codeSystemName) {
        super(CodeType.PARTICIPANT_OBJECT_ID_TYPE, code, codeSystem, codeSystemName);
    }

    public ObjectIdTypeCodeEntity(String code, String codeSystem, String codeSystemName, String displayName) {
        super(CodeType.PARTICIPANT_OBJECT_ID_TYPE, code, codeSystem, codeSystemName, displayName);
    }

    public ObjectIdTypeCodeEntity(String code, String codeSystem, String codeSystemName,
                                  String displayName, String originalText) {
        super(CodeType.PARTICIPANT_OBJECT_ID_TYPE, code, codeSystem, codeSystemName, displayName, originalText);
    }
}
