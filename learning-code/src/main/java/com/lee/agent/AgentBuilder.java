package com.lee.agent;

import javassist.*;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class AgentBuilder {


    public static void setAgents(Object agentInterfaceBean, LinkedList agentImplBeans) {
        ((IAgent) agentInterfaceBean)._setDeletages(agentImplBeans);
    }


    public static interface IAgent {

        public void _appendDelegate(Object delegate);

        public void _removeDelegate(Object delegate);

        public void _forEachDeletage(Consumer<?> consumer);

        void _setDeletages(LinkedList delegates);
    }

    public static Class<?> buildClass(Class<?> clazz) {
        boolean ignoreError = clazz.isAnnotationPresent(Agent.class) ? clazz.getAnnotation(Agent.class).ingoreError() : true;
        return buildClass(clazz, ignoreError);
    }

    public synchronized static Class<?> buildClass(Class<?> cclazz, boolean ignoreError) {
        try {
            Class<?> type = cclazz;
            ClassPool pool = ClassPool.getDefault();
            CtClass ctParent = pool.get(type.getName());
            String proxyName = type.getName() + "$_Agent";

            Class<?> proxyClass = null;
            try {
                proxyClass = Class.forName(proxyName);
            } catch (ClassNotFoundException e) {
            }

            if (proxyClass == null) {
                CtClass ctClass = pool.makeClass(proxyName);
                if (Modifier.isInterface(type.getModifiers())) {
                    ctClass.addInterface(ctParent);
                } else {
                    ctClass.setSuperclass(ctParent);
                }
                ctClass.addInterface(pool.get(IAgent.class.getName()));

                ctClass.addField(CtField.make("private volatile java.util.LinkedList delegates = new java.util.LinkedList();", ctClass));

                ctClass.addField(CtField.make("private static final org.slf4j.Logger _logger = org.slf4j.LoggerFactory.getLogger(java.lang.Object.class);", ctClass));

                // CtMethod appendDelegateMethod = CtMethod.make("public void _appendDelegate(java.lang.Object bean){this.delegates.add(bean);}", ctClass);
                // ctClass.addMethod(appendDelegateMethod);
                //
                // CtMethod removeDelegateMethod = CtMethod.make("public void  _removeDelegate(java.lang.Object bean){this.delegates.remove(bean);}", ctClass);
                // ctClass.addMethod(removeDelegateMethod);

                CtMethod forEachDelegateMethod = CtMethod.make("public void _forEachDeletage(java.util.function.Consumer c){this.delegates.forEach(c);}", ctClass);
                ctClass.addMethod(forEachDelegateMethod);

                CtMethod setDelegatesMethod = CtMethod.make("public void _setDeletages(java.util.LinkedList delegates){this.delegates=delegates;}", ctClass);
                ctClass.addMethod(setDelegatesMethod);

                CtMethod[] ctMethods = ctParent.getMethods();
                for (CtMethod ctMethod : ctMethods) {
                    if (Object.class.getName().equals(ctMethod.getDeclaringClass().getName()))
                        continue;
                    if (Modifier.isStatic(ctMethod.getModifiers()))
                        continue;

                    StringBuilder body = new StringBuilder("{")
                            .append("java.util.Iterator it = this.delegates.iterator();")
                            .append("while(it.hasNext()){")
                            .append(cclazz.getName()).append(" delegate = ").append("(").append(cclazz.getName()).append(") it.next();");
                    if (ctMethod.getReturnType() == CtClass.voidType) {
                        body
                                .append(tryBlock(ignoreError))
                                .append(invokeBlock(ctMethod))
                                .append(catchBlock(ignoreError))
                                .append("}");//结束while
                    }
                    body.append("}");

                    ctClass.addMethod(copy(ctMethod, body.toString(), ctClass));
                }
                proxyClass = ctClass.toClass();
            }
            return proxyClass;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

        private static Object tryBlock(boolean ignoreError) {
            return ignoreError ? "try{" : "";
        }

        private static Object catchBlock(boolean ignoreError) {
            return ignoreError ? "}catch(Throwable t){_logger.error(\"agent ignored error\",t);}" : "";
        }

        private static String invokeBlock(CtMethod ctMethod) throws NotFoundException {
            return new StringBuilder()
                    .append("delegate.").append(ctMethod.getName()).append("(")
                    .append(String.join(",", IntStream.rangeClosed(1, ctMethod.getParameterTypes().length).mapToObj(idx -> "$" + idx).toArray(String[]::new)))
                    .append(");")
                    .toString();
        }

        private static CtMethod copy(CtMethod src, String body, CtClass declaring) throws CannotCompileException, NotFoundException {
            return CtNewMethod.make(src.getModifiers(), src.getReturnType(), src.getName(), src.getParameterTypes(), src.getExceptionTypes(), body, declaring);
        }
}
