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

package org.openhealthtools.openatna.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Feb 6, 2010: 7:11:49 PM
 */

public class StringifiedError {

    private String stackTrace = "";
    private String content = "";
    private String ip = "";
    private Date time;
    private String message = "";
    private Long id;


    public StringifiedError(ErrorEntity errorEntity) {
        this.id = errorEntity.getId();
        if (errorEntity.getStackTrace() != null) {
            this.stackTrace = new String(errorEntity.getStackTrace());
        }
        if (errorEntity.getPayload() != null) {
            try {
                this.content = new String(errorEntity.getPayload(), "UTF-8");
                this.content = this.content.replaceAll("<", "&lt;");
            } catch (UnsupportedEncodingException e) {

            }
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
