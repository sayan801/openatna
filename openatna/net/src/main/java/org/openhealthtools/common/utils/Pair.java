package org.openhealthtools.common.utils;

import java.io.Serializable;

/**
 * A utility container class used to group a pair of related objects.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */

public final class Pair implements Serializable {

    private Object _first = null;

    private Object _second = null;

    public Pair() {
    }

    public Pair(Object first, Object second) {
        _set(first, second);
    }

    public Pair(int first, int second) {
        _set(first, second);
    }

    public Pair(String first, Object second) {
        _set(first, second);
    }

    private void _set(Object first, Object second) {
        _first = first;
        _second = second;
    }
    
    public Object get_first() {
        return _first;
    }

    public void set_first(Object _first) {
        this._first = _first;
    }

    public Object get_second() {
        return _second;
    }

    public void set_second(Object _second) {
        this._second = _second;
    }

    /**
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

    /**
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
            return other._second == null;
        } else return _second.equals(other._second);
    }
}
