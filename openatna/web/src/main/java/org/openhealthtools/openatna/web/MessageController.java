package org.openhealthtools.openatna.web;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openhealthtools.openatna.all.logging.AuditLoggerPluginManager;
import org.openhealthtools.openatna.anom.Timestamp;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
public class MessageController {

    private static final String PARSE_EXCEPTION_MESSAGE = "ParseException: '{}', {}";
    private static final String HOUR_23 = "23";
    private static final String MINUTE_59 = "59";
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final AuditLoggerPluginManager auditLoggerPluginManager;
    private final MessageDao messageDao;

    @Autowired
    public MessageController(AuditLoggerPluginManager auditLoggerPluginManager, MessageDao messageDao) {
        this.auditLoggerPluginManager = auditLoggerPluginManager;
        this.messageDao = messageDao;
    }

    @GetMapping(value = "/query")
    public String home(ModelMap model) {

        logger.info("[ATNA] Query Controller");
        var queryBean = new QueryBean();
        model.addAttribute("queryBean", queryBean);
        return "messageForm";
    }

    @PostMapping(value = "/query")
    public ModelAndView query(HttpServletRequest request, QueryBean queryBean) {

        try {
            var modelMap = new ModelMap();
            var offset = 0;
            String start = request.getParameter("start");
            if (start != null) {
                try {
                    QueryBean qb = (QueryBean) request.getSession().getAttribute("currBean");
                    if (qb != null) {
                        queryBean = qb;
                    }
                    offset = Integer.parseInt(start);
                    if (offset < 0) {
                        offset = 0;
                    }

                } catch (NumberFormatException e) {
                    logger.debug("error for start offset value = {}; exception {} ", start, e);
                }
            }
            modelMap.addAttribute("offset", offset);
            queryBean.setStartOffset((offset) * queryBean.getMaxResults());
            var query = createQuery(queryBean);
            List<? extends MessageEntity> messageEntities = messageDao.getByQuery(query);
            List<StringifiedMessage> list = new ArrayList<>();
            if (query.hasConditionals() && messageEntities != null) {

                auditLoggerPluginManager.handleAuditEvent(request, getBeanMap(queryBean), createIdList(messageEntities));
                for (MessageEntity ent : messageEntities) {
                    list.add(new StringifiedMessage(ent));
                }
            }
            modelMap.addAttribute("messages", list);
            modelMap.addAttribute("queryBean", queryBean);

            return new ModelAndView("messageForm", modelMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            var modelMap = new ModelMap();
            modelMap.addAttribute("errorBean", new ErrorBean(e.getMessage()));
            return new ModelAndView("errorPage", modelMap);
        }
    }

    private Map<String, String> getBeanMap(QueryBean qb) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        Map<String, String> fields = BeanUtils.describe(qb);
        Map<String, String> map = new HashMap<>();

        for (Entry<String, String> entry : fields.entrySet()) {
            if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue()) && !"class".equals(entry.getKey())) {
                // class is a BeanUtils internal entry defining the Bean class
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    private List<Long> createIdList(List<? extends MessageEntity> messageEntities) {

        List<Long> ids = new ArrayList<>();
        for (MessageEntity me : messageEntities) {
            ids.add(me.getId());
        }
        return ids;
    }

    private String convertStars(String starred) {

        if (starred.startsWith("*")) {
            starred = "%" + starred.substring(1);
        }
        if (starred.endsWith("*")) {
            starred = starred.substring(0, starred.length() - 1) + "%";
        }
        return starred;
    }

    private Query.Conditional getConditionalForString(String val) {

        if (val.startsWith("*") || val.endsWith("*")) {
            return Query.Conditional.LIKE;
        }
        return Query.Conditional.EQUALS;
    }

    private Query createQuery(QueryBean bean) {

        var query = new Query();

        if (StringUtils.isNotBlank(bean.getEventIdCode())) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventIdCode(), Query.Target.EVENT_ID_CODE);
        }
        if (StringUtils.isNotBlank(bean.getEventOutcome())) {
            query.addConditional(Query.Conditional.EQUALS, Integer.parseInt(bean.getEventOutcome()), Query.Target.EVENT_OUTCOME);
        }
        if (StringUtils.isNotBlank(bean.getObjectId())) {
            query.addConditional(getConditionalForString(bean.getObjectId()), convertStars(bean.getObjectId()), Query.Target.OBJECT_ID);
        }
        if (StringUtils.isNotBlank(bean.getSourceId())) {
            query.addConditional(getConditionalForString(bean.getSourceId()), convertStars(bean.getSourceId()), Query.Target.SOURCE_ID);
        }
        if (StringUtils.isNotBlank(bean.getParticipantTypeCode())) {
            query.addConditional(getConditionalForString(bean.getParticipantTypeCode()), convertStars(bean.getParticipantTypeCode()),
                    Query.Target.PARTICIPANT_TYPE_CODE);
        }
        if (StringUtils.isNotBlank(bean.getSourceTypeCode())) {
            query.addConditional(getConditionalForString(bean.getSourceTypeCode()), convertStars(bean.getSourceTypeCode()),
                    Query.Target.SOURCE_TYPE_CODE);
        }
        if (StringUtils.isNotBlank(bean.getObjectTypeCode())) {
            query.addConditional(getConditionalForString(bean.getObjectTypeCode()), convertStars(bean.getObjectTypeCode()),
                    Query.Target.OBJECT_TYPE_CODE);
        }
        if (StringUtils.isNotBlank(bean.getParticipantId())) {
            query.addConditional(getConditionalForString(bean.getParticipantId()), convertStars(bean.getParticipantId()),
                    Query.Target.PARTICIPANT_ID);
        }
        if (StringUtils.isNotBlank(bean.getEventAction())) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventAction(), Query.Target.EVENT_ACTION);
        }
        if (StringUtils.isNotBlank(bean.getEventTime())) {
            var date = Timestamp.parseToDate(bean.getEventTime());

            if (date != null) {
                query.addConditional(Query.Conditional.EQUALS, date, Query.Target.EVENT_TIME);
            }
        }

        this.processDateFields(bean, query);

        if (StringUtils.isNotBlank(bean.getEventTypeCode())) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventTypeCode(), Query.Target.EVENT_TYPE_CODE);
        }
        if (StringUtils.isNotBlank(bean.getSourceAddress())) {
            query.addConditional(Query.Conditional.EQUALS, bean.getSourceAddress(), Query.Target.SOURCE_ADDRESS);
        }
        query.setMaxResults(bean.getMaxResults());
        query.setStartOffset(bean.getStartOffset());
        query.orderAscending(Query.Target.ID);

        return query;
    }

    /**
     * if both dates are filled, verify if end date is after start date. <br\>
     * if so,  <br\>
     * then filter between dates <br\>
     * if not, then put the hour of end date to midnight.  <br\>
     * if end date after start date  <br\>
     * then filter between dates <br\>
     * if still end date not after start date then set the query to filter after start date. <br\>
     * <br\>
     * otherwise: <br\>
     * if start date filled: query after start date <br\>
     * <br\>
     * if end date filled : query before end date <br\>
     * <br\>
     *
     * @param bean
     * @param query
     */
    private void processDateFields(QueryBean bean, Query query) {

        boolean isStartDateFilled = StringUtils.isNotBlank(bean.getStartDate());
        boolean isEndDateFilled = StringUtils.isNotBlank(bean.getEndDate());
        // is preferable to instantiate each time
        SimpleDateFormat dateFormat = (isStartDateFilled || isEndDateFilled) ? new SimpleDateFormat("yyyy-MM-dd HH:mm") : null;
        if (isStartDateFilled && isEndDateFilled) {
            processBothDateFilled(bean, query, dateFormat);
        } else if (isStartDateFilled) { // only start date
            var startDate = parseStartDate(bean, dateFormat);
            if (startDate != null)
                query.after(startDate);
        } else if (isEndDateFilled) { // only end date
            var endDate = parseEndDate(bean, dateFormat);
            if (endDate != null)
                query.before(endDate);
        }
    }

    private void processBothDateFilled(QueryBean bean, Query query, SimpleDateFormat dateFormat) {
        // verify if both date fields are filled
        var startDate = parseStartDate(bean, dateFormat);
        var endDate = parseEndDate(bean, dateFormat);
        if (startDate != null && endDate != null) {
            if (endDate.after(startDate)) {
                query.between(startDate, endDate);
            } else {
                try {
                    endDate = dateFormat.parse(bean.getEndDate() + " " + HOUR_23 + ":" + MINUTE_59);
                } catch (ParseException e) {
                    logger.error(PARSE_EXCEPTION_MESSAGE, e.getMessage(), e);
                }
                if (endDate != null && endDate.after(startDate)) {
                    query.between(startDate, endDate);
                } else { // still strange situation
                    query.after(startDate);
                }
            }
            //strange situations might happen
        } else if (startDate != null) {
            query.after(startDate);
        } else if (endDate != null) {
            query.before(endDate);
        }
    }

    private Date parseEndDate(QueryBean bean, SimpleDateFormat dateFormat) {
        Date endDate = null;
        try {
            endDate = dateFormat.parse(bean.getEndDate() + " " + bean.getEndHour() + ":" + bean.getEndMin());
        } catch (ParseException e) {
            logger.error(PARSE_EXCEPTION_MESSAGE, e.getMessage(), e);
        }
        return endDate;
    }

    private Date parseStartDate(QueryBean bean, SimpleDateFormat dateFormat) {
        Date startDate = null;
        try {
            startDate = dateFormat.parse(bean.getStartDate() + " " + bean.getStartHour() + ":" + bean.getStartMin());
        } catch (ParseException e) {
            logger.error(PARSE_EXCEPTION_MESSAGE, e.getMessage(), e);
        }
        return startDate;
    }
}
