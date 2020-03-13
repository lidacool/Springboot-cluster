package com.lee.theGeneric.genericClass;


import com.sun.org.apache.xpath.internal.operations.String;

import java.util.ArrayList;
import java.util.List;

public class Genericity<T> {

    private T t;

    public Genericity(T t) {
        this.t=t;
    }

    public Genericity() {//

    }

    public T getT() {
        return t;
    }

    //泛型类中的泛型方法
    private void show_t1(T t){System.out.println(t.toString());}

    private <E> void  show_t2(E e){System.out.println(e.toString());}

    private <T> void show_t3(T t){System.out.println(t.toString());}

    static class Fruit{
        @Override
        public java.lang.String toString() {
            return "fruit";
        }
    }

    public static void main(String[] args) {
        //泛型擦除机制
//        List<String>[] lsa = new List<String>[10]; // Not really allowed.
//        Object o = lsa;
//        Object[] oa = (Object[]) o;
//        List<Integer> li = new ArrayList<Integer>();
//        li.add(new Integer(3));
//        oa[1] = li; // Unsound, but passes run time store check
//        String s = lsa[1].get(0); // Run-time error: ClassCastException.


        List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.
        Object o = lsa;
        Object[] oa = (Object[]) o;
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Correct.
        Integer i = (Integer) lsa[1].get(0); // OK
    }


}
