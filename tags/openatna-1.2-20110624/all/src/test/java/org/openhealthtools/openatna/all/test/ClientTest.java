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

package org.openhealthtools.openatna.all.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.codes.CodeParser;
import org.openhealthtools.openatna.jaxb21.JaxbIOFactory;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 22, 2009: 12:31:51 PM
 * @date $Date:$ modified by $Author:$
 */

public abstract class ClientTest {

    static {
        URL defCodes = Thread.currentThread().getContextClassLoader().getResource("atnacodes.xml");
        if (defCodes != null) {
            CodeParser.parse(defCodes.toString());
        } else {
            System.out.println("could not load codes!!");
        }
    }

    public List<AtnaMessage> getMessages() throws IOException, AtnaException {
        JaxbIOFactory fac = new JaxbIOFactory();
        List<AtnaMessage> messages = new ArrayList<AtnaMessage>();
        URL url = ClientTest.class.getResource("/msgs");
        File f = new File(url.getFile());
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().endsWith(".xml")) {
                messages.add(fac.read(new FileInputStream(file)));
            }
        }

        return messages;
    }
}
