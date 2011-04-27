/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.audit;

import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.dao.EntityDao;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.dao.NetworkAccessPointDao;
import org.openhealthtools.openatna.audit.persistence.dao.ObjectDao;
import org.openhealthtools.openatna.audit.persistence.dao.ParticipantDao;
import org.openhealthtools.openatna.audit.persistence.dao.ProvisionalDao;
import org.openhealthtools.openatna.audit.persistence.dao.QueryDao;
import org.openhealthtools.openatna.audit.persistence.dao.SourceDao;
import org.openhealthtools.openatna.audit.service.AuditService;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.SpringFacade;
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
    private static AtnaFactory instance;
    private static String openatnaProperties = null;


    private AtnaFactory(final ApplicationContext context) {
        this.context = context;
    }

    private Object getComponent(final String value) {
        return context.getBean(value);
    }

    /**
     * if called before any bean getter methods, this allows the factory
     * to be inialized by an arbitrary Application Context.
     * Of course, this context must contain the beans defined by OpenATNA.
     *
     * @param context
     */
    public static synchronized void initialize(ApplicationContext context) {
        if (context == null) {
    		try {
    			SpringFacade.loadSpringConfig( new String[]{"classpath*:/*Context.xml"} );
    			context = SpringFacade.getApplicationContext();
    		}catch(ConfigurationException e){
    			e.printStackTrace();
                throw new RuntimeException("FATAL: Could not create Spring Application Context.", e);
    		}
        }
        instance = new AtnaFactory(context);
    }

    public static synchronized Object getBean(final String id) {
        if (instance == null) {
            initialize(null);
        }
        return instance.getComponent(id);
    }

    public static void setPropertiesLocation(String location) {
        openatnaProperties = location;
    }

    public static String getPropertiesLocation() {
        return openatnaProperties;
    }

    public static CodeDao codeDao() {
        return (CodeDao) getBean("codeDao");
    }

    public static ParticipantDao participantDao() {
        return (ParticipantDao) getBean("participantDao");
    }

    public static NetworkAccessPointDao networkAccessPointDao() {
        return (NetworkAccessPointDao) getBean("networkAccessPointDao");
    }

    public static MessageDao messageDao() {
        return (MessageDao) getBean("messageDao");
    }

    public static SourceDao sourceDao() {
        return (SourceDao) getBean("sourceDao");
    }

    public static ObjectDao objectDao() {
        return (ObjectDao) getBean("objectDao");
    }

    public static EntityDao entityDao() {
        return (EntityDao) getBean("entityDao");
    }

    public static AuditService auditService() {
        return (AuditService) getBean("auditService");
    }

    public static ErrorDao errorDao() {
        return (ErrorDao) getBean("errorDao");
    }

    public static ProvisionalDao provisionalDao() {
        return (ProvisionalDao) getBean("provisionalDao");
    }

    public static QueryDao queryDao() {
        return (QueryDao) getBean("queryDao");
    }

}
