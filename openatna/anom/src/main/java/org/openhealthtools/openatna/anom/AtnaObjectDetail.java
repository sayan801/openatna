package org.openhealthtools.openatna.anom;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Object Detail
 *
 * @author Andrew Harrison
 */
public class AtnaObjectDetail implements Serializable {

    private static final long serialVersionUID = -4400971126353837669L;

    private String type;
    private byte[] value;

    public String getType() {
        return type;
    }

    public AtnaObjectDetail setType(String type) {
        this.type = type;
        return this;
    }

    public byte[] getValue() {
        return value;
    }

    public AtnaObjectDetail setValue(byte[] value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AtnaObjectDetail that = (AtnaObjectDetail) o;

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? Arrays.hashCode(value) : 0);
        return result;
    }

    @Override
    public String toString() {
        byte[] bytes = getValue();
        if (bytes == null) {
            bytes = new byte[0];
        }
        return "[" +
                getClass().getName() +
                " type='" +
                getType() +
                "' value='" +
                new String(bytes) +
                "']";
    }
}
