/*
 * Copyright (c) 2010 University of Cardiff and others.
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

package org.openhealthtools.openatna.syslog.bsd;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.SyslogMessageFactory;

/**
 * Reads in data and creates BSD style syslog messages
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 11:48:37 AM
 * @date $Date:$ modified by $Author:$
 */

public class BsdMessageFactory extends SyslogMessageFactory {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.syslog.bsd.BsdMessageFactory");


    private static SimpleDateFormat format = new SimpleDateFormat("MMM d HH:mm:ss");
    private static SimpleDateFormat singleDateFormat = new SimpleDateFormat("MMM  d HH:mm:ss");

    public static String createDate(Date date) {
        Calendar c = new GregorianCalendar(TimeZone.getDefault());
        c.setTime(date);
        if (c.get(Calendar.DATE) > 9) {
            return format.format(date);
        } else {
            return singleDateFormat.format(date);
        }
    }

    public static Date formatDate(String date) throws Exception {
        return format.parse(date);
    }

    /**
     * reads the priority value and positions the stream at the space after greater than sign.
     * This reads up to 5 characters to read the priority and the following space.
     *
     * @param in
     * @return
     * @throws org.openhealthtools.openatna.syslog.SyslogException
     *
     */
    private int readPriority(InputStream in) throws SyslogException {
        try {
            int max = 5;
            int count = 0;
            String pri = "";
            boolean afterOpen = false;
            while (count < max) {
                char c = (char) in.read();
                count++;
                switch (c) {
                    case '<':
                        afterOpen = true;
                        break;
                    case '>':
                        int priority = Integer.parseInt(pri);
                        if (!afterOpen || priority < 0 || priority > 191) {
                            throw new SyslogException("syntax error");
                        }

                        return priority;
                    default:
                        if (afterOpen) {
                            pri += c;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            throw new SyslogException(e);
        }
        throw new SyslogException("too many characters");
    }


    public SyslogMessage read(InputStream in) throws SyslogException {
        try {
            PushbackInputStream pin = new PushbackInputStream(in, 5);
            int priority = readPriority(pin);
            int facility;
            int severity;
            byte c;
            int spaces = 4;
            int count = 0;
            boolean spaceBefore = false;
            ByteBuffer buff = ByteBuffer.wrap(new byte[256]);

            String timestamp;
            String month = null;
            String date = null;
            String time = null;
            String host = "";
            int max = 256;
            int curr = 0;

            while (count < spaces && curr < max) {
                c = (byte) pin.read();
                curr++;
                if (c == ' ') {
                    if (!spaceBefore) {
                        count++;
                        String currHeader = new String(buff.array(), 0, buff.position(), Constants.ENC_UTF8);
                        buff.clear();
                        switch (count) {
                            case 1:
                                month = currHeader;
                                break;
                            case 2:
                                date = currHeader;
                                break;
                            case 3:
                                time = currHeader;
                                break;
                            case 4:
                                host = currHeader;
                                break;

                        }
                    }
                    spaceBefore = true;

                } else {
                    spaceBefore = false;
                    buff.put(c);
                }
            }
            if (month == null || date == null || time == null) {
                timestamp = createDate(new Date());
            } else {
                String gap = " ";
                if (date.length() == 1) {
                    gap = "  ";
                }
                timestamp = (month + gap + date + " " + time);
                try {
                    formatDate(timestamp);
                } catch (Exception e) {
                    timestamp = createDate(new Date());
                }
            }
            String tag = null;
            int tagLen = 32;
            buff.clear();
            for (int i = 0; i < tagLen; i++) {
                c = (byte) pin.read();
                curr++;
                if (!Character.isLetterOrDigit((char) (c & 0xff))) {
                    pin.unread(c);
                    break;
                }
                buff.put(c);
            }
            if (buff.position() > 0) {
                tag = new String(buff.array(), 0, buff.position(), Constants.ENC_UTF8);
            }

            LogMessage logMessage = getLogMessage(tag);
            String encoding = readBom(pin, logMessage.getExpectedEncoding());
            logMessage.read(pin, encoding);
            facility = priority / 8;
            severity = priority % 8;
            return new BsdMessage(facility, severity, timestamp, host, logMessage, tag);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SyslogException(e);
        }
    }
}
