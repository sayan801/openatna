package org.openhealthtools.openatna.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.openhealthtools.openatna.audit.AtnaFactory;
import org.openhealthtools.openatna.audit.persistence.AtnaPersistenceException;
import org.openhealthtools.openatna.audit.persistence.dao.EntityDao;
import org.openhealthtools.openatna.audit.persistence.dao.MessageDao;
import org.openhealthtools.openatna.audit.persistence.model.MessageEntity;
import org.openhealthtools.openatna.audit.persistence.model.PersistentEntity;
import org.openhealthtools.openatna.audit.persistence.util.QueryString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Andrew Harrison
 */
public class Reporter {

    protected static final String[] entities = {
            "CodeEntity",
            "MessageEntity",
            "SourceEntity",
            "MessageSourceEntity",
            "ParticipantEntity",
            "MessageParticipantEntity",
            "ObjectEntity",
            "MessageObjectEntity",
            "NetworkAccessPointEntity",
            "ObjectDetailEntity",
            "ProvisionalEntity"
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(Reporter.class);
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy_hh-mm-ss");
    private ReportConfig config;
    private boolean isAtnaQuery = false;

    public Reporter(ReportConfig config) {
        this.config = config;
    }

    public static void main(String[] args) {

        try {
            InputStream in = new FileInputStream(args[0]);
            ReportConfig rc = ReportConfig.fromXml(in);
            Reporter r = new Reporter(rc);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Report: '{}'", r.report());
            }
        } catch (Exception e) {
            LOGGER.error("Exception: '{}'", e.getMessage(), e);
        }
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
                LOGGER.error("Exception: '{}'", e.getMessage(), e);
                String target = config.getTarget();
                if (target == null) {
                    report = guessReportFromHql(query);
                    if (report == null) {
                        throw new IllegalArgumentException("Could not parse query:" + query);
                    }
                } else {
                    report = getReportFromTarget(target);
                }
                if (report == null) {
                    throw new Exception("Could not parse query:" + query);
                }
                config.setQueryLanguage(ReportConfig.HQL);
                config.setReportInstance(report);
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
        File parent;
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
                LOGGER.error("Exception: '{}'", e.getMessage(), e);
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
        File res = new File(dir, name + "." + config.getOutputType().toLowerCase());

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
                MessageDao dao = AtnaFactory.messageDao();
                try {
                    List<? extends MessageEntity> l = dao.getByQuery(QueryString.parse(query));
                    return new EntityDataSource(l);
                } catch (AtnaPersistenceException e) {
                    LOGGER.error("AtnaPersistenceException: '{}'", e.getMessage(), e);
                    return null;
                }
            } else {
                Object dao = getObjectForTarget(report);
                if (dao != null) {
                    try {
                        Method m = dao.getClass().getMethod("getAll", new Class[0]);
                        List<? extends PersistentEntity> l = (List<? extends PersistentEntity>) m.invoke(dao, new Object[0]);
                        return new EntityDataSource(l);
                    } catch (Exception e) {
                        LOGGER.error("Exception: '{}'", e.getMessage(), e);
                    }
                }
            }
        } else if (config.getQueryLanguage().equals(ReportConfig.HQL)) {
            try {
                EntityDao dao = AtnaFactory.entityDao();
                List<? extends PersistentEntity> l = dao.query(query);
                return new EntityDataSource(l);
            } catch (AtnaPersistenceException e) {
                LOGGER.error("AtnaPersistenceException: '{}'", e.getMessage(), e);
                return null;
            }
        } else {
            throw new IllegalArgumentException("Unknown query language: " + config.getQueryLanguage());
        }
        return null;
    }

    private Object getObjectForTarget(String target) {

        switch (target) {
            case "MessageReport":
                return AtnaFactory.messageDao();
            case "CodeReport":
                return AtnaFactory.codeDao();
            case "ObjectReport":
                return AtnaFactory.objectDao();
            case "ParticipantReport":
                return AtnaFactory.participantDao();
            case "SourceReport":
                return AtnaFactory.sourceDao();
            case "NapReport":
                return AtnaFactory.networkAccessPointDao();
            case "ProvisionalReport":
                return AtnaFactory.provisionalDao();
        }
        return null;
    }

    /**
     * gets the most simple query which is just the target entity requested.
     * All entities of this type are returned.
     *
     * @param target
     * @return
     */
    private String getReportFromTarget(String target) {

        switch (target) {
            case ReportConfig.MESSAGES:
                return "MessageReport";
            case ReportConfig.CODES:
                return "CodeReport";
            case ReportConfig.OBJECTS:
                return "ObjectReport";
            case ReportConfig.PARTICIPANTS:
                return "ParticipantReport";
            case ReportConfig.SOURCES:
                return "SourceReport";
            case ReportConfig.NETWORK_ACCESS_POINTS:
                return "NapReport";
            case ReportConfig.PROVISIONAL_MESSAGES:
                return "ProvisionalReport";
        }
        return null;
    }

    private String getReportFromEntity(String s) {

        switch (s) {
            case "MessageEntity":
                return "MessageReport";
            case "CodeEntity":
                return "CodeReport";
            case "ObjectEntity":
                return "ObjectReport";
            case "ParticipantEntity":
                return "ParticipantReport";
            case "SourceEntity":
                return "SourceReport";
            case "NeworkAccessPointEntity":
                return "NapReport";
            case "MessageObjectEntity":
                return "MessageObjectReport";
            case "MessageParticipantEntity":
                return "MessageParticipantReport";
            case "MessageSourceEntity":
                return "MessageSourceReport";
            case "ProvisionalEntity":
                return "ProvisionalReport";
        }
        return null;
    }

    private String guessReportFromHql(String hql) {

        int min = Integer.MAX_VALUE;
        String ent = null;
        for (String entity : entities) {
            int index = hql.indexOf(entity);
            if (index > -1 && index < min) {
                min = index;
                ent = entity;
            }
        }
        if (ent != null && min > -1) {
            return getReportFromEntity(ent);
        }
        return null;
    }
}
