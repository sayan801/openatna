package org.openhealthtools.openatna.all.logging;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface AuditLogger {

    void destroy();

    void start();

    void logViewRequest(HttpServletRequest request, Map<String, String> queryParameters, List<Long> messageEntityIds);
}
