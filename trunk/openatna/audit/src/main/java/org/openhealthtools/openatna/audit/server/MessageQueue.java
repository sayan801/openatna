/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.audit.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 29, 2010
 */
public class MessageQueue {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.MessageQueue");


    private ExecutorService exec = Executors.newSingleThreadExecutor();
    private boolean running = false;
    private Runner runner;

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
        log.debug("Message Queue shutting down...");
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

        private SyslogListener listener;
        private BlockingQueue messageQueue = new LinkedBlockingQueue();

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
            while (!Thread.interrupted()) {
                try {
                    Object o = messageQueue.take();
                    if (o instanceof SyslogMessage) {
                        listener.messageArrived((SyslogMessage) o);
                    } else if (o instanceof SyslogException) {
                        listener.exceptionThrown((SyslogException) o);
                    }
                } catch (InterruptedException e) {
                    messageQueue.clear();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
