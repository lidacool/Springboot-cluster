package com.lee.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lida
 * @description field annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sex {

     enum SexType{

         male("男"),
         female("女"),
         Not_man_and_female("什么嘛")
         ;

         private String value;

         SexType(String value) {
             this.value = value;
         }

         @Override
         public String toString() {
             return this.value;
         }
     }

     SexType gender() default SexType.Not_man_and_female;
}
