package com.lee.reflectAjavassist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class Reflect {
    //JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，
    // 都能够调用它的任意方法和属性；这种动态获取信息以及动态调用对象方法的功能称为java语言的反射机制。

    //是程序可以访问、检测和修改它本身状态或行为的一种能力。java反射使得我们可以在程序运行时动态加载一个类，
    // 动态获取类的基本信息和定义的方法,构造函数,域等。除了检阅类信息外，还可以动态创建类的实例，执行类实例的方法，获取类实例的域值。
    private static  final  Logger logger=Logger.getLogger(Reflect.class.getName());
    private final static String TAG = "reflect.Reflect";

    public  static  void reflectNewInstance(){
        try{
            Class<?> bookClass=Class.forName("com.lee.reflectAjavassist.Book");
            Book book=(Book) bookClass.newInstance();
            book.setName("戴望舒");
            book.setId(7);
            logger.info(TAG+",reflectNewInstance book ="+book.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reflectPriConstructor(){
        try {
            Class<?> bookClass=Class.forName("com.lee.reflectAjavassist.Book");
            Constructor<?> constructorBook=bookClass.getDeclaredConstructor(String.class,int.class);
            constructorBook.setAccessible(true);
            Book book=(Book) constructorBook.newInstance("葡萄干",1);
            logger.info(TAG+",reflectPriConstructor book= "+book.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void reflectPriField(){
        try {
            Class<?> bookClass = Class.forName("com.lee.reflectAjavassist.Book");
            Object bookObj= bookClass.newInstance();
            Field field=bookClass.getDeclaredField("TAG");
            field.setAccessible(true);
            String fieldStr= (String) field.get(bookObj);
            logger.info(TAG+",reflectPriField bookField="+fieldStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reflectPriMethod(){
        try {
            Class<?> bookClass = Class.forName("com.lee.reflectAjavassist.Book");
            Method method=bookClass.getDeclaredMethod("declaredMethod", int.class);
            method.setAccessible(true);
            Object bookObj=bookClass.newInstance();
            String string= (String) method.invoke(bookObj,0);
            logger.info(TAG+",reflectPrivateMethod string = " + string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Reflect.reflectNewInstance();
        Reflect.reflectPriConstructor();
        Reflect.reflectPriField();
        Reflect.reflectPriMethod();
    }


}
