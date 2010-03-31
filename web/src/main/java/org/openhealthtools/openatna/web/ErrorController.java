/**
 *  Copyright (c) 2009-2010 University of Cardiff and others
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

package org.openhealthtools.openatna.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 19, 2010: 9:58:57 AM
 */

public class ErrorController extends MultiActionController {

    private ErrorDao errorDao;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ErrorDao getErrorDao() {
        return errorDao;
    }

    public void setErrorDao(ErrorDao errorDao) {
        this.errorDao = errorDao;
    }

    public ModelAndView errors(HttpServletRequest request,
                               HttpServletResponse response, QueryBean queryBean) throws Exception {

        ModelMap modelMap = new ModelMap();
        List<? extends ErrorEntity> ents = query(queryBean);
        if (ents != null) {
            List<StringifiedError> list = new ArrayList<StringifiedError>();
            for (ErrorEntity ent : ents) {
                list.add(new StringifiedError(ent));
            }
            modelMap.addAttribute("errors", list);
        } else {
            modelMap.addAttribute("errors", new ArrayList());
        }
        modelMap.addAttribute("queryBean", queryBean);
        return new ModelAndView("errorForm", modelMap);
    }

    private List<? extends ErrorEntity> query(QueryBean bean) throws AtnaPersistenceException {
        List<? extends ErrorEntity> ents = null;
        Date startDate = null;
        if (bean.getStartDate() != null && bean.getStartDate().length() > 0) {
            String date = bean.getStartDate();
            try {
                startDate = format.parse(date + " " + bean.getStartHour() + ":" + bean.getStartMin());
            } catch (ParseException e) {
            }
        }
        Date endDate = null;
        if (bean.getEndDate() != null && bean.getEndDate().length() > 0) {
            String date = bean.getEndDate();
            try {
                endDate = format.parse(date + " " + bean.getEndHour() + ":" + bean.getEndMin());
                if (startDate != null) {
                    if (endDate.before(startDate)) {
                        endDate = null;
                    }
                }
            } catch (ParseException e) {
            }
        }
        String ip = null;
        if (bean.getSourceAddress() != null && bean.getSourceAddress().length() > 0) {
            ip = bean.getSourceAddress();
        }
        if (ip != null) {
            if (startDate != null) {
                if (endDate != null) {
                    ents = errorDao.getBetween(ip, startDate, endDate);
                } else {
                    ents = errorDao.getAfter(ip, startDate);
                }
            } else {
                if (endDate != null) {
                    ents = errorDao.getBefore(ip, endDate);
                } else {
                    ents = errorDao.getBySourceIp(ip);
                }
            }
        }
        return ents;
    }

}