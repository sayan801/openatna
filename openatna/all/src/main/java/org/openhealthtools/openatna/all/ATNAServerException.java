package org.openhealthtools.openatna.all;

public class ATNAServerException extends RuntimeException {

    public ATNAServerException() {
        super();
    }

    public ATNAServerException(String message) {
        super(message);
    }

    public ATNAServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
