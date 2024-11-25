package org.openhealthtools.openatna.web;

import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class StringifiedError {

    private String stackTrace = "";
    private String content = "";
    private String ip = "";
    private final Date time;
    private String message = "";
    private final Long id;

    public StringifiedError(ErrorEntity errorEntity) {
        this.id = errorEntity.getId();
        if (errorEntity.getStackTrace() != null) {
            this.stackTrace = new String(errorEntity.getStackTrace());
        }
        if (errorEntity.getPayload() != null) {
            this.content = new String(errorEntity.getPayload(), StandardCharsets.UTF_8);
            this.content = this.content.replaceAll("<", "&lt;");
        }
        if (errorEntity.getSourceIp() != null) {
            this.ip = errorEntity.getSourceIp();
        }
        this.time = errorEntity.getErrorTimestamp();
        if (errorEntity.getErrorMessage() != null) {
            this.message = errorEntity.getErrorMessage();
        }
    }

    public Long getId() {
        return id;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getContent() {
        return content;
    }

    public String getIp() {
        return ip;
    }

    public Date getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
