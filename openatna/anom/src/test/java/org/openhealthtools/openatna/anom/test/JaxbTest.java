package org.openhealthtools.openatna.anom.test;

import org.junit.Test;
import org.openhealthtools.openatna.anom.*;
import org.openhealthtools.openatna.anom.JaxbIOFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrew Harrison
 */
public class JaxbTest {

    @Test
    public void testAnom() throws AtnaException {

        AtnaCode evtCode = AtnaCode.eventIdCode("abc", "SYS_CODE", "SYS_CODENAME", null, null);

        AtnaMessage msg = new AtnaMessage(evtCode, EventOutcome.SUCCESS);
        msg.addSource(new AtnaSource("source").addSourceTypeCode(AtnaCode.sourceTypeCode("4", null, null, null, null)))
                .addParticipant(new AtnaMessageParticipant(new AtnaParticipant("participant")))
                .addObject(new AtnaMessageObject(new AtnaObject("obj-id", AtnaCode.objectIdTypeCode("obj-code", null, null, null, null))));
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
