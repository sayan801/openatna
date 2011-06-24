/**
 * Copyright (c) 2009-2011 University of Cardiff and others.
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

package org.openhealthtools.openatna.archive;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.CodeDao;
import org.openhealthtools.openatna.audit.persistence.dao.ErrorDao;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.dao.NetworkAccessPointDao;
import org.openhealthtools.openatna.audit.persistence.dao.ObjectDao;
import org.openhealthtools.openatna.audit.persistence.dao.ParticipantDao;
import org.openhealthtools.openatna.audit.persistence.dao.SourceDao;
import org.openhealthtools.openatna.audit.persistence.model.ErrorEntity;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.NetworkAccessPointEntity;
import org.openhealthtools.openatna.audit.persistence.model.ObjectEntity;
import org.openhealthtools.openatna.audit.persistence.model.ParticipantEntity;
import org.openhealthtools.openatna.audit.persistence.model.SourceEntity;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;

/**
 * @author Andrew Harrison
 * @version 1.0.0
 * @date Mar 12, 2010: 11:00:59 PM
 */

public class Loader {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.archive.Loader");

    private String archive;
    private boolean loadMessages = true;
    private boolean loadEntities = true;
    private boolean loadErrors = true;
    private int pageSize = 100;
    private PersistencePolicies pp = new PersistencePolicies();

    public Loader(String archive) {
        this.archive = archive;
        initPolicies();

    }

    public Loader(String archive, String propertiesLocation) {
        this(archive);
        AtnaFactory.setPropertiesLocation(propertiesLocation);
    }

    private void initPolicies() {
        pp.setAllowNewCodes(true);
        pp.setAllowNewNetworkAccessPoints(true);
        pp.setAllowNewSources(true);
        pp.setAllowNewParticipants(true);
        pp.setAllowNewObjects(true);
        pp.setAllowUnknownDetailTypes(true);
    }

    public void extract() throws Exception {
        File f = new File(archive);
        if (!f.exists() || f.length() == 0) {
            throw new Exception("archive does not exist");
        }
        if (loadEntities) {
            InputStream min = ArchiveHandler.readEntities(f);
            if (min != null) {
                XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(min);
                loadEntities(reader);
                reader.close();
            } else {
                log.info(" Input stream to " + f.getAbsolutePath() + " message file is null");
            }
        }
        if (loadMessages) {
            InputStream min = ArchiveHandler.readMessages(f);
            if (min != null) {
                XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(min);
                loadMessages(reader);
                reader.close();
            } else {
                log.info(" Input stream to " + f.getAbsolutePath() + " message file is null");
            }
        }

        if (loadErrors) {
            InputStream min = ArchiveHandler.readErrors(f);
            if (min != null) {
                XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(min);
                loadErrors(reader);
                reader.close();
            } else {
                log.info(" Input stream to " + f.getAbsolutePath() + " message file is null");
            }
        }


    }

    public void loadMessages(XMLEventReader reader) throws Exception {
        MessageDao dao = AtnaFactory.messageDao();
        MessageReader mr = new MessageReader();
        mr.begin(reader);
        int total = 0;
        List<? extends MessageEntity> msgs = mr.readMessages(pageSize, reader);
        for (MessageEntity msg : msgs) {
            dao.save(msg, pp);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = mr.readMessages(pageSize, reader);
            for (MessageEntity msg : msgs) {
                dao.save(msg, pp);
            }
            total += msgs.size();
        }
        log.info("read " + total + " messages");
    }


    public void loadErrors(XMLEventReader reader) throws Exception {
        ErrorDao dao = AtnaFactory.errorDao();
        ErrorReader mr = new ErrorReader();
        mr.begin(reader);
        int total = 0;
        List<? extends ErrorEntity> msgs = mr.readErrors(pageSize, reader);
        for (ErrorEntity msg : msgs) {
            dao.save(msg);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = mr.readErrors(pageSize, reader);
            for (ErrorEntity msg : msgs) {
                dao.save(msg);
            }
            total += msgs.size();
        }
        log.info("read " + total + " errors");
    }

    public void loadEntities(XMLEventReader reader) throws Exception {
        EntityReader er = new EntityReader();
        er.begin(reader);
        loadCodes(reader, er);
        loadNaps(reader, er);
        loadSources(reader, er);
        loadParticipants(reader, er);
        loadObjects(reader, er);

    }

    public void loadCodes(XMLEventReader reader, EntityReader er) throws Exception {
        CodeDao dao = AtnaFactory.codeDao();
        er.beginType(reader, DataConstants.CODES);
        int total = 0;
        List<? extends CodeEntity> msgs = er.readCodes(pageSize, reader);
        for (CodeEntity msg : msgs) {
            dao.save(msg, pp);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readCodes(pageSize, reader);
            for (CodeEntity msg : msgs) {
                dao.save(msg, pp);
            }
            total += msgs.size();
        }
        log.info("read " + total + " codes");
        er.endType(reader);
    }

    public void loadNaps(XMLEventReader reader, EntityReader er) throws Exception {
        NetworkAccessPointDao dao = AtnaFactory.networkAccessPointDao();
        er.beginType(reader, DataConstants.NETWORK_ACCESS_POINTS);
        int total = 0;
        List<? extends NetworkAccessPointEntity> msgs = er.readNaps(pageSize, reader);
        for (NetworkAccessPointEntity msg : msgs) {
            dao.save(msg, pp);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readNaps(pageSize, reader);
            for (NetworkAccessPointEntity msg : msgs) {
                dao.save(msg, pp);
            }
            total += msgs.size();
        }
        log.info("read " + total + " network access points");
        er.endType(reader);
    }

    public void loadSources(XMLEventReader reader, EntityReader er) throws Exception {
        SourceDao dao = AtnaFactory.sourceDao();
        er.beginType(reader, DataConstants.SOURCES);
        int total = 0;
        List<? extends SourceEntity> msgs = er.readSources(pageSize, reader);
        for (SourceEntity msg : msgs) {
            dao.save(msg, pp);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readSources(pageSize, reader);
            for (SourceEntity msg : msgs) {
                dao.save(msg, pp);
            }
            total += msgs.size();
        }
        log.info("read " + total + " sources");
        er.endType(reader);
    }

    public void loadParticipants(XMLEventReader reader, EntityReader er) throws Exception {
        ParticipantDao dao = AtnaFactory.participantDao();
        er.beginType(reader, DataConstants.PARTICIPANTS);
        int total = 0;
        List<? extends ParticipantEntity> msgs = er.readParticipants(pageSize, reader);
        for (ParticipantEntity msg : msgs) {
            dao.save(msg, pp);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readParticipants(pageSize, reader);
            for (ParticipantEntity msg : msgs) {
                dao.save(msg, pp);
            }
            total += msgs.size();
        }
        log.info("read " + total + " participants");
        er.endType(reader);
    }

    public void loadObjects(XMLEventReader reader, EntityReader er) throws Exception {
        ObjectDao dao = AtnaFactory.objectDao();
        er.beginType(reader, DataConstants.OBJECTS);
        int total = 0;
        List<? extends ObjectEntity> msgs = er.readObjects(pageSize, reader);
        for (ObjectEntity msg : msgs) {
            dao.save(msg, pp);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readObjects(pageSize, reader);
            for (ObjectEntity msg : msgs) {
                dao.save(msg, pp);
            }
            total += msgs.size();
        }
        log.info("read " + total + " objects");
        er.endType(reader);
    }

    public boolean isLoadMessages() {
        return loadMessages;
    }

    public void setLoadMessages(boolean loadMessages) {
        this.loadMessages = loadMessages;
    }

    public boolean isLoadEntities() {
        return loadEntities;
    }

    public void setLoadEntities(boolean loadEntities) {
        this.loadEntities = loadEntities;
    }

    public boolean isLoadErrors() {
        return loadErrors;
    }

    public void setLoadErrors(boolean loadErrors) {
        this.loadErrors = loadErrors;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static void main(String[] args) throws Exception {
        Loader e = new Loader(System.getProperty("user.dir") + File.separator + "test.oar", "archive.properties");
        e.extract();

    }
}
