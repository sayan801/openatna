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

package org.openhealthtools.openatna.persistence.dao;

import java.util.HashMap;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 6, 2009: 6:03:28 PM
 * @date $Date:$ modified by $Author:$
 */

public class PersistencePolicies extends HashMap<String, Object> {


    public static final String ALLOW_NEW_CODES = "allowNewCodes";
    public static final String ALLOW_NEW_NETWORK_POINTS = "allowNewNetworkAccessPoints";
    public static final String ALLOW_NEW_PARTICIPANTS = "allowNewParticipants";
    public static final String ALLOW_NEW_SOURCES = "allowNewSources";
    public static final String ALLOW_NEW_OBJECTS = "allowNewObjects";
    public static final String ALLOW_UNKNOWN_DETAIL_TYPES = "allowUnknownDetailTypes";
    public static final String ALLOW_MODIFY_MESSAGES = "allowModifyMessages";
    public static final String ERROR_ON_DUPLICATE_INSERT = "errorOnDuplicateInsert";


    public boolean isAllowNewCodes() {
        Object o = get(ALLOW_NEW_CODES);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowNewCodes(boolean allowNewCodes) {
        put(ALLOW_NEW_CODES, allowNewCodes);
    }

    public boolean isAllowNewNetworkAccessPoints() {
        Object o = get(ALLOW_NEW_NETWORK_POINTS);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowNewNetworkAccessPoints(boolean allowNewNetworkAccessPoints) {
        put(ALLOW_NEW_NETWORK_POINTS, allowNewNetworkAccessPoints);
    }

    public boolean isAllowNewParticipants() {
        Object o = get(ALLOW_NEW_PARTICIPANTS);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowNewParticipants(boolean allowNewParticipants) {
        put(ALLOW_NEW_PARTICIPANTS, allowNewParticipants);
    }

    public boolean isAllowNewSources() {
        Object o = get(ALLOW_NEW_SOURCES);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowNewSources(boolean allowNewSources) {
        put(ALLOW_NEW_SOURCES, allowNewSources);
    }

    public boolean isAllowNewObjects() {
        Object o = get(ALLOW_NEW_OBJECTS);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowNewObjects(boolean allowNewObjects) {
        put(ALLOW_NEW_OBJECTS, allowNewObjects);
    }

    public boolean isAllowUnknownDetailTypes() {
        Object o = get(ALLOW_UNKNOWN_DETAIL_TYPES);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowUnknownDetailTypes(boolean allowUnknownDetailTypes) {
        put(ALLOW_UNKNOWN_DETAIL_TYPES, allowUnknownDetailTypes);
    }

    public boolean isAllowModifyMessages() {
        Object o = get(ALLOW_MODIFY_MESSAGES);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setAllowModifyMessages(boolean allowModifyMessages) {
        put(ALLOW_MODIFY_MESSAGES, allowModifyMessages);
    }

    public boolean isErrorOnDuplicateInsert() {
        Object o = get(ERROR_ON_DUPLICATE_INSERT);
        return o != null && o.equals(Boolean.TRUE);
    }

    public void setErrorOnDuplicateInsert(boolean errorOnDuplicateInsert) {
        put(ERROR_ON_DUPLICATE_INSERT, errorOnDuplicateInsert);
    }

}
