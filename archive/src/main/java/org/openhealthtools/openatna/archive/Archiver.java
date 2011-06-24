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
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.AtnaFactory;
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
 * @date Mar 12, 2010: 8:39:44 PM
 */

public class Archiver {

    static Log log = LogFactory.getLog("org.openhealthtools.openatna.archive.Archiver");

    public static final String EXT = ".oar";
    public static final String MESSAGES = "messages.xml";
    public static final String ENTITIES = "entities.xml";
    public static final String ERRORS = "errors.xml";
    protected static DateFormat archiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    protected static DateFormat nameFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");

    private boolean archiveMessages = true;
    private boolean archiveEntities = true;
    private boolean archiveErrors = true;
    private int pageSize = 100;
    private String archiveDirectory;
    private String archiveName;

    public Archiver(String propertiesLocation) {
        AtnaFactory.setPropertiesLocation(propertiesLocation);
    }

    public Archiver() {
    }

    public void archive() throws Exception {
        String name = nameFormat.format(new Date());
        setDefaultArchiveName(name);
        File dir = setDefaultArchiveDirectoy(archiveName);
        List<File> files = new ArrayList<File>();
        if (archiveMessages) {
            files.add(archiveMessages(dir));
        }
        if (archiveEntities) {
            files.add(archiveEntities(dir));
        }
        if (archiveErrors) {
            files.add(archiveErrors(dir));
        }
        ArchiveHandler.archive(archiveName, files, dir);
        ArchiveHandler.deleteFiles(dir, true);
    }

    private File archiveMessages(File directory) throws Exception {
        MessageDao dao = AtnaFactory.messageDao();
        MessageWriter writer = new MessageWriter();
        File ret = new File(directory, MESSAGES);
        FileOutputStream fout = new FileOutputStream(ret);
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(fout);
        int offset = 0;
        List<? extends MessageEntity> msgs = dao.getAll(offset, pageSize);
        writer.begin(w);
        writer.writeMessages(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = dao.getAll(offset, pageSize);
            writer.writeMessages(msgs, w);
            offset += msgs.size();
        }
        writer.finish(w);
        log.info("written " + offset + " messages");
        return ret;
    }

    /**
     * order is important:
     * <p/>
     * 1. codes
     * 2. naps
     * 3. sources
     * 4. participants
     * 5. objects
     *
     * @param directory
     * @return
     * @throws Exception
     */
    private File archiveEntities(File directory) throws Exception {
        File ret = new File(directory, ENTITIES);
        FileOutputStream fout = new FileOutputStream(ret);
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(fout);
        EntityWriter writer = new EntityWriter();
        writer.begin(w);
        archiveCodes(writer, w, pageSize);
        archiveNaps(writer, w, pageSize);
        archiveSources(writer, w, pageSize);
        archiveParticipants(writer, w, pageSize);
        archiveObjects(writer, w, pageSize);
        writer.finish(w);
        return ret;
    }

    private void archiveSources(EntityWriter writer, XMLStreamWriter w, int max) throws Exception {
        SourceDao dao = AtnaFactory.sourceDao();
        int offset = 0;
        List<? extends SourceEntity> msgs = dao.getAll(offset, max);
        writer.beginType(w, DataConstants.SOURCES);
        writer.writeSources(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= max) {
            msgs = dao.getAll(offset, max);
            writer.writeSources(msgs, w);
            offset += msgs.size();
        }
        writer.finishType(w);
        log.info("written " + offset + " sources");
    }

    private void archiveParticipants(EntityWriter writer, XMLStreamWriter w, int max) throws Exception {
        ParticipantDao dao = AtnaFactory.participantDao();
        int offset = 0;
        List<? extends ParticipantEntity> msgs = dao.getAll(offset, max);
        writer.beginType(w, DataConstants.PARTICIPANTS);
        writer.writeParticipants(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= max) {
            msgs = dao.getAll(offset, max);
            writer.writeParticipants(msgs, w);
            offset += msgs.size();
        }
        writer.finishType(w);
        log.info("written " + offset + " participants");
    }

    private void archiveObjects(EntityWriter writer, XMLStreamWriter w, int max) throws Exception {
        ObjectDao dao = AtnaFactory.objectDao();
        int offset = 0;
        List<? extends ObjectEntity> msgs = dao.getAll(offset, max);
        writer.beginType(w, DataConstants.OBJECTS);
        writer.writeObjects(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= max) {
            msgs = dao.getAll(offset, max);
            writer.writeObjects(msgs, w);
            offset += msgs.size();
        }
        writer.finishType(w);
        log.info("written " + offset + " objects");
    }

    private void archiveCodes(EntityWriter writer, XMLStreamWriter w, int max) throws Exception {
        CodeDao dao = AtnaFactory.codeDao();
        int offset = 0;
        List<? extends CodeEntity> msgs = dao.getAll(offset, max);
        writer.beginType(w, DataConstants.CODES);
        writer.writeCodes(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= max) {
            msgs = dao.getAll(offset, max);
            writer.writeCodes(msgs, w);
            offset += msgs.size();
        }
        writer.finishType(w);
        log.info("written " + offset + " codes");
    }

    private void archiveNaps(EntityWriter writer, XMLStreamWriter w, int max) throws Exception {
        NetworkAccessPointDao dao = AtnaFactory.networkAccessPointDao();
        int offset = 0;
        List<? extends NetworkAccessPointEntity> msgs = dao.getAll(offset, max);
        writer.beginType(w, DataConstants.NETWORK_ACCESS_POINTS);
        writer.writeNaps(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= max) {
            msgs = dao.getAll(offset, max);
            writer.writeNaps(msgs, w);
            offset += msgs.size();
        }
        writer.finishType(w);
        log.info("written " + offset + " network access points");
    }

    private File archiveErrors(File directory) throws Exception {
        ErrorDao dao = AtnaFactory.errorDao();
        ErrorWriter writer = new ErrorWriter();
        File ret = new File(directory, ERRORS);
        FileOutputStream fout = new FileOutputStream(ret);
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(fout);
        int offset = 0;
        List<? extends ErrorEntity> msgs = dao.getAll(offset, pageSize);
        writer.begin(w);
        writer.writeErrors(msgs, w);
        offset += msgs.size();
        while (msgs.size() >= pageSize) {
            msgs = dao.getAll(offset, pageSize);
            writer.writeErrors(msgs, w);
            offset += msgs.size();
        }
        writer.finish(w);
        log.info("written " + offset + " errors");
        return ret;
    }

    private File setDefaultArchiveDirectoy(String name) {
        if (name.indexOf(EXT) > -1) {
            name = name.substring(0, name.indexOf(EXT));
        }
        if (archiveDirectory == null) {
            archiveDirectory = System.getProperty("user.dir");
        }
        File dir = new File(archiveDirectory + File.separator + name);
        dir.mkdirs();
        return dir;
    }

    private void setDefaultArchiveName(String name) {
        if (archiveName == null) {
            archiveName = (name);
        }
        if (!archiveName.endsWith(EXT)) {
            archiveName += EXT;
        }
    }


    public boolean isArchiveMessages() {
        return archiveMessages;
    }

    public void setArchiveMessages(boolean archiveMessages) {
        this.archiveMessages = archiveMessages;
    }

    public boolean isArchiveEntities() {
        return archiveEntities;
    }

    public void setArchiveEntities(boolean archiveEntities) {
        this.archiveEntities = archiveEntities;
    }

    public boolean isArchiveErrors() {
        return archiveErrors;
    }

    public void setArchiveErrors(boolean archiveErrors) {
        this.archiveErrors = archiveErrors;
    }

    public String getArchiveDirectory() {
        return archiveDirectory;
    }

    public void setArchiveDirectory(String archiveDirectory) {
        this.archiveDirectory = archiveDirectory;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static void main(String[] args) throws Exception {
        Archiver a = new Archiver();
        a.setArchiveName("test");
        a.archive();
    }
}
