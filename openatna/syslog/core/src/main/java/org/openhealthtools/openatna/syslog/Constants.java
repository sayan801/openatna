package org.openhealthtools.openatna.syslog;

/**
 * Syslog constants
 *
 * @author Andrew Harrison
 */
public class Constants {

    public static final char VERSION = '1';
    public static final String ENC_UTF8 = "UTF-8";
    public static final String ENC_UTF16LE = "UTF-16LE";
    public static final String ENC_UTF16BE = "UTF-16BE";
    public static final String ENC_UTF32LE = "UTF-32LE";
    public static final String ENC_UTF32BE = "UTF-32BE";
    protected static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    protected static final byte[] UTF16LE_BOM = {(byte) 0xFF, (byte) 0xFE};
    protected static final byte[] UTF16BE_BOM = {(byte) 0xFE, (byte) 0xFF};
    protected static final byte[] UTF32LE_BOM = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
    protected static final byte[] UTF32BE_BOM = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};
}
