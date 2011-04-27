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

package org.openhealthtools.openatna.report;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.model.PersistentEntity;
import org.openhealthtools.openatna.audit.persistence.util.Base64;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 3, 2009: 10:01:41 PM
 * @date $Date:$ modified by $Author:$
 */

public class EntityDataSource implements JRDataSource {

    private Iterator<? extends PersistentEntity> it;
    private PersistentEntity curr;

    public EntityDataSource(Collection<? extends PersistentEntity> codes) throws AtnaPersistenceException {
        this.it = codes.iterator();
    }

    public EntityDataSource(PersistentEntity code) throws AtnaPersistenceException {
        Set<PersistentEntity> set = new HashSet<PersistentEntity>();
        set.add(code);
        this.it = set.iterator();
        this.curr = code;
    }

    public boolean next() throws JRException {
        if (it.hasNext()) {
            curr = it.next();
            return true;
        }
        return false;
    }

    public Object getFieldValue(JRField jrField) throws JRException {
        if (curr == null) {
            return null;
        }
        String field = jrField.getName();
        if (field.equals("entity")) {
            return curr;
        }
        String method = field.substring(0, 1).toUpperCase() + field.substring(1);
        Method ms = null;
        try {
            ms = curr.getClass().getMethod("get" + method, new Class[0]);
        } catch (NoSuchMethodException e) {
            try {
                ms = curr.getClass().getMethod("is" + method, new Class[0]);
            } catch (NoSuchMethodException e1) {
            }
        }
        if (ms != null) {
            try {
                Object ret = ms.invoke(curr, new Object[0]);
                if (ret instanceof Enum) {
                    return ret.toString();
                } else if (ret instanceof byte[]) {
                    try {
                        return Base64.decodeString(new String((byte[]) ret, "UTF-8"));
                    } catch (Exception e) {
                        try {
                            return new String((byte[]) ret, "UTF-8");
                        } catch (UnsupportedEncodingException e1) {

                        }
                    }
                }
                return ret;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}