package org.openhealthtools.openatna.syslog.protocol;

import org.apache.commons.lang.StringUtils;
import org.openhealthtools.openatna.syslog.Constants;
import org.openhealthtools.openatna.syslog.SyslogException;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A structured element as defined by RFC 5424
 *
 * @author Andrew Harrison
 */
public class StructuredElement implements Serializable {

    protected static final int DOC_ENTERPRISE_NUMBER = 32473;
    /**
     * timeQuality parameters
     */
    protected static final String TZ_KNOWN = "tzKnown";
    protected static final String IS_SYNCED = "isSynced";
    protected static final String SYNC_ACCURACY = "syncAccuracy";
    /**
     * origin parameters
     */
    protected static final String IP = "ip";
    protected static final String ENTERPRISE_ID = "enterpriseId";
    protected static final String SOFTWARE = "software";
    protected static final String SW_VERSION = "swVersion";
    /**
     * meta parameters
     */
    protected static final String SEQUENCE_ID = "sequenceId";
    protected static final String SYS_UPTIME = "sysUpTime";
    protected static final String LANGUAGE = "language";
    protected static char[] escaped = {'"', '\\', ']'};
    protected static char[] disallowed = {' ', '=', '\\', ']'};
    protected static String[] ianaIds = {"timeQuality", "origin", "meta",};
    protected Set<SdParam> params = new HashSet<>();
    private String id;

    public StructuredElement(String id, List<SdParam> params) {
        this.id = id;
        this.params.addAll(params);
    }

    public static String unescape(String param) {

        boolean afterBaskslash = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (c == '\\') {
                afterBaskslash = true;
            } else {
                if (afterBaskslash) {
                    boolean toescape = false;
                    for (char c1 : escaped) {
                        if (c == c1) {
                            toescape = true;
                            break;
                        }
                    }
                    if (!toescape) {
                        sb.append('\\');
                    }
                    afterBaskslash = false;
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String escape(String param) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            for (char c1 : escaped) {
                if (c == c1) {
                    sb.append('\\');
                    break;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * reads in structured data and leaves the stream positioned after the space at the end of the data, i.e.
     * ready to read the payload.
     * <p/>
     * This reads up to 1024 characters. This limit is arbitrary. It is imposed to reduce the risk
     * of badly formed or malicious messages from using too many resources.
     * state:
     * 0 - not in element
     * 1 - inElement
     * 2 - inId
     * 3 - inName
     * 4 - inParamQuote
     * 5 - inParam
     * 6 - afterParam
     *
     * @param in
     * @return
     * @throws SyslogException
     */
    public static List<StructuredElement> parse(InputStream in) throws SyslogException {

        List<StructuredElement> ret = new ArrayList<>();
        List<SdParam> params = new ArrayList<>();
        String currName = null;
        String id = null;
        int state = 0;
        int max = 1024;
        int curr = 0;
        ByteBuffer buff = ByteBuffer.wrap(new byte[max]);
        try {
            boolean afterBackslash = false;
            while (curr < max) {
                byte c = (byte) in.read();
                curr++;
                switch (state) {
                    case 0: // not in element
                        if (c == ' ') {
                            return ret; // done
                        } else if (c == '[') {
                            state = 1;
                        } else {
                            throw new SyslogException("invalid charater:" + c);
                        }
                        break;
                    case 1: // in element. chararter must be the start of the id
                        for (char c1 : disallowed) {
                            if (c == c1) {
                                throw new SyslogException("invalid charater:" + c);
                            }
                        }
                        buff.put(c);
                        state = 2;
                        break;
                    case 2: // in id
                        if (c == ']') { // no parameters. This is allowed (I think)
                            if (buff.position() == 0) {
                                throw new SyslogException("no id defined");
                            }
                            id = new String(buff.array(), 0, buff.position(), Constants.ENC_UTF8);
                            verifyId(id);
                            buff.clear();
                            StructuredElement se = new StructuredElement(id, params);
                            ret.add(se);
                            id = null;
                            currName = null;
                            params.clear();
                            state = 0;
                        } else if (c == ' ') {
                            if (buff.position() == 0) {
                                throw new SyslogException("no id defined");
                            }
                            id = new String(buff.array(), 0, buff.position(), Constants.ENC_UTF8);
                            verifyId(id);
                            buff.clear();
                            state = 3; // move to param name
                        } else {
                            for (char c1 : disallowed) {
                                if (c == c1) {
                                    throw new SyslogException("invalid charater:" + c);
                                }
                            }
                            buff.put(c);
                        }
                        break;
                    case 3:  // in param name
                        if (c == '=') {
                            if (buff.position() == 0) {
                                throw new SyslogException("no param key defined");
                            }
                            currName = new String(buff.array(), 0, buff.position(), Constants.ENC_UTF8);
                            verifyName(currName);
                            buff.clear();
                            state = 4; // move to param value quotes
                        } else {
                            for (char c1 : disallowed) {
                                if (c == c1) {
                                    throw new SyslogException("invalid charater:" + c);
                                }
                            }
                            buff.put(c);
                        }
                        break;
                    case 4: // after param name, before param value
                        if (c != '"') {
                            throw new SyslogException("invalid charater:" + c);
                        }
                        state = 5; // move into param value
                        break;
                    case 5:
                        if (afterBackslash) {
                            afterBackslash = false;
                            boolean shouldBeEscaped = false;
                            for (char c1 : escaped) {
                                if (c == c1) {
                                    shouldBeEscaped = true;
                                    break;
                                }
                            }
                            if (!shouldBeEscaped) {
                                throw new SyslogException("invalid charater:" + c);
                            }
                            buff.put(c);
                        } else {
                            if (c == '"') { // end of param value
                                if (currName == null) {
                                    throw new SyslogException("no param name defined for value");
                                }
                                if (buff.position() == 0) {
                                    throw new SyslogException("no param value defined");
                                }
                                params.add(new SdParam(currName, new String(buff.array(), 0, buff.position(), Constants.ENC_UTF8)));
                                buff.clear();
                                state = 6;
                            } else if (c == '\\') {

                                afterBackslash = true;
                            } else {
                                buff.put(c);
                            }
                        }
                        break;
                    case 6:
                        if (c == ']') {
                            if (id == null) {
                                throw new SyslogException("no id defined");
                            }
                            StructuredElement se = new StructuredElement(id, params);
                            ret.add(se);
                            id = null;
                            currName = null;
                            buff.clear();
                            params.clear();
                            state = 0;
                        } else {
                            if (c != ' ') {
                                throw new SyslogException("invalid charater:" + c);
                            }
                            state = 3; // go to next parameter
                        }

                        break;
                    default:
                        throw new SyslogException("unknown state:" + state);
                }
            }
        } catch (Exception e) {
            throw new SyslogException(e);
        }
        throw new SyslogException("suspicious: too many characters.");
    }

    private static void verifyId(String id) throws SyslogException {

        boolean iana = false;
        for (String ianaId : ianaIds) {
            if (id.equals(ianaId)) {
                iana = true;
                break;
            }
        }
        if (!iana && !id.contains("@")) {
            throw new SyslogException("Non reserverd id with no @ symbol");
        }

    }

    private static void verifyName(String name) throws SyslogException {

        if (StringUtils.equals(name, String.valueOf(DOC_ENTERPRISE_NUMBER))) {
            throw new SyslogException("documentation enterprise number not allowed");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * returns a copy
     *
     * @return
     */
    public List<SdParam> getParams() {
        return new ArrayList<>(params);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(id);
        for (SdParam s : params) {
            sb.append(" ")
                    .append(s.getName())
                    .append("=\"")
                    .append(escape(s.getValue()))
                    .append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StructuredElement that = (StructuredElement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return params != null ? params.equals(that.params) : that.params == null;
    }

    @Override
    public int hashCode() {

        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }
}
