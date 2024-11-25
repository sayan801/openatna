package org.openhealthtools.openatna.syslog;

/**
 * This exception is thrown if there is a problem reading a syslog message.
 * There can be two causes for this - usually either an IOException, which this wraps, or a syslog syntax exception.
 * <p/>
 * NOTE: If an IOException is thrown, then one reason could be an SSLHandshakeException or similar SSL error.
 * Implementations should look for this, because it means an un-authorized access attempt has possibly taken place.
 * This needs to be audited. The SyslogError code is provided to allow implementations to signal this possibility,
 * along with general IO errors, and syslog syntax errors.
 * <p/>
 * This exception can also contain the bytes that lead to the exception, but this is not guaranteed.
 * The only thing that is guaranteed, is that the byte[] will not be null, i.e., if no bytes are passed into the
 * constructor, an empty array is used.
 *
 * @author Andrew Harrison
 */
public class SyslogException extends Exception {

    private static final long serialVersionUID = -8496727797149988770L;
    private byte[] bytes;
    private SyslogError error;
    private String sourceIp;

    public SyslogException(String s) {
        this(s, new byte[0], SyslogError.UNDEFINED);
    }

    public SyslogException(String s, Throwable throwable) {
        this(s, throwable, new byte[0], SyslogError.UNDEFINED);
    }

    public SyslogException(Throwable throwable) {
        this(throwable, new byte[0], SyslogError.UNDEFINED);
    }

    public SyslogException(String s, byte[] bytes) {
        this(s, bytes, SyslogError.UNDEFINED);
    }

    public SyslogException(String s, Throwable throwable, byte[] bytes) {
        this(s, throwable, bytes, SyslogError.UNDEFINED);
    }

    public SyslogException(Throwable throwable, byte[] bytes) {
        this(throwable, bytes, SyslogError.UNDEFINED);
    }

    public SyslogException(String s, byte[] bytes, SyslogError error) {
        super(s);
        this.bytes = bytes;
        this.error = error;
    }

    public SyslogException(String s, Throwable throwable, byte[] bytes, SyslogError error) {
        super(s, throwable);
        this.bytes = bytes;
        this.error = error;
    }

    public SyslogException(Throwable throwable, byte[] bytes, SyslogError error) {
        super(throwable);
        this.bytes = bytes;
        this.error = error;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public SyslogError getError() {
        return error;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public enum SyslogError {
        UNDEFINED,
        SYNTAX_ERROR,
        IO_ERROR,
        SSL_ERROR
    }
}
