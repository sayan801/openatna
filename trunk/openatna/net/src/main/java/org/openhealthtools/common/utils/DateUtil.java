/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The date util class
 *
 * @author Wenzhi Li
 * @version 2.0, Jul 3, 2006
 */
public class DateUtil {

    /**
     * Parses a datetime string to its corresponding calendar value
     *
     * @param datetime the string value of the date time
     * @param format   the format of the string datetime
     * @return the Calendar object
     * @throws ParseException if there is a parsing error.
     */
    public static Calendar parseCalendar(String datetime, String format) throws ParseException {
        if (!StringUtil.goodString(datetime)) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date d = null;
        d = df.parse(datetime);
        Calendar date = new GregorianCalendar();
        date.setTime(d);
        return date;
    }


}
