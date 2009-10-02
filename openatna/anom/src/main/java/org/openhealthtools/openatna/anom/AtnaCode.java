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
public class AtnaCode extends ImmutableAtnaCode {

	private static final long serialVersionUID = -2436873338756275488L;

	public AtnaCode(String code) {
        super(code);
    }

    public AtnaCode(String code, String codeSystem) {
    	super(code, codeSystem, null, null, null);
    }

    public AtnaCode(String code, String codeSystem, String codeSystemName) {
        super(code, codeSystem, codeSystemName, null, null);
    }

    public AtnaCode(String code, String codeSystem, String codeSystemName, String displayName) {
        super(code, codeSystem, codeSystemName, displayName, null);
    }

    public AtnaCode setCode(String code) {
        this.code = code;
        return this;
    }

    public AtnaCode setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
        return this;
    }

    public AtnaCode setCodeSystemName(String codeSystemName) {
        this.codeSystemName = codeSystemName;
        return this;
    }

    public AtnaCode setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public AtnaCode setOriginalText(String originalText) {
        this.originalText = originalText;
        return this;
    }
}
