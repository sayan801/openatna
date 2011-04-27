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

package org.openhealthtools.openatna.syslog;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * reads and writes the application specific message.
 * It is the responsibility of the LogMessage to include (or not)
 * a UTF BOM, if writing 5424 messages. SyslogMessageFactory contains write&ltUTF-encoding>Bom(OutputStream) methods that can
 * be invoked before writing the message to the stream, if a UTF BOM is desired.
 * <p/>
 * The SyslogMessage tries to determine the encoding of the arriving stream and defaults
 * to the expected encoding of the LogMessage
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Aug 15, 2009: 11:47:13 AM
 * @date $Date:$ modified by $Author:$
 */
public interface LogMessage<M extends Object> {

    public String getExpectedEncoding();

    public void read(InputStream in, String encoding) throws SyslogException;

    public void write(OutputStream out) throws SyslogException;

    public M getMessageObject();
}
