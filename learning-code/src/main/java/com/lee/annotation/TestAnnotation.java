package com.lee.annotation;


import com.lee.util.log.Logging;
import com.lee.util.parseFormula.ParseException;
import com.lee.util.parseFormula.Parser;

import java.lang.reflect.Field;

public class TestAnnotation {

    public static void main(String args[]) {

//        String info = getInfo(Person.class);
//        System.out.println(info);

        //test formula parse and clac
        String formula = "(20+x)*y";
        double eval = new Parser().parse(formula).eval(s -> {
            if (s.equals("x")) {
                Logging.info("into change x");
                return 0;
            } else if (s.equals("y")){
                Logging.info("into change y");
                return 3;
            }
            Logging.error(new ParseException(103).getMessage());
            throw new ParseException(103);
        });
        System.out.println(eval);

    }

    private static String getInfo(Class<?> clazz) {

        String result = "";

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(Name.class)) {

                Name annotation = field.getAnnotation(Name.class);
                result += field.getName() + " is :" + annotation.value() + "，";

            } else if (field.isAnnotationPresent(Sex.class)) {

                Sex annotation = field.getAnnotation(Sex.class);
                result += field.getName() + " is :" + annotation.gender().toString();
            }


        }

        return result;
    }

}
