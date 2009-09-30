/*
 * Copyright (c) 2009 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.audit.process;

import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openatna.audit.AuditException;

/**
 * A chain for processors.
 * <p/>
 * The addNext() method is relative to the previous call to one of the add* methods.
 * addFirst() resets the "next" counter to 1.
 * addLastLast() resets the "next" counter to the size of the list of processors.
 * addNext() increments the "next" counter.
 * <p/>
 * The "next" counter starts off with a value of 0.
 * <p/>
 * The chain will persists the atna message, if no previous processors have set the
 * state of the context to PERSISTED. This means only domain/business processors
 * need to be added manually.
 * <p/>
 * It will also validate the message before passing it to any other processors.
 * To not use the built-in validator, use the constructor with the boolean set to false.
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 13, 2009: 9:07:16 PM
 * @date $Date:$ modified by $Author:$
 */

public class ProcessorChain {

    private List<AtnaProcessor> processors = new ArrayList<AtnaProcessor>();
    private int next = 0;
    private ValidationProcessor validator = new ValidationProcessor();
    private PersistenceProcessor persist = new PersistenceProcessor();
    private boolean validate;

    public ProcessorChain(boolean validate) {
        this.validate = validate;
    }

    public ProcessorChain() {
        this(true);
    }

    public ProcessorChain addFirst(AtnaProcessor processor) {
        processors.add(0, processor);
        next = 1;
        return this;
    }

    public ProcessorChain addLast(AtnaProcessor processor) {
        processors.add(processor);
        next = processors.size();
        return this;
    }

    public ProcessorChain addNext(AtnaProcessor processor) {
        processors.add(next, processor);
        next++;
        return this;
    }

    public void process(ProcessContext context) throws AuditException {
        if (validate) {
            validator.process(context);
        }
        for (AtnaProcessor processor : processors) {
            processor.process(context);
        }
        if (context.getState() != ProcessContext.State.PERSISTED) {
            persist.process(context);
        }
    }

}
