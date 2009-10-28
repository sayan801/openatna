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

package org.openhealthtools.openatna.report;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.File;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import org.hibernate.type.Type;
import org.openhealthtools.openatna.persistence.model.*;
import org.openhealthtools.openatna.persistence.model.codes.CodeEntity;
import org.openhealthtools.openatna.persistence.util.QueryString;
import org.openhealthtools.openatna.persistence.dao.DaoFactory;
import org.openhealthtools.openatna.persistence.dao.MessageDao;
import org.openhealthtools.openatna.persistence.dao.hibernate.SpringDaoFactory;
import org.openhealthtools.openatna.persistence.AtnaPersistenceException;

/**
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Oct 25, 2009: 10:13:04 PM
 * @date $Date:$ modified by $Author:$
 */

public class Reporter {

    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");

    private ReportConfig config;
    private DaoFactory factory = SpringDaoFactory.getFactory();
    private boolean isAtnaQuery = false;

    public Reporter(ReportConfig config) {
        this.config = config;
    }

    public String report() throws Exception {
        if (config.getTitle() == null) {
            config.setTitle("Audit Report");
        }
        if (config.getOutputDirectory() == null) {
            config.setOutputDirectory(System.getProperty("user.dir"));
        }
        if (config.getOutputFileName() == null) {
            config.setOutputFileName(format.format(new Date()));
        }
        if (config.getOutputType() == null) {
            config.setOutputType(ReportConfig.HTML);
        }
        if (config.getOutputType().equals(ReportConfig.HTML)) {
            config.put("IS_IGNORE_PAGINATION", Boolean.TRUE);
        }
        String query = config.getQuery();
        if (query == null) {
            throw new Exception("Query must be defined in ReportConfig");
        }
        String report = getReportFromTarget(query);
        if (report != null) {
            config.setQueryLanguage(ReportConfig.ATNA);
            config.setReportInstance(report);
        } else {
            try {
                QueryString.parse(query);
                config.setQueryLanguage(ReportConfig.ATNA);
                config.setReportInstance("MessageReport");
                isAtnaQuery = true;
            } catch (Exception e) {
                // TODO  HQL
            }
        }
        String input = getInputDirectory();
        if (input == null) {
            throw new Exception("Cannot determine input directory. This needs to be a directory where the .jasper files are.");
        }
        config.setInputDirectory(input);
        JRDataSource source = createDataSource(query, report);
        if (source == null) {
            throw new Exception("Could not create data source");
        }
        return compile(source);
    }

    private String getInputDirectory() {
        if (config.getInputDirectory() != null) {
            String input = config.getInputDirectory();
            if (!input.endsWith(File.separator)) {
                input += File.separator;
            }
            return input;
        }
        URL url = getClass().getResource("/AuditReport.jasper");
        File parent = null;
        if (url != null) {
            try {
                File f = new File(url.toURI());
                if (f.exists() && f.length() > 0) {
                    parent = f.getParentFile();
                    String input = parent.getAbsolutePath();
                    if (!input.endsWith(File.separator)) {
                        input += File.separator;
                    }
                    return input;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String compile(JRDataSource source) throws Exception {

        String dest = config.getOutputDirectory();
        File dir = new File(dest);
        String name = config.getOutputFileName();
        dir.mkdirs();
        File jasper = new File(config.getInputDirectory(), "AuditReport.jasper");

        File jrprint = new File(dir, name + ".jrprint");
        File res = new File(dir, name + "." + config.getOutputType().toString().toLowerCase());

        JasperFillManager.fillReportToFile(jasper.getAbsolutePath(),
                jrprint.getAbsolutePath(),
                config, source);
        if (config.getOutputType().equals(ReportConfig.PDF)) {
            JasperExportManager.exportReportToPdfFile(jrprint.getAbsolutePath(),
                    res.getAbsolutePath());
        } else if (config.getOutputType().equals(ReportConfig.HTML)) {
            JasperExportManager.exportReportToHtmlFile(jrprint.getAbsolutePath(),
                    res.getAbsolutePath());
        } else {
            return null;
        }

        return res.getAbsolutePath();
    }

    @SuppressWarnings("unchecked")
    private JRDataSource createDataSource(String query, String report) {

        if (config.getQueryLanguage().equals(ReportConfig.ATNA)) {
            if (isAtnaQuery) {
                MessageDao dao = factory.messageDao();
                try {
                    List<? extends MessageEntity> l = dao.getByQuery(QueryString.parse(query));
                    return new EntityDataSource(l);
                } catch (AtnaPersistenceException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Object dao = getObjectForTarget(report);
                if (dao != null) {
                    try {
                        Method m = dao.getClass().getMethod("getAll", new Class[0]);
                        List<? extends PersistentEntity> l = (List<? extends PersistentEntity>) m.invoke(dao, new Object[0]);
                        return new EntityDataSource(l);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (config.getQueryLanguage().equals(ReportConfig.HQL)) {
            System.out.println("HQL not supported yet... watch this space");
        } else {
            throw new IllegalArgumentException("Unknown query language: " + config.getQueryLanguage());
        }
        return null;
    }

    private Object getObjectForTarget(String target) {
        if (target.equals("MessageReport")) {
            return factory.messageDao();
        } else if (target.equals("CodeReport")) {
            return factory.codeDao();
        } else if (target.equals("ObjectReport")) {
            return factory.objectDao();
        } else if (target.equals("ParticipantReport")) {
            return factory.participantDao();
        } else if (target.equals("SourceReport")) {
            return factory.sourceDao();
        } else if (target.equals("NapReport")) {
            return factory.networkAccessPointDao();
        }
        return null;
    }

    private String getReportFromTarget(String target) {
        if (target.equals(ReportConfig.MESSAGES)) {
            return "MessageReport";
        } else if (target.equals(ReportConfig.CODES)) {
            return "CodeReport";
        } else if (target.equals(ReportConfig.OBJECTS)) {
            return "ObjectReport";
        } else if (target.equals(ReportConfig.PARTICIPANTS)) {
            return "ParticipantReport";
        } else if (target.equals(ReportConfig.SOURCES)) {
            return "SourceReport";
        } else if (target.equals(ReportConfig.NETWORK_ACCESS_POINTS)) {
            return "NapReport";
        }
        return null;
    }

    private String getReportFromHql(org.hibernate.Query hql) {
        Type[] types = hql.getReturnTypes();
        if (types.length == 1) {
            Class cls = types[0].getReturnedClass();
            if (MessageEntity.class.isAssignableFrom(cls)) {
                return "MessageReport";
            } else if (CodeEntity.class.isAssignableFrom(cls)) {
                return "CodeReport";
            } else if (ObjectEntity.class.isAssignableFrom(cls)) {
                return "ObjectReport";
            } else if (ParticipantEntity.class.isAssignableFrom(cls)) {
                return "ParticipantReport";
            } else if (SourceEntity.class.isAssignableFrom(cls)) {
                return "SourceReport";
            } else if (NetworkAccessPointEntity.class.isAssignableFrom(cls)) {
                return "NapReport";
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            InputStream in = Reporter.class.getResourceAsStream("/rc.xml");
            ReportConfig rc = ReportConfig.fromXml(in);
            Reporter r = new Reporter(rc);
            System.out.println(r.report());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
