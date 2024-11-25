package org.openhealthtools.openatna.all.logging;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface AuditLoggerPluginManager {

    void start() throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    void destroy();

    void handleAuditEvent(HttpServletRequest request, Map<String, String> queryParameters, List<Long> messageEntityIds);
}
