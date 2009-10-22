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

package org.openhealthtools.openatna.audit.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaIOFactory;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.audit.log.AtnaErrorLogger;
import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.LogMessage;

/**
 * A wrapper for an AtnaIOFactory that ties in with Syslog.
 * Designed for subclasses to specify a Factory meaning the log message
 * can have a default constructor, and one taking just an AtnaMessage:
 * <p/>
 * e.g.:
 * <p/>
 * class MyLogMessage extends AtnaLogMessage {
 * public MyLogMessage() {
 * super(new MyIOFactory());
 * }
 * }
 * <p/>
 * class MyLogMessage extends AtnaLogMessage {
 * public MyLogMessage(AtnaMEssage msg) {
 * super(msg, new MyIOFactory());
 * }
 * }
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 3:04:53 PM
 * @date $Date:$ modified by $Author:$
 */

public abstract class AtnaLogMessage implements LogMessage<AtnaMessage> {

    private AtnaMessage message;
    private AtnaIOFactory factory;

    public AtnaLogMessage(AtnaIOFactory factory) {
        this.factory = factory;
    }

    public AtnaLogMessage(AtnaMessage message, AtnaIOFactory factory) {
        this.message = message;
        this.factory = factory;
    }

    public String getExpectedEncoding() {
        return Constants.ENC_UTF8;
    }

    public void read(InputStream in, String encoding) throws IOException {
        try {
            message = factory.read(in);
        } catch (AtnaException e) {
            processReadError(e);
        }
    }

    public void write(OutputStream out) throws IOException {
        if (getMessageObject() == null) {
            throw new IOException("no AtnaMessage to write out.");
        }
        try {
            factory.write(getMessageObject(), out);
        } catch (AtnaException e) {
            processWriteError(e);
        }
    }

    public AtnaMessage getMessageObject() {
        return message;
    }


    protected void processReadError(AtnaException e) {
        AtnaErrorLogger.log(e);
    }

    protected void processWriteError(AtnaException e) {
        AtnaErrorLogger.log(e);
    }

}