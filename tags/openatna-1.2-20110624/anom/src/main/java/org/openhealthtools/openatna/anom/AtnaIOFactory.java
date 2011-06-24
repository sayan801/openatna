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

package org.openhealthtools.openatna.anom;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 1:51:10 PM
 * @date $Date:$ modified by $Author:$
 */

public interface AtnaIOFactory {

    public abstract AtnaMessage read(InputStream in) throws AtnaException;

    public abstract void write(AtnaMessage message, OutputStream out) throws AtnaException;

    public abstract void write(AtnaMessage message, OutputStream out, boolean includeDeclaration) throws AtnaException;
}
