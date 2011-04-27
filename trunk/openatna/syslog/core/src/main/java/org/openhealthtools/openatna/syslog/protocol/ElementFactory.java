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

package org.openhealthtools.openatna.syslog.protocol;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openhealthtools.openatna.syslog.SyslogException;

/**
 * creates SEs of the defined types for convenience.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 15, 2009: 3:24:41 PM
 * @date $Date:$ modified by $Author:$
 */

public class ElementFactory {

    public static StructuredElement newTimeQualityElement(boolean tzKnown, boolean synced, int syncAccuracy)
            throws SyslogException {
        String tz = tzKnown ? "1" : "0";
        String isSync = synced ? "1" : "0";
        String quality = synced ? null : Integer.toString(syncAccuracy);

        List<SdParam> els = new ArrayList<SdParam>();

        els.add(new SdParam(StructuredElement.TZ_KNOWN, tz));
        els.add(new SdParam(StructuredElement.IS_SYNCED, isSync));
        if (quality != null && syncAccuracy > -1) {
            els.add(new SdParam(StructuredElement.SYNC_ACCURACY, quality));
        }
        return new StructuredElement(StructuredElement.ianaIds[0], els);
    }

    public static StructuredElement newTimeQualityElement(boolean tzKnown, boolean synced) throws SyslogException {
        return newTimeQualityElement(tzKnown, synced, -1);
    }

    public static StructuredElement newOriginElement(String enterpriseId, String software, String version,
                                                     String... ips) throws SyslogException {
        List<SdParam> els = new ArrayList<SdParam>();
        if (enterpriseId != null) {
            els.add(new SdParam(StructuredElement.ENTERPRISE_ID, enterpriseId));
        }
        if (software != null) {
            els.add(new SdParam(StructuredElement.SOFTWARE, software));
        }
        if (version != null) {
            els.add(new SdParam(StructuredElement.SW_VERSION, version));
        }
        if (ips != null) {
            for (String ip : ips) {
                if (ip != null) {
                    els.add(new SdParam(StructuredElement.IP, ip));
                }
            }
        }
        return new StructuredElement(StructuredElement.ianaIds[1], els);
    }

    public static StructuredElement newOriginElement(String software, String version, String... ips) throws SyslogException {
        return newOriginElement(null, software, version, ips);
    }

    public static StructuredElement newOriginElement(String... ips) throws SyslogException {
        return newOriginElement(null, null, null, ips);
    }

    public static StructuredElement newOriginElement(String software, String version) throws SyslogException {
        return newOriginElement(null, software, version, (String[]) null);
    }

    public static StructuredElement newMetaElement(long sequenceId, long upTime, String language) throws SyslogException {
        List<SdParam> els = new ArrayList<SdParam>();
        els.add(new SdParam(StructuredElement.SEQUENCE_ID, Long.toString(sequenceId)));
        if (upTime > -1) {
            els.add(new SdParam(StructuredElement.SYS_UPTIME, Long.toString(upTime)));
        }
        if (language == null) {
            language = Locale.getDefault().getLanguage();
        }
        els.add(new SdParam(StructuredElement.LANGUAGE, language));
        return new StructuredElement(StructuredElement.ianaIds[2], els);
    }

    public static StructuredElement newMetaElement(long sequenceId, long upTime) throws SyslogException {
        return newMetaElement(sequenceId, upTime, null);
    }

    public static StructuredElement newMetaElement(long sequenceId) throws SyslogException {
        return newMetaElement(sequenceId, -1, null);
    }
}
