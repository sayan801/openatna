package org.openhealthtools.openatna.audit.server;

import org.openhealthtools.openatna.audit.log.SyslogErrorLogger;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 29, 2010
 */
public class MessageQueue {

    private final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private boolean running = false;
    private final Runner runner;

    public MessageQueue(SyslogListener listener) {
        this.runner = new Runner(listener);
    }

    public void start() {

        if (running) {
            return;
        }
        running = true;
        exec.execute(runner);
    }

    public void stop() {

        logger.debug("Message Queue shutting down...");
        running = false;
        exec.shutdown();
    }

    public void put(SyslogMessage msg) {
        runner.put(msg);
    }

    public void put(SyslogException msg) {
        runner.put(msg);
    }

    private class Runner implements Runnable {

        private final SyslogListener listener;
        private final BlockingQueue messageQueue = new LinkedBlockingQueue();

        private Runner(SyslogListener listener) {
            this.listener = listener;
        }

        public void put(Object msg) {
            try {
                messageQueue.put(msg);
            } catch (InterruptedException e) {
                messageQueue.clear();
                Thread.currentThread().interrupt();
            }
        }

        public void run() {

            while (!Thread.interrupted() && running) {
                Object o = messageQueue.poll();
                if (o == null) {
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        logger.error("InterruptedException: '{}'", e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                } else {
                    handleMessage(o);
                }
            }
            logAndClearMessageQueue();
        }

        private void logAndClearMessageQueue() {

            while (!messageQueue.isEmpty()) {
                handleMessage(messageQueue.poll());
            }
        }

        private void handleMessage(Object o) {

            if (o instanceof SyslogMessage) {
                handleSysLogMessage((SyslogMessage) o);
            } else if (o instanceof SyslogException) {
                handleSysLogException((SyslogException) o);
            }
        }

        private void handleSysLogMessage(SyslogMessage message) {

            if (running) {
                listener.messageArrived(message);
            } else {
                try {
                    if (logger.isErrorEnabled()) {
                        logger.error("MessageQueue was unable to persist message: '{}'", new String(message.toByteArray()));
                    }
                } catch (SyslogException e) {
                    handleSysLogException(e);
                }
            }
        }

        private void handleSysLogException(SyslogException exception) {

            if (running) {
                listener.exceptionThrown(exception);
            } else {
                SyslogErrorLogger.log(exception);
            }
        }
    }
}
