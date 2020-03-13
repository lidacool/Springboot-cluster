package com.lee.reflectAjavassist;

import com.lee.util.log.Logging;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Ssist {

    public static void main(String args[]){
            ClassPool pool=ClassPool.getDefault();
            try {
                CtClass ct=pool.getCtClass("reflectAjavassist.Point");
                CtMethod m=ct.getDeclaredMethod("move");
                m.insertBefore("{ System.out.print(\"dx:\"+$1); System.out.println(\"dy:\"+$2);}");
                m.insertAfter("{System.out.println(this.x); System.out.println(this.y);}");

                ct.writeFile();
                //通过反射调用方法，查看结果
                Class pc=ct.toClass();
                Method move= pc.getMethod("move",new Class[]{int.class,int.class});
                Constructor<?> con=pc.getConstructor(new Class[]{int.class,int.class});
                move.invoke(con.newInstance(1,2),1,2);
            } catch (Exception e) {
                Logging.error("javassist or reflect use exception !");
            }
    }

}
