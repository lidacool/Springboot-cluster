package com.lee.designPatterns.protoType;

import java.io.*;
import java.util.List;

public class ProtoType implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String testString;
    private int testInt;
    private List<Integer> testList;
    private boolean testBool;

    public ProtoType() {
    }

    public ProtoType(String testString, int testInt, List<Integer> testList, boolean testBool) {
        this.testString = testString;
        this.testInt = testInt;
        this.testList = testList;
        this.testBool = testBool;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Object deepClone() throws IOException, ClassNotFoundException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public String getTestString() {
        return testString;
    }

    public int getTestInt() {
        return testInt;
    }

    public List<Integer> getTestList() {
        return testList;
    }

    public boolean isTestBool() {
        return testBool;
    }

    public void  updateProtoType() {
        this.testString = "updateName";
        this.testInt = 1;
        this.testBool = false;
        this.testList.add(6);
    }

    @Override
    public String toString() {
        return "ProtoType{" +
                "testString='" + testString + '\'' +
                ", testInt=" + testInt +
                ", testList=" + testList +
                ", testBool=" + testBool +
                '}';
    }
}
