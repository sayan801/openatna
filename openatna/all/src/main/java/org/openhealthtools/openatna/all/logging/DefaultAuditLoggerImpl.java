package org.openhealthtools.openatna.all.logging;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of OpenATNALogViewLogger implementation
 */
public class DefaultAuditLoggerImpl implements AuditLogger {

    private final Logger logger = LoggerFactory.getLogger(DefaultAuditLoggerImpl.class);

    public void logViewRequest(HttpServletRequest request, Map<String, String> queryParameters, List<Long> messageEntityIds) {

        if (logger.isInfoEnabled()) {
            logger.info("Cookies: '{}'", cookiesToString(request.getCookies()));
            logger.info("Search Parameters: '{}'", queryParameters);
            logger.info("LogRow Entity Ids: '{}'", messageEntityIds);
        }
    }

    public void start() {
        // No specific action required during startup.
    }

    public void destroy() {
        // No specific action required during shutdown.
    }

    /**
     * Returns a concatenate String with the Cookie attributes values {Name, Value, MaxAge and Secure} if available.
     *
     * @param cookies - Array of Cookie from the Session
     * @return String chain of Cookie attributes.
     */
    private String cookiesToString(Cookie[] cookies) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (int i = 0; i < cookies.length; i++) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append("{");
                if (cookies[i].getName() != null) {
                    stringBuilder.append("Name=").append(cookies[i].getName());
                }
                if (cookies[i].getValue() != null) {
                    stringBuilder.append(" Value=").append(cookies[i].getValue());
                }
                if (cookies[i].getMaxAge() != -1) {
                    stringBuilder.append(" MaxAge=").append(cookies[i].getMaxAge());
                }
                stringBuilder.append(" Secure=").append(cookies[i].getSecure());
                stringBuilder.append("}");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
