package com.lee.agent;

import com.lee.util.string.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Scanner {

    private static final String MANFEST_CLASS_PATH = "Class-Path";
    private static final String WIN_FILE_SEPARATOR = "\\";
    // 文件分隔符"\"
    private static final String FILE_SEPARATOR = "/";
    // package扩展名分隔符
    private static final String PACKAGE_SEPARATOR = ".";
    // java类文件的扩展名
    private static final String CLASS_FILE_EXT = ".class";
    // jar类文件的扩展名
    private static final String JAR_FILE_EXT = ".jar";

    private static boolean isJarFile(String path) {
        return path.endsWith(JAR_FILE_EXT);
    }

    private static boolean isInsidePath(String path) {
        return path.contains("!/");
    }


    public static List<ClassEntry> scan(String includes, String excludes, Set<String> exClassPathes) {
        return scan(new ScanMatcher(includes), new ScanMatcher(excludes), exClassPathes);
    }

    public static List<ClassEntry> scan(ScanMatcher includes, ScanMatcher excludes, Set<String> exClassPathes) {
        List<ClassEntry> ret = new ArrayList<>();
        try {
            scanClassPathes(includes, excludes, ret, getClassPathes(includes, excludes));
            scanClassPathes(includes, excludes, ret, exClassPathes);
        } catch (Throwable t) {
            t.printStackTrace();
            // ignore
        }
        return ret;
    }

    public static List<String> getClassPathes(ScanMatcher includes, ScanMatcher excludes) {
        List<String> ret = new ArrayList<>();
        try {
            Set<String> classPathes = getClassPathes();
            for (String path : classPathes) {
                if (!isJarFile(path) || (includes.slack(path) && !excludes.strict(path)))
                    ret.add(path);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // ignore
        }
        return ret;
    }


    /**
     * 获取项目的所有classpath ，包括 APP_CLASS_PATH 和所有的jar文件
     */
    private static Set<String> getClassPathes() throws Exception {
        Set<String> set = new LinkedHashSet<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        while (set.isEmpty() && loader != null) {
            if (loader instanceof URLClassLoader) {
                Arrays.stream(((URLClassLoader) loader).getURLs()).map(url -> fromURIPath(url.getPath())).forEach(set::add);
            }
            loader = loader.getParent();
        }

        for (String cp : set.stream().filter(p -> isJarFile(p) && !isInsidePath(p)).collect(Collectors.toList())) {
            File newFile = new File(URLDecoder.decode(cp, "utf-8"));
            if(!newFile.exists()) continue;
            JarFile jarFile = new JarFile(newFile);
            String manfest = jarFile.getManifest().getMainAttributes().getValue(MANFEST_CLASS_PATH);
            if (!StringUtil.isEmpty(manfest)) {
                for (String c : manfest.split("\\s+")) {
                    if (c.contains(":"))// 带路径协议
                        set.add(fromURIPath(new URL(c).getPath()));
                    else if (c.endsWith(".jar"))// 单纯文件路径
                        set.add(new File(URLDecoder.decode(c, "utf-8")).getAbsolutePath());
                }
            }
            jarFile.close();
        }
        return set;
    }

    public static String fromURIPath(String path) {
        String p = path;
        // "/foo!/" --> "/foo"
        if (p.length() > 2 && p.endsWith("!/")) {// jar path
            p = p.substring(0, p.length() - 2);
        }
        if ((p.length() > 2) && (p.charAt(2) == ':')) {// win path
            // "/c:/foo" --> "c:/foo"
            p = p.substring(1);
            // "c:/foo/" --> "c:/foo", but "c:/" --> "c:/"
            if ((p.length() > 3) && p.endsWith("/"))
                p = p.substring(0, p.length() - 1);
        } else if ((p.length() > 1) && p.endsWith("/")) {// unix path
            // "/foo/" --> "/foo"
            p = p.substring(0, p.length() - 1);
        }
        if (p.startsWith("file:")) {
            p = p.substring(5);//rm file:
        }
        return p;
    }

    private static void scanClassPathes(ScanMatcher includes, ScanMatcher excludes, List<ClassEntry> ret, Collection<String> classPathes) throws Exception {
        for (String path : classPathes) {
            for (ClassEntry clazz : getFromPath(path)) {
                if (includes.slack(clazz.name) && !excludes.strict(clazz.name)) {
                    ret.add(clazz);
                }
            }
        }
    }

    private static Set<ClassEntry> getFromPath(String path) throws Exception {
        if (isInsidePath(path)) {
            return getFromInsidePath(path);
        }
        if (isJarFile(path)) {
            return getFromJar(new File(URLDecoder.decode(path, "utf-8")));
        }
        return getFromDir(new File(URLDecoder.decode(path, "utf-8")));
    }


    /**
     * 得到文件夹下所有class的全包名
     */
    private static Set<ClassEntry> getFromDir(File file) {
        Set<File> files = getClassFiles(file);
        Set<ClassEntry> classes = new LinkedHashSet<>();
        for (File f : files) {
            classes.add(new ClassEntry(file, f));
        }
        return classes;
    }
    /**
     * 获取文件下的所有.class文件
     */
    private static Set<File> getClassFiles(File file) {
        // 获取所有文件
        Set<File> files = getFiles(file);
        Set<File> classes = new LinkedHashSet<>();
        // 只保留.class 文件
        for (File f : files) {
            if (f.getName().endsWith(CLASS_FILE_EXT)) {
                classes.add(f);
            }
        }
        return classes;
    }
    /**
     * 获取文件下的所有文件(递归)
     */
    private static Set<File> getFiles(File file) {
        Set<File> files = new LinkedHashSet<>();
        if (!file.isDirectory()) {
            files.add(file);
        } else {
            File[] subFiles = file.listFiles();
            for (File f : subFiles) {
                files.addAll(getFiles(f));
            }
        }
        return files;
    }
    /**
     * 获取jar文件里的所有class文件名
     */
    private static Set<ClassEntry> getFromJar(File file) throws Exception {
        JarFile jarFile = new JarFile(file);
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            Set<ClassEntry> classes = new LinkedHashSet<>();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(CLASS_FILE_EXT)) {
                    classes.add(new ClassEntry(entry));
                }
            }
            return classes;
        } finally {
            jarFile.close();
        }
    }

    private static Set<ClassEntry> getFromInsidePath(String path) throws Exception {
        String[] _path = path.split("!/");
        if (_path.length >= 2) {
            try (JarFile jarFile = new JarFile(Paths.get(new URI(_path[0])).toFile())) {
                if (isJarFile(_path[1])) {// jar
                    JarEntry entry = jarFile.getJarEntry(_path[1]);
                    return getFromJarStream(jarFile.getInputStream(entry));
                } else {// folder
                    Enumeration<JarEntry> entries = jarFile.entries();
                    Set<ClassEntry> classes = new LinkedHashSet<ClassEntry>();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = (JarEntry) entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(_path[1]) && name.endsWith(CLASS_FILE_EXT)) {
                            String className = name.substring(_path[1].length(), entry.getName().indexOf(CLASS_FILE_EXT)).replace(FILE_SEPARATOR, PACKAGE_SEPARATOR);
                            classes.add(new ClassEntry(className, entry.getSize(), entry.getTime()));
                        }
                    }
                }
            }
        }
        return new LinkedHashSet<>();
    }

    private static Set<ClassEntry> getFromJarStream(InputStream input) throws Exception {
        Set<ClassEntry> classes = new LinkedHashSet<>();
        try (JarInputStream jarInput = new JarInputStream(input);) {
            JarEntry next;
            while ((next = jarInput.getNextJarEntry()) != null) {
                if (next.getName().endsWith(CLASS_FILE_EXT)) {
                    classes.add(new ClassEntry(next));
                }
            }
        }
        return classes;
    }

    public static class ClassEntry {

        public final String name;
        public final long size;
        public final long modifyTime;

        public ClassEntry(File root, File file) {
            String fileName = root.toPath().relativize(file.toPath()).toString().replace(WIN_FILE_SEPARATOR, FILE_SEPARATOR);
            this.name = fileName.substring(0, fileName.indexOf(CLASS_FILE_EXT)).replace(FILE_SEPARATOR, PACKAGE_SEPARATOR);
            this.size = file.length();
            this.modifyTime = file.lastModified();
        }

        public ClassEntry(JarEntry entry) {
            this.name = entry.getName().substring(0, entry.getName().indexOf(CLASS_FILE_EXT)).replace(FILE_SEPARATOR, PACKAGE_SEPARATOR);
            this.size = entry.getSize();
            this.modifyTime = entry.getTime();
        }

        public ClassEntry(String name, long size, long modifyTime) {
            this.name = name;
            this.size = size;
            this.modifyTime = modifyTime;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    public static class ScanMatcher {

        Pattern[] clsPatterns;
        Pattern[] jarPatterns;

        public ScanMatcher(String res) {
            if (res == null || res.length() == 0) {
                clsPatterns = new Pattern[0];
                jarPatterns = new Pattern[0];
                return;
            }

            String[] regs = res.split(";");
            List<Pattern> clsPatterns = new ArrayList<>();
            List<Pattern> jarPatterns = new ArrayList<>();
            for (int i = 0; i < regs.length; i++) {
                String reg = regs[i];
                List<Pattern> list = reg.endsWith(".jar") ? jarPatterns : clsPatterns;
                reg = reg.endsWith("*") ? reg : reg + "$";
                reg = reg.replace(".", "\\.");
                reg = reg.replace("*", ".*");
                list.add(Pattern.compile(reg));
            }

            this.jarPatterns = jarPatterns.toArray(new Pattern[0]);
            this.clsPatterns = clsPatterns.toArray(new Pattern[0]);
        }

        private boolean match(String path, boolean nilMatch) {//
            if (path.endsWith(".jar")) {
                return match0(jarPatterns, path, nilMatch);
            }
            return match0(clsPatterns, path, nilMatch);
        }

        public boolean strict(String path) {
            return match(path, false);
        }

        public boolean slack(String path) {
            return match(path, true);
        }

        private boolean match0(Pattern[] patterns, String path, boolean nilMatch) {
            return (nilMatch && patterns.length == 0) || match0(patterns, path);
        }

        private boolean match0(Pattern[] patterns, String path) {
            for (Pattern p : patterns) {
                if (p.matcher(path).find()) {
                    return true;
                }
            }
            return false;
        }
    }
}
