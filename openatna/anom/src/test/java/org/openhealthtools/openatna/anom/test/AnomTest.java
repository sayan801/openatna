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

package org.openhealthtools.openatna.anom.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.anom.jaxb21.JaxbAtnaFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * TODO - make complete
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 5, 2009: 8:38:33 PM
 * @date $Date:$ modified by $Author:$
 */

public class AnomTest {

    @Test
    public void testAnom() throws IOException, AtnaException {

        AtnaFactory fac = new JaxbAtnaFactory();
        AtnaCode evtCode = fac.newCode("abc", "SYS_CODE", "SYS_CODENAME");
        AtnaEvent evt = fac.newEvent(evtCode, EventOutcome.SUCCESS);

        AtnaMessage msg = fac.newMessage(evt);
        msg.addSource(fac.newSource("source").addSourceTypeCode(fac.newCode("4")))
                .addParticipant(fac.newParticipant("participant"))
                .addObject(fac.newObject(fac.newCode("obj-code"), "obj-id"));
        msg.getObject("obj-id").addObjectDetail(fac.newObjectDetail().setType("detail").setValue("THIS IS DETAIL".getBytes()));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        fac.write(msg, bout);

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        msg = fac.read(bin);
        assertEquals(msg.getEvent().getEventOutcome(), EventOutcome.SUCCESS);
        assertEquals(msg.getSource("source").getSourceTypeCodes().get(0).getCode(), "4");
        assertEquals(new String(msg.getObject("obj-id").getObjectDetails().get(0).getValue()), "THIS IS DETAIL");

    }


}
