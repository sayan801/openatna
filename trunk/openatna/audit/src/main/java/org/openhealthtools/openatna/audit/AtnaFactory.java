/*
 * Copyright (c) 2009 University of Cardiff and others.
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

package org.openhealthtools.openatna.audit;

import org.openhealthtools.openatna.audit.persistence.dao.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Nov 1, 2009: 10:31:59 AM
 * @date $Date:$ modified by $Author:$
 */

public class AtnaFactory {

    private ApplicationContext context;
    private static AtnaFactory instance = new AtnaFactory();

    private AtnaFactory() {
        context = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext.xml"});
        if (context == null) {
            throw new RuntimeException("FATAL: Could not create Spring Application Context.");
        }
    }

    private Object getBean(String value) {
        return context.getBean(value);
    }

    public static CodeDao codeDao() {
        return (CodeDao) instance.getBean("codeDao");
    }

    public static ParticipantDao participantDao() {
        return (ParticipantDao) instance.getBean("participantDao");
    }

    public static NetworkAccessPointDao networkAccessPointDao() {
        return (NetworkAccessPointDao) instance.getBean("networkAccessPointDao");
    }

    public static MessageDao messageDao() {
        return (MessageDao) instance.getBean("messageDao");
    }

    public static SourceDao sourceDao() {
        return (SourceDao) instance.getBean("sourceDao");
    }

    public static ObjectDao objectDao() {
        return (ObjectDao) instance.getBean("objectDao");
    }

    public static EntityDao entityDao() {
        return (EntityDao) instance.getBean("entityDao");
    }

    public static AuditService auditService() {
        return (AuditService) instance.getBean("auditService");
    }


}
