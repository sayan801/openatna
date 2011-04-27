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

package org.openhealthtools.openatna.audit.process;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 26, 2009: 4:01:23 PM
 * @date $Date:$ modified by $Author:$
 */

public class PhaseProcessor implements AtnaProcessor {

    private List<AtnaProcessor> processors = new ArrayList<AtnaProcessor>();
    private int next = 0;

    public void addFirst(AtnaProcessor processor) {
        processors.add(0, processor);
        next = 1;
    }

    public void addLast(AtnaProcessor processor) {
        processors.add(processor);
        next = processors.size();
    }

    public void addNext(AtnaProcessor processor) {
        processors.add(next, processor);
        next++;
    }

    public void process(ProcessContext context) throws Exception {
        if (context.getState() == ProcessContext.State.ABORTED) {
            return;
        }
        for (AtnaProcessor processor : processors) {
            processor.process(context);
            if (context.getState() == ProcessContext.State.ABORTED) {
                return;
            }
        }
    }

    public void error(ProcessContext context) {
        for (int i = processors.size() - 1; i > 0; i--) {
            AtnaProcessor atnaProcessor = processors.get(i);
            atnaProcessor.error(context);
        }
    }


}
