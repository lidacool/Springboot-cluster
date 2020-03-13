package com.lee.agent;

import javassist.*;

import java.lang.annotation.Inherited;
import java.util.*;

/**
 * 启动项目类时，将所有项目的被@service @agent @...等注释的相关的类注入分类管理，用于后面查询调用等
 */
public class ClassUtil {

    private static Map<Class<?>, List<Class<?>>> annoClassesMap;

    public static void processAnnoClasses(String includeModels, Set<String> paths, Class<?>... annos) {
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new LoaderClassPath(ClassUtil.class.getClassLoader()));
        Map<Class<?>, List<CtClass>> annoClasses = new HashMap<>();
        List<Scanner.ClassEntry> classEntries = Scanner.scan(includeModels, "", paths);
        Set<CtClass> patched = new LinkedHashSet<>();
        for (Scanner.ClassEntry entry : classEntries) {
            try {
                CtClass ctClass = pool.get(entry.name);

                for (Class<?> anno : annos) {
                    if (isAnnotationPresient(ctClass,anno)){
                        putToMapList(annoClasses,anno,ctClass);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        annoClassesMap = new HashMap<>();
        for (Map.Entry<Class<?>, List<CtClass>> entry : annoClasses.entrySet()) {
            Class<?> key = entry.getKey();
            for (CtClass ctClass : entry.getValue()) {
                putToMapList(annoClassesMap, key, defineClass(ctClass));
            }
        }
    }

    private static boolean isAnnotationPresient(CtClass cc, Class<?> anno) {
        try {
            if (cc.getAnnotation(anno) != null) {
                return true;
            }
            if (anno.getAnnotation(Inherited.class) != null) {
                return Arrays.stream(cc.getInterfaces()).filter(ci -> isAnnotationPresient(ci, anno)).findAny().isPresent();
            }
        } catch (ClassNotFoundException | NotFoundException e) {
            //ignore
        }
        return false;
    }

    private static <T> void putToMapList(Map<Class<?>, List<T>> map, Class<?> key, T val) {
        List<T> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        list.add(val);
    }

    private static Class<?> defineClass(CtClass cc) {
        try {
            return cc.toClass();
        } catch (CannotCompileException e) {
            try {
                return Class.forName(cc.getName());
            } catch (ClassNotFoundException e1) {
                return null;
            }
        }
    }

    public static List<Class<?>> getClasses(Class<?> anno) {
        if (annoClassesMap==null){
            return Collections.emptyList();//web 端的未注入注释类 web端不处理内部业务
        }
        List<Class<?>> list = annoClassesMap.get(anno);
        return list == null ? Collections.emptyList() : list;
    }

}
