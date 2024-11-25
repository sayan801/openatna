package org.openhealthtools.openatna.web;

import org.apache.commons.lang3.StringUtils;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    private final ErrorDao errorDao;

    @Autowired
    public ErrorController(ErrorDao errorDao) {
        this.errorDao = errorDao;
    }

    @GetMapping(value = "/errors")
    public String errors(ModelMap model) {

        var queryBean = new QueryBean();
        model.addAttribute("queryBean", queryBean);
        return "errorForm";
    }

    @PostMapping(value = "/errors")
    public ModelAndView errors(QueryBean queryBean) throws AtnaPersistenceException {

        var modelMap = new ModelMap();
        List<? extends ErrorEntity> errorEntities = query(queryBean);
        if (errorEntities != null) {
            List<StringifiedError> list = new ArrayList<>();
            for (ErrorEntity ent : errorEntities) {
                list.add(new StringifiedError(ent));
            }
            modelMap.addAttribute("errors", list);
        } else {
            modelMap.addAttribute("errors", new ArrayList<StringifiedError>());
        }
        modelMap.addAttribute("queryBean", queryBean);
        return new ModelAndView("errorForm", modelMap);
    }

    private List<? extends ErrorEntity> query(QueryBean bean) throws AtnaPersistenceException {

        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<? extends ErrorEntity> errorEntities = null;
        Date startDate = null;
        if (StringUtils.isNotBlank(bean.getStartDate())) {
            String date = bean.getStartDate();
            try {
                startDate = simpleDateFormat.parse(date + " " + bean.getStartHour() + ":" + bean.getStartMin());
            } catch (ParseException e) {
                logger.error("ParseException: '{}'", e.getMessage(), e);
            }
        }
        Date endDate = null;
        if (StringUtils.isNotBlank(bean.getEndDate())) {
            String date = bean.getEndDate();
            try {
                endDate = simpleDateFormat.parse(date + " " + bean.getEndHour() + ":" + bean.getEndMin());
                if (startDate != null && endDate.before(startDate)) {
                    endDate = null;
                }
            } catch (ParseException e) {
                logger.error("ParseException: '{}'", e.getMessage(), e);
            }
        }
        String ip = null;
        if (bean.getSourceAddress() != null && bean.getSourceAddress().length() > 0) {
            ip = bean.getSourceAddress();
        }
        if (ip != null) {
            if (startDate != null) {
                if (endDate != null) {
                    errorEntities = errorDao.getBetween(ip, startDate, endDate);
                } else {
                    errorEntities = errorDao.getAfter(ip, startDate);
                }
            } else {
                if (endDate != null) {
                    errorEntities = errorDao.getBefore(ip, endDate);
                } else {
                    errorEntities = errorDao.getBySourceIp(ip);
                }
            }
        }
        return errorEntities;
    }
}
