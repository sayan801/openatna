/*
 * Copyright (c) 2010 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation
 */

package org.openhealthtools.openatna.web;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openhealthtools.openatna.anom.Timestamp;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.Query;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Jan 19, 2010: 9:58:57 AM
 */

public class MessageController extends MultiActionController {

    private MessageDao messageDao;

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public ModelAndView query(HttpServletRequest request,
                              HttpServletResponse response, QueryBean queryBean) throws Exception {

        ModelMap modelMap = new ModelMap();
        Query q = createQuery(queryBean);
        if (q.hasConditionals()) {
            modelMap.addAttribute("messages", messageDao.getByQuery(q));
        } else {
            modelMap.addAttribute("messages", new ArrayList());
        }
        modelMap.addAttribute("queryBean", queryBean);
        return new ModelAndView("messageForm", modelMap);
    }

    private Query createQuery(QueryBean bean) {
        Query query = new Query();
        if (bean.getEventIdCode() != null && bean.getEventIdCode().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventIdCode(), Query.Target.EVENT_ID_CODE);
        }
        if (bean.getEventOutcome() != null && bean.getEventOutcome().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, Integer.parseInt(bean.getEventOutcome()), Query.Target.EVENT_OUTCOME);
        }
        if (bean.getObjectId() != null && bean.getObjectId().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getObjectId(), Query.Target.PARTICIPANT_ID);
        }
        if (bean.getSourceId() != null && bean.getSourceId().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getSourceId(), Query.Target.SOURCE_ID);
        }
        if (bean.getParticipantId() != null && bean.getParticipantId().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getParticipantId(), Query.Target.PARTICIPANT_ID);
        }
        if (bean.getEventAction() != null && bean.getEventAction().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventAction(), Query.Target.EVENT_ACTION);
        }
        if (bean.getEventTime() != null && bean.getEventTime().length() > 0) {
            Date ts = Timestamp.parseToDate(bean.getEventTime());

            if (ts != null) {
                query.addConditional(Query.Conditional.EQUALS, ts, Query.Target.EVENT_TIME);
            }
        }
        if (bean.getEventTypeCode() != null && bean.getEventTypeCode().length() > 0) {
            query.addConditional(Query.Conditional.EQUALS, bean.getEventTypeCode(), Query.Target.EVENT_TYPE_CODE);
        }
        return query;
    }


}
