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

package org.openhealthtools.openatna.all.test.persistence;

import java.io.InputStream;

import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.util.DataReader;


/**
 * Populates the DB with dummy data
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 9:01:02 PM
 * @date $Date:$ modified by $Author:$
 */

public class Populate {


    public static void main(String[] args) {
        try {
            InputStream in = Populate.class.getClassLoader().getResourceAsStream("test-data.xml");
            DataReader reader = new DataReader(in);
            reader.parse();
        } catch (AtnaPersistenceException e) {
            e.printStackTrace();
            System.out.println("ERROR: " + e.getError());
        }
    }


}
