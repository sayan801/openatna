package org.openhealthtools.openatna.archive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class ArchiveHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveHandler.class);
    private static final String JAR_SEP = "/";

    private static final ArrayList<String> ignores = new ArrayList<>();

    static {
        ignores.add(".DS_Store");
        ignores.add("CVS");
        ignores.add(".svn");
    }

    private ArchiveHandler() {
    }

    public static File archive(String jarName, List<File> files, File destDir) throws IOException {

        return archive(jarName, files, destDir, true);
    }

    public static File archive(String jarName, List<File> files, File destDir, boolean recursive) throws IOException {

        if (!destDir.exists() && !destDir.mkdirs()) {
            LOGGER.error("Cannot create directory: '{}'", destDir.getAbsolutePath());
        }
        File jar = new File(destDir, jarName);
        if (jar.exists() && !jar.delete()) {
            LOGGER.error("Cannot delete JAR file: '{}'", jar.getAbsolutePath());
        }
        File parent = destDir.getParentFile();
        if (parent == null) {
            parent = destDir;
        }
        jar = new File(parent, jarName);
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jar));
        writeManifest(jos);
        ArrayList<String> entries = new ArrayList<>();
        for (File file : files) {
            writeEntry(file, jos, file.getParentFile(), entries, recursive);
        }
        jos.flush();
        jos.close();
        return jar;
    }

    private static void writeEntry(File f, JarOutputStream jos, File build, List<String> entries, boolean recursive) throws IOException {

        if (f == null || shouldBeIgnored(f.getName())) {
            return;
        }
        String path = createPath(build, f);
        if (path == null) {
            return;
        }
        if (entries.contains(path)) {
            return;
        }
        entries.add(path);
        if (f.isDirectory()) {
            jos.putNextEntry(new ZipEntry(path));
            jos.closeEntry();
            if (recursive) {
                File[] childers = f.listFiles();
                if (childers != null) {
                    for (File childer : childers) {
                        writeEntry(childer, jos, build, entries, true);
                    }
                }
            }
        } else {
            try (FileInputStream in = new FileInputStream(f)) {
                byte[] bytes = new byte[64768];
                int c;
                jos.putNextEntry(new ZipEntry(path));
                while ((c = in.read(bytes)) != -1) {
                    jos.write(bytes, 0, c);
                }
                jos.closeEntry();
            }
        }
    }

    private static void writeManifest(JarOutputStream out) throws IOException {

        out.putNextEntry(new ZipEntry("META-INF/"));
        out.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        out.write("Manifest-Version: 1.0\n".getBytes(StandardCharsets.UTF_8));
        out.write(("Built-Date: " + Archiver.formatDate(new Date()) + "\n").getBytes(StandardCharsets.UTF_8));
        out.closeEntry();
    }

    private static String createPath(File build, File file) throws IOException {

        String root = build.getCanonicalPath();
        String entry = file.getCanonicalPath();
        String jarPath = entry.substring(root.length()).replace(File.separator, JAR_SEP);
        if (file.isDirectory() && !(jarPath.endsWith(JAR_SEP))) {
            jarPath += JAR_SEP;
        }
        if (jarPath.startsWith(JAR_SEP)) {
            jarPath = jarPath.substring(1);
        }

        return jarPath;
    }

    private static boolean shouldBeIgnored(String name) {

        for (String ignore : ignores) {
            if (name.equals(ignore)) {
                return true;
            }
        }
        return false;
    }

    public static List<File> copyFilesRecursive(File src, File dest) throws IOException {

        ArrayList<File> list = new ArrayList<>();
        if (!src.exists()) {
            throw new FileNotFoundException("Input file does not exist.");
        }
        if (src.isDirectory()) {
            if (!dest.exists()) {

                if (!dest.mkdirs()) {
                    LOGGER.error("Cannot create directory: '{}''", dest.getAbsolutePath());
                }
            } else if (!dest.isDirectory()) {
                throw new IOException("cannot write a directory to a file.");
            }
            list.add(dest);
            File[] srcFiles = src.listFiles();
            if (srcFiles != null) {
                for (File srcFile : srcFiles) {
                    list.addAll(copyFilesRecursive(srcFile, new File(dest, srcFile.getName())));
                }
            }
        } else {
            if (dest.exists() && dest.isDirectory()) {
                dest = new File(dest, src.getName());
            }
            try (FileInputStream source = new FileInputStream(src); FileOutputStream destination = new FileOutputStream(dest)) {

                BufferedInputStream in = new BufferedInputStream(source);
                BufferedOutputStream out = new BufferedOutputStream(destination);

                byte[] bytes = new byte[8192];
                int c;
                while ((c = in.read(bytes)) != -1) {
                    out.write(bytes, 0, c);
                }
                out.flush();
                out.close();
                in.close();
                list.add(dest);
            }
        }
        return list;
    }

    /**
     * deletes files recursively. can optionally delete the parent file as well. So, if the parent file
     * is not a directory, and incParent is false, then nothing will be deleted.
     *
     * @param parent    file to delete. If this is a directory then any children are deleted.
     * @param incParent boolean that determines if the parent file is also deleted.
     * @throws FileNotFoundException
     */
    public static void deleteFiles(File parent, boolean incParent) throws FileNotFoundException {

        if (!parent.exists()) {
            throw new FileNotFoundException("File does not exist.");
        }
        if (parent.isDirectory() && !(parent.listFiles() == null)) {
            File[] files = parent.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFiles(file, true);
                }
            }
        }
        if (incParent && !parent.delete()) {
            LOGGER.error("Cannot delete directory: '{}'", parent.getAbsolutePath());
        }
    }

    public static InputStream readMessages(File file) throws IOException {

        return readEntry(file, Archiver.MESSAGES);
    }

    public static InputStream readEntities(File file) throws IOException {

        return readEntry(file, Archiver.ENTITIES);
    }

    public static InputStream readErrors(File file) throws IOException {

        return readEntry(file, Archiver.ERRORS);
    }

    public static File extractMessages(File file, File destDir) throws IOException {

        return extractEntry(file, Archiver.MESSAGES, destDir);
    }

    public static File extractEntities(File file, File destDir) throws IOException {

        return extractEntry(file, Archiver.ENTITIES, destDir);
    }

    public static File extractErrors(File file, File destDir) throws IOException {

        return extractEntry(file, Archiver.ERRORS, destDir);
    }

    public static File extractEntry(JarFile jarFile, String path, File destDir) throws IOException {

        JarEntry entry = jarFile.getJarEntry(path);
        if (entry == null) {
            return null;
        }
        InputStream in = jarFile.getInputStream(entry);
        File f = createOutputFile(destDir, path);
        if (f.isDirectory()) {
            return f;
        }
        try (FileOutputStream out = new FileOutputStream(f)) {
            byte[] bytes = new byte[8192];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            out.flush();
            out.close();
            in.close();
        }
        return f;
    }

    public static InputStream readEntry(JarFile jarFile, String path) throws IOException {

        JarEntry entry = jarFile.getJarEntry(path);
        if (entry == null) {
            return null;
        }
        return jarFile.getInputStream(entry);
    }

    public static File extractEntry(File file, String path, File destDir) throws IOException {

        JarFile jarFile = new JarFile(file);
        return extractEntry(jarFile, path, destDir);
    }

    public static InputStream readEntry(File file, String path) throws IOException {

        JarFile jarFile = new JarFile(file);
        return readEntry(jarFile, path);
    }

    private static File createOutputFile(File destDir, String jarEntry) {

        int sep = jarEntry.indexOf(JAR_SEP);
        if (sep == -1) {
            return new File(destDir, jarEntry);
        }
        String path = jarEntry.substring(0, jarEntry.lastIndexOf(JAR_SEP)).replace(JAR_SEP, File.separator);
        File dir = new File(destDir, path);
        if (!dir.mkdirs()) {
            LOGGER.error("Cannot create directory: '{}'-'{}'", destDir, path);
        }
        String name = jarEntry.substring(jarEntry.lastIndexOf(JAR_SEP) + 1);
        if (name.length() == 0) {
            return dir;
        }
        return new File(dir, name);
    }

    public static void extract(File file, File destDir) throws IOException {

        JarFile jarFile = new JarFile(file);
        extract(jarFile, destDir);
    }

    public static void extract(JarFile jarFile, File destDir) throws IOException {

        Enumeration<JarEntry> en = jarFile.entries();
        while (en.hasMoreElements()) {
            JarEntry je = en.nextElement();
            String name = je.getName();
            extractEntry(jarFile, name, destDir);
        }
    }
}
