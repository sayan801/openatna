package org.openhealthtools.openatna.audit.persistence.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "errors")
public class ErrorEntity extends PersistentEntity {

    private static final long serialVersionUID = 4878120253966610977L;
    private Long id;
    private Integer version;
    private Date errorTimestamp;
    private String errorMessage = "";
    private String sourceIp = "";
    private byte[] stackTrace = new byte[0];
    private byte[] payload = new byte[0];


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

    @Temporal(TemporalType.TIMESTAMP)
    public Date getErrorTimestamp() {
        return errorTimestamp;
    }

    public void setErrorTimestamp(Date errorTimestamp) {
        this.errorTimestamp = errorTimestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Lob
    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    @Lob
    public byte[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(byte[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ErrorEntity that = (ErrorEntity) o;

        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) {
            return false;
        }
        if (errorTimestamp != null ? !errorTimestamp.equals(that.errorTimestamp) : that.errorTimestamp != null) {
            return false;
        }
        if (!Arrays.equals(payload, that.payload)) {
            return false;
        }
        if (sourceIp != null ? !sourceIp.equals(that.sourceIp) : that.sourceIp != null) {
            return false;
        }
        return stackTrace != null ? Arrays.equals(stackTrace, that.stackTrace) : that.stackTrace == null;
    }

    @Override
    public int hashCode() {
        int result = errorTimestamp != null ? errorTimestamp.hashCode() : 0;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (sourceIp != null ? sourceIp.hashCode() : 0);
        result = 31 * result + (stackTrace != null ? Arrays.hashCode(stackTrace) : 0);
        result = 31 * result + (payload != null ? Arrays.hashCode(payload) : 0);
        return result;
    }

    public String toString() {
        return getClass().getName() +
                "[" +
                "id=" +
                getId() +
                ", version=" +
                getVersion() +
                ", error message=" +
                getErrorMessage() +
                ", payload=" +
                new String(getPayload()) +
                "]";
    }
}
