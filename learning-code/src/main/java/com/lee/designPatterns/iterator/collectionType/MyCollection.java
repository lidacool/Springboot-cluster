package com.lee.designPatterns.iterator.collectionType;

import com.lee.designPatterns.iterator.Collection;
import com.lee.designPatterns.iterator.Iterator;

import java.util.ArrayList;
import java.util.List;

public class MyCollection implements Collection {

    public List<Object> objects =new ArrayList<Object>();

    @Override
    public Iterator iterator() {
        return new MyIterator(this);
    }

    @Override
    public Object get(int index) {
        return objects.get(index);
    }

    @Override
    public int size() {
        return objects.size();
    }

    @Override
    public void add(Object object) {
        objects.add(object);
    }
}
