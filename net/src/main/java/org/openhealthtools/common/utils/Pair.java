/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.common.utils;

import java.io.Serializable;

/**
 * A utility container class used to group a pair of related objects.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */

public final class Pair implements Serializable {
    public Object _first = null;
    public Object _second = null;

    public Pair() {
    }

    public Pair(Object first, Object second) {
        _set(first, second);
    }

    public Pair(int first, int second) {
        _set(new Integer(first), new Integer(second));
    }

    public Pair(String first, Object second) {
        _set(first, second);
    }

    private void _set(Object first, Object second) {
        _first = first;
        _second = second;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_first == null) ? 0 : _first.hashCode());
        result = prime * result + ((_second == null) ? 0 : _second.hashCode());
        return result;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#equals(java.lang.Object)
      */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (_first == null) {
            if (other._first != null) {
                return false;
            }
        } else if (!_first.equals(other._first)) {
            return false;
        }
        if (_second == null) {
            if (other._second != null) {
                return false;
            }
        } else if (!_second.equals(other._second)) {
            return false;
        }
        return true;
    }

}