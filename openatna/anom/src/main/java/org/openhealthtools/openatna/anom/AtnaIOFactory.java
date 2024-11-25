package org.openhealthtools.openatna.anom;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Andrew Harrison
 */
public interface AtnaIOFactory {

    AtnaMessage read(InputStream in) throws AtnaException;

    void write(AtnaMessage message, OutputStream out) throws AtnaException;

    void write(AtnaMessage message, OutputStream out, boolean includeDeclaration) throws AtnaException;
}
