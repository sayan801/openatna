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
 */
public class DateUtil {

    private DateUtil() {
    }

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
        Date d = df.parse(datetime);
        Calendar date = new GregorianCalendar();
        date.setTime(d);
        return date;
    }
}
