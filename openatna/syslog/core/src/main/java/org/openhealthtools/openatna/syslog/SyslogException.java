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
 * This exception is thrown if there is a problem reading a syslog message. There
 * can be two causes for this - usually either an IOException, which this wraps,
 * or a syslog syntax exception.
 * <p/>
 * NOTE: If an IOException is thrown, then one reason could be an SSLHandshakeException or similar
 * SSL error.
 * Implementations should look for this, because it means an un-authorized access attempt has possibly taken
 * place. This needs to be audited. The SyslogError code is provided to allow implementations
 * to signal this possiblity, along with general IO errors, and syslog syntax errors.
 * <p/>
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

    public static enum SyslogError {
        UNDEFINED,
        SYNTAX_ERROR,
        IO_ERROR,
        SSL_ERROR
    }

    private byte[] bytes;
    private SyslogError error;

    public SyslogException(String s) {
        this(s, new byte[0], SyslogError.UNDEFINED);
    }

    public SyslogException(String s, Throwable throwable) {
        this(s, throwable, new byte[0], SyslogError.UNDEFINED);
    }

    public SyslogException(Throwable throwable) {
        this(throwable, new byte[0], SyslogError.UNDEFINED);
    }

    public SyslogException(String s, byte[] bytes) {
        this(s, bytes, SyslogError.UNDEFINED);
    }

    public SyslogException(String s, Throwable throwable, byte[] bytes) {
        this(s, throwable, bytes, SyslogError.UNDEFINED);
    }

    public SyslogException(Throwable throwable, byte[] bytes) {
        this(throwable, bytes, SyslogError.UNDEFINED);
    }

    public SyslogException(String s, byte[] bytes, SyslogError error) {
        super(s);
        this.bytes = bytes;
        this.error = error;
    }

    public SyslogException(String s, Throwable throwable, byte[] bytes, SyslogError error) {
        super(s, throwable);
        this.bytes = bytes;
        this.error = error;
    }

    public SyslogException(Throwable throwable, byte[] bytes, SyslogError error) {
        super(throwable);
        this.bytes = bytes;
        this.error = error;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public SyslogError getError() {
        return error;
    }
}
