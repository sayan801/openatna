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

package org.openhealthtools.openatna.audit.persistence.dao.hibernate;

import org.openhealthtools.openatna.audit.persistence.dao.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 8, 2009: 6:14:56 PM
 * @date $Date:$ modified by $Author:$
 */

public class SpringDaoFactory implements DaoFactory {

    private static SpringDaoFactory factory = new SpringDaoFactory();

    public static SpringDaoFactory getFactory() {
        return factory;
    }

    private ApplicationContext context;

    public SpringDaoFactory() {
        context = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext-dao.xml"});
        if (context == null) {
            throw new RuntimeException("FATAL: Could not create Spring Application Context.");
        }
    }

    public CodeDao codeDao() {
        return (CodeDao) context.getBean("codeDao");
    }

    public ParticipantDao participantDao() {
        return (ParticipantDao) context.getBean("participantDao");
    }

    public NetworkAccessPointDao networkAccessPointDao() {
        return (NetworkAccessPointDao) context.getBean("networkAccessPointDao");
    }

    public MessageDao messageDao() {
        return (MessageDao) context.getBean("messageDao");
    }

    public SourceDao sourceDao() {
        return (SourceDao) context.getBean("sourceDao");
    }

    public ObjectDao objectDao() {
        return (ObjectDao) context.getBean("objectDao");
    }

    public EntityDao entityDao() {
        return (EntityDao) context.getBean("entityDao");
    }

}
