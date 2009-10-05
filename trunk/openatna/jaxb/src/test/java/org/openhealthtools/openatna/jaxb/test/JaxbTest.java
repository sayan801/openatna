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

package org.openhealthtools.openatna.jaxb.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.jaxb21.JaxbIOFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 30, 2009: 1:58:14 PM
 * @date $Date:$ modified by $Author:$
 */

public class JaxbTest {


    @Test
    public void testAnom() throws IOException, AtnaException {

        AtnaCode evtCode = new AtnaCode("abc", "SYS_CODE", "SYS_CODENAME", null, null);

        AtnaMessage msg = new AtnaMessage(evtCode, EventOutcome.SUCCESS);
        msg.addSource(new AtnaSource("source").addSourceTypeCode(new AtnaCode("4", null, null, null, null)))
                .addParticipant(new AtnaMessageParticipant(new AtnaParticipant("participant")))
                .addObject(new AtnaMessageObject(new AtnaObject("obj-id", new AtnaCode("obj-code", null, null, null, null))));
        msg.getObject("obj-id").addObjectDetail(new AtnaObjectDetail().setType("detail").setValue("THIS IS DETAIL".getBytes()));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        JaxbIOFactory fac = new JaxbIOFactory();
        fac.write(msg, bout);
        AtnaMessage other = fac.read(new ByteArrayInputStream(bout.toByteArray()));
        assertEquals(other.getSources().size(), 1);
        AtnaSource as = other.getSource("source");
        assertEquals(as.getSourceId(), "source");
        AtnaCode code = as.getSourceTypeCodes().get(0);
        assertEquals(code.getCode(), "4");
        assertEquals(other.getEventOutcome(), EventOutcome.SUCCESS);
        assertEquals(other.getSource("source").getSourceTypeCodes().get(0).getCode(), "4");
        assertEquals(new String(other.getObject("obj-id").getObjectDetails().get(0).getValue()), "THIS IS DETAIL");
        assertEquals(other, msg);
    }
}
