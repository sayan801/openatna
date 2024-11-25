package org.openhealthtools.openatna.anom;

/**
 * Value Meaning
 * ----- --------------------------------
 * 1   Machine Name, including DNS name
 * 2   IP Address
 * 3   Telephone Number
 *
 * @author Andrew Harrison
 */
public enum NetworkAccessPoint {

    MACHINE_NAME(1),
    IP_ADDRESS(2),
    TELEPHONE_NUMBER(3);

    private int value;

    NetworkAccessPoint(int value) {
        this.value = value;
    }

    public static NetworkAccessPoint getAccessPoint(int value) {
        for (NetworkAccessPoint o : values()) {
            if (o.value() == value) {
                return o;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }
}
