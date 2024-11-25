package org.openhealthtools.openatna.syslog;

import org.openhealthtools.openatna.syslog.bsd.BsdMessageFactory;
import org.openhealthtools.openatna.syslog.protocol.ProtocolMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Determines whether the message is BSD or RFC 5424 based on the first few bytes of the message, and calls
 * the appropriate MessageFactory.
 *
 * @author Andrew Harrison
 */
public class GenericMessageFactory extends SyslogMessageFactory {

    private static BsdMessageFactory bsdFactory = new BsdMessageFactory();
    private static ProtocolMessageFactory protFactory = new ProtocolMessageFactory();
    private final Logger logger = LoggerFactory.getLogger(GenericMessageFactory.class);

    /**
     * Returns true if this is a BSD syslog message.
     * Relies on the 5424 VERSION value being '1' otherwise false
     *
     * @param bytes
     * @return
     * @throws org.openhealthtools.openatna.syslog.SyslogException
     */
    private boolean isBSD(byte[] bytes) throws SyslogException {
        try {
            boolean isBSD = true;
            String s = new String(bytes);
            int close = s.indexOf('>');
            if (close > 1 && close < bytes.length - 2) {
                char next = s.charAt(close + 1);
                if (next == Constants.VERSION) {
                    next = s.charAt(close + 2);
                    if (next == ' ') {
                        isBSD = false;
                    }
                }
            }
            return isBSD;
        } catch (Exception e) {
            throw new SyslogException(e);
        }
    }

    public SyslogMessage read(InputStream in) throws SyslogException {
        try {
            PushbackInputStream pin = new PushbackInputStream(in, 7);
            byte[] bytes = new byte[7];
            int byteRead = pin.read(bytes);

            logger.debug("Message Read '{}' length", byteRead);
            boolean bsd = isBSD(bytes);
            pin.unread(bytes);
            if (bsd) {
                logger.debug("message is BSD style");
                return bsdFactory.read(pin);
            } else {
                logger.debug("message RFC 5424");
                return protFactory.read(pin);
            }
        } catch (IOException e) {
            throw new SyslogException(e);
        } catch (Exception e) {
            logger.error("Exception: '{}'", e.getMessage(), e);
            throw new SyslogException(e);
        }
    }
}
