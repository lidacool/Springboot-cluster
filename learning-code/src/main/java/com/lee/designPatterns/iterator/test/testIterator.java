package com.lee.designPatterns.iterator.test;

import com.lee.designPatterns.iterator.Collection;
import com.lee.designPatterns.iterator.Iterator;
import com.lee.designPatterns.iterator.collectionType.MyCollection;

public class testIterator {

    public static void main(String[] args) {
        Collection collection=new MyCollection();
        String string="haha";
        collection.add(string);
        Integer integer=123;
        collection.add(integer);
        byte bytee='b';
        collection.add(bytee);
        byte[] bytes={'a','/','b'};
        collection.add(bytes);
        Iterator iterator=collection.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
