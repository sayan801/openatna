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

package org.openhealthtools.openatna.anom;

/**
 * Coded Value
 * <p/>
 * provides setters for everything bar the code which is required
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 2:37:18 PM
 * @date $Date:$ modified by $Author:$
 */
public interface AtnaCode {

    public String getCode();

    public String getCodeSystem();

    public AtnaCode setCodeSystem(String value);

    public String getCodeSystemName();

    public AtnaCode setCodeSystemName(String value);

    public String getDisplayName();

    public AtnaCode setDisplayName(String value);

    public String getOriginalText();

    public AtnaCode setOriginalText(String value);


}