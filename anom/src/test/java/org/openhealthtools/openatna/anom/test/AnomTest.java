/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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

package org.openhealthtools.openatna.anom.test;

import java.io.IOException;

import org.openhealthtools.openatna.anom.AtnaCode;
import org.openhealthtools.openatna.anom.AtnaException;
import org.openhealthtools.openatna.anom.AtnaMessage;
import org.openhealthtools.openatna.anom.AtnaMessageObject;
import org.openhealthtools.openatna.anom.AtnaMessageParticipant;
import org.openhealthtools.openatna.anom.AtnaObject;
import org.openhealthtools.openatna.anom.AtnaObjectDetail;
import org.openhealthtools.openatna.anom.AtnaParticipant;
import org.openhealthtools.openatna.anom.AtnaSource;
import org.openhealthtools.openatna.anom.EventOutcome;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


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

        AtnaCode evtCode = AtnaCode.eventIdCode("abc", "SYS_CODE", "SYS_CODENAME", null, null);

        AtnaMessage msg = new AtnaMessage(evtCode, EventOutcome.SUCCESS);
        msg.addSource(new AtnaSource("source").addSourceTypeCode(AtnaCode.sourceTypeCode("4", null, null, null, null)))
                .addParticipant(new AtnaMessageParticipant(new AtnaParticipant("participant")))
                .addObject(new AtnaMessageObject(new AtnaObject("obj-id", AtnaCode.objectIdTypeCode("obj-code", null, null, null, null))));
        msg.getObject("obj-id").addObjectDetail(new AtnaObjectDetail().setType("detail").setValue("THIS IS DETAIL".getBytes()));


        assertEquals(msg.getEventOutcome(), EventOutcome.SUCCESS);
        assertEquals(msg.getSource("source").getSourceTypeCodes().get(0).getCode(), "4");
        assertEquals(new String(msg.getObject("obj-id").getObjectDetails().get(0).getValue()), "THIS IS DETAIL");

    }


}
