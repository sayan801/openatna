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

package org.openhealthtools.openatna.syslog;

/**
 * This exception is thrown if there is a problem reading a syslog exception. There
 * can be two causes for this - usually either an IOException, which this wraps,
 * or a syslog syntax exception.
 * This exception can also contain the bytes that lead to the exception, but this is not
 * guarannteed. The only thing that is gurannteed, is that the byte[] will not be null,
 * i.e., if no bytes are passed into the constructor, an empty array is used.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 19, 2009: 11:14:40 AM
 * @date $Date:$ modified by $Author:$
 */
public class SyslogException extends Exception {

    private byte[] bytes;

    public SyslogException(String s) {
        this(s, new byte[0]);
    }

    public SyslogException(String s, Throwable throwable) {
        this(s, throwable, new byte[0]);
    }

    public SyslogException(Throwable throwable) {
        this(throwable, new byte[0]);
    }

    public SyslogException(String s, byte[] bytes) {
        super(s);
        this.bytes = bytes;
    }

    public SyslogException(String s, Throwable throwable, byte[] bytes) {
        super(s, throwable);
        this.bytes = bytes;
    }

    public SyslogException(Throwable throwable, byte[] bytes) {
        super(throwable);
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
