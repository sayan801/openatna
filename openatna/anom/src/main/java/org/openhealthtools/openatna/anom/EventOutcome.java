package org.openhealthtools.openatna.anom;

/**
 * <p>
 * Value Meaning
 * ---- ----------------------------------------------------
 * 0   Success
 * 4   Minor failure; action restarted, e.g., invalid password with first retry
 * 8   Serious failure; action terminated, e.g., invalid password with excess retries
 * 12   Major failure; action made unavailable, e.g., user account disabled due to excessive invalid log-on attempts
 * </p>
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public enum EventOutcome {

    SUCCESS(0),
    MINOR_FAILURE(4),
    SERIOUS_FAILURE(8),
    MAJOR_FAILURE(12);

    private int value;

    EventOutcome(int value) {
        this.value = value;
    }

    public static EventOutcome getOutcome(int value) {

        for (EventOutcome o : values()) {
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
