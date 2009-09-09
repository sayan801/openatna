/*
 * Copyright (c) 2009 University of Cardiff and others.
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

package org.openhealthtools.openatna.persistence.test;

import org.openhealthtools.openatna.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.persistence.dao.*;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.*;
import org.openhealthtools.openatna.util.Base64;


/**
 * Populates the DB with dummy data
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Sep 9, 2009: 9:01:02 PM
 * @date $Date:$ modified by $Author:$
 */

public class Populate {

    public static final String EVENT_ID = "EV-ID-";
    public static final String EVENT_TYPE = "EV-TYPE-";
    public static final String PARTICIPANT = "ACT-PART-";
    public static final String OBJECT_ID_TYPE = "OB-ID-TYPE-";
    public static final String SOURCE = "AUD-SRC-";

    public static void populate(int num) throws AtnaPersistenceException {
        DaoFactory factory = SpringDaoFactory.getFactory();
        CodeDao dao = factory.codeDao();

        for (int i = 0; i < num; i++) {
            // different codes
            EventIdCodeEntity evid = new EventIdCodeEntity();
            loadCode(dao, evid, EVENT_ID + i);
            EventTypeCodeEntity evtype = new EventTypeCodeEntity();
            loadCode(dao, evtype, EVENT_TYPE + i);
            ParticipantCodeEntity ap = new ParticipantCodeEntity();
            loadCode(dao, ap, PARTICIPANT + i);
            ObjectIdTypeCodeEntity obidtype = new ObjectIdTypeCodeEntity();
            loadCode(dao, obidtype, OBJECT_ID_TYPE + i);
            SourceCodeEntity as = new SourceCodeEntity();
            loadCode(dao, as, SOURCE + i);

            // network access points
            NetworkAccessPointEntity nap = new NetworkAccessPointEntity();
            nap.setType(1);
            nap.setIdentifier("192.168.0." + i);
            loadNetworkAccessPoint(factory, nap);
        }
        // sources, objects, participants
        for (int i = 0; i < num; i++) {
            loadAuditSources(factory, "AuditSource-" + i, i);
            loadActiveParticiapants(factory, "ActiveParticipants-" + i, i);
            loadParticiapantObjects(factory, "ParticipantObject-" + i, i);
        }
    }

    private static void loadCode(CodeDao dao, CodeEntity code, String codeName) throws AtnaPersistenceException {
        code.setCode(codeName);
        code.setCodeSystem(codeName + "-System");
        code.setCodeSystemName(codeName + "-Name");
        code.setDisplayName(codeName);
        dao.save(code);
    }

    private static void loadNetworkAccessPoint(DaoFactory factory, NetworkAccessPointEntity nap) throws AtnaPersistenceException {
        NetworkAccessPointDao napd = factory.networkAccessPointDao();
        napd.save(nap);
    }

    private static void loadAuditSources(DaoFactory factory, String prefix, int num) throws AtnaPersistenceException {
        SourceEntity source = new SourceEntity();
        source.setSourceId(prefix + "-id");
        source.setEnterpriseSiteId(prefix + "-enterpriseSiteId");
        SourceCodeEntity sce = new SourceCodeEntity();
        sce.setCode(SOURCE + num);
        sce.setCodeSystem(SOURCE + num + "-System");
        sce.setCodeSystemName(SOURCE + num + "-Name");
        source.getSourceTypeCodes().add(sce);
        SourceDao dao = factory.sourceDao();
        dao.save(source);
    }

    private static void loadActiveParticiapants(DaoFactory factory, String prefix, int num) throws AtnaPersistenceException {
        ParticipantEntity pe = new ParticipantEntity();
        pe.setUserId(prefix + "-id");
        pe.setUserName(prefix + " Name");
        pe.setAlternativeUserId(prefix + "-alt-id");
        ParticipantCodeEntity sce = new ParticipantCodeEntity();
        sce.setCode(PARTICIPANT + num);
        sce.setCodeSystem(PARTICIPANT + num + "-System");
        sce.setCodeSystemName(PARTICIPANT + num + "-Name");
        pe.getCodes().add(sce);
        ParticipantDao dao = factory.participantDao();
        dao.save(pe);
    }

    private static void loadParticiapantObjects(DaoFactory factory, String prefix, int num) throws AtnaPersistenceException {
        ObjectEntity oe = new ObjectEntity();
        oe.setObjectId(prefix + "-id");
        oe.setObjectName(prefix + " Name");
        oe.setObjectSensitivity("N");
        oe.setObjectTypeCode(new Short("1"));
        oe.setObjectTypeCodeRole(new Short("2"));
        ObjectIdTypeCodeEntity codeEntity = new ObjectIdTypeCodeEntity();
        codeEntity.setCode(OBJECT_ID_TYPE + num);
        codeEntity.setCodeSystem(OBJECT_ID_TYPE + num + "-System");
        codeEntity.setCodeSystemName(OBJECT_ID_TYPE + num + "-Name");
        oe.setObjectIdTypeCode(codeEntity);

        ObjectDetailEntity detail = new ObjectDetailEntity();
        detail.setType(prefix + "-Detail");
        detail.setValue(Base64.encode("THIS IS SOME DETAIL WHICH IS NOT VERY INTERESTING".getBytes()));
        ObjectDetailDao detaildao = factory.objectDetailDao();
        detaildao.save(detail);
        oe.getObjectDetails().add(detail);
        ObjectDao dao = factory.objectDao();
        dao.save(oe);
    }


    private static int next(int max) {
        return (int) Math.round(Math.random() % max);
    }

    public static void main(String[] args) {
        try {
            Populate.populate(5);
        } catch (AtnaPersistenceException e) {
            e.printStackTrace();
            System.out.println("ERROR: " + e.getError());
        }
    }


}
