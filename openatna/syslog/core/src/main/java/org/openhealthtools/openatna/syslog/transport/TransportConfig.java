package org.openhealthtools.openatna.syslog.transport;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for configuring servers. Not required by Syslog Servers.
 *
 * @author Andrew Harrison
 */
public abstract class TransportConfig {

    private String name;

    private Map<String, Object> properties = new HashMap<>();

    public TransportConfig(String name) {
        this.name = name;
    }

    public TransportConfig(String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }
}
