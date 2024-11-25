package org.openhealthtools.openatna.audit.log;

/**
 * Added to a Logger, these get called when the log() method of the logger gets called.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */
public interface ErrorHandler<T extends Throwable> {

    void handle(T t);
}
