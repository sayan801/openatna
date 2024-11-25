package org.openhealthtools.openatna.archive;

import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.PersistencePolicies;
import org.openhealthtools.openatna.audit.persistence.dao.*;
import org.openhealthtools.openatna.audit.persistence.model.*;
import org.openhealthtools.openatna.audit.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.audit.persistence.util.DataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    private final String archive;
    private final PersistencePolicies policies = new PersistencePolicies();
    private boolean loadMessages = true;
    private boolean loadEntities = true;
    private boolean loadErrors = true;
    private int pageSize = 100;

    public Loader(String archive) {
        this.archive = archive;
        initPolicies();

    }

    public Loader(String archive, String propertiesLocation) {
        this(archive);
        AtnaFactory.setPropertiesLocation(propertiesLocation);
    }

    public static void main(String[] args) throws Exception {
        Loader e = new Loader(System.getProperty("user.dir") + File.separator + "test.oar", "archive.properties");
        e.extract();

    }

    private void initPolicies() {

        policies.setAllowNewCodes(true);
        policies.setAllowNewNetworkAccessPoints(true);
        policies.setAllowNewSources(true);
        policies.setAllowNewParticipants(true);
        policies.setAllowNewObjects(true);
        policies.setAllowUnknownDetailTypes(true);
    }

    public void extract() throws Exception {

        File f = new File(archive);
        if (!f.exists() || f.length() == 0) {
            throw new Exception("archive does not exist");
        }
        if (loadEntities) {
            try (InputStream min = ArchiveHandler.readEntities(f)) {
                if (min != null) {
                    XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(min);
                    loadEntities(reader);
                    reader.close();
                } else {
                    LOGGER.info(" Input stream to '{}' message file is null", f.getAbsolutePath());
                }
            }
        }
        if (loadMessages) {
            try (InputStream min = ArchiveHandler.readMessages(f)) {
                if (min != null) {
                    XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(min);
                    loadMessages(reader);
                    reader.close();
                } else {
                    LOGGER.info(" Input stream to '{}' message file is null", f.getAbsolutePath());
                }
            }
        }

        if (loadErrors) {

            try (InputStream min = ArchiveHandler.readErrors(f)) {
                if (min != null) {
                    XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(min);
                    loadErrors(reader);
                    reader.close();
                } else {
                    LOGGER.info(" Input stream to '{}' message file is null", f.getAbsolutePath());
                }
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
            dao.save(msg, policies);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = mr.readMessages(pageSize, reader);
            for (MessageEntity msg : msgs) {
                dao.save(msg, policies);
            }
            total += msgs.size();
        }
        LOGGER.info("Read '{}' messages", total);
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
        LOGGER.info("Read '{}' errors", total);
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
            dao.save(msg, policies);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readCodes(pageSize, reader);
            for (CodeEntity msg : msgs) {
                dao.save(msg, policies);
            }
            total += msgs.size();
        }
        LOGGER.info("Read '{}' codes", total);
        er.endType(reader);
    }

    public void loadNaps(XMLEventReader reader, EntityReader er) throws Exception {

        NetworkAccessPointDao dao = AtnaFactory.networkAccessPointDao();
        er.beginType(reader, DataConstants.NETWORK_ACCESS_POINTS);
        int total = 0;
        List<? extends NetworkAccessPointEntity> msgs = er.readNaps(pageSize, reader);
        for (NetworkAccessPointEntity msg : msgs) {
            dao.save(msg, policies);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readNaps(pageSize, reader);
            for (NetworkAccessPointEntity msg : msgs) {
                dao.save(msg, policies);
            }
            total += msgs.size();
        }
        LOGGER.info("Read '{}' network access points", total);
        er.endType(reader);
    }

    public void loadSources(XMLEventReader reader, EntityReader er) throws Exception {
        SourceDao dao = AtnaFactory.sourceDao();
        er.beginType(reader, DataConstants.SOURCES);
        int total = 0;
        List<? extends SourceEntity> msgs = er.readSources(pageSize, reader);
        for (SourceEntity msg : msgs) {
            dao.save(msg, policies);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readSources(pageSize, reader);
            for (SourceEntity msg : msgs) {
                dao.save(msg, policies);
            }
            total += msgs.size();
        }
        LOGGER.info("Read '{}' sources", total);
        er.endType(reader);
    }

    public void loadParticipants(XMLEventReader reader, EntityReader er) throws Exception {

        ParticipantDao dao = AtnaFactory.participantDao();
        er.beginType(reader, DataConstants.PARTICIPANTS);
        int total = 0;
        List<? extends ParticipantEntity> msgs = er.readParticipants(pageSize, reader);
        for (ParticipantEntity msg : msgs) {
            dao.save(msg, policies);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readParticipants(pageSize, reader);
            for (ParticipantEntity msg : msgs) {
                dao.save(msg, policies);
            }
            total += msgs.size();
        }
        LOGGER.info("Read '{}' participants", total);
        er.endType(reader);
    }

    public void loadObjects(XMLEventReader reader, EntityReader er) throws Exception {

        ObjectDao dao = AtnaFactory.objectDao();
        er.beginType(reader, DataConstants.OBJECTS);
        int total = 0;
        List<? extends ObjectEntity> msgs = er.readObjects(pageSize, reader);
        for (ObjectEntity msg : msgs) {
            dao.save(msg, policies);
        }
        total += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = er.readObjects(pageSize, reader);
            for (ObjectEntity msg : msgs) {
                dao.save(msg, policies);
            }
            total += msgs.size();
        }
        LOGGER.info("Read '{}' objects", total);
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
}
