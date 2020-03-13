package com.lee.designPatterns.protoType.testPrototype;

import com.lee.designPatterns.protoType.ProtoType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestPrototype {

    public static void main(String[] args) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        List<Integer> testInteger= new ArrayList<>(Arrays.asList(1,2,3,4,5));
        ProtoType protoType=new ProtoType("李凌卓",1007,testInteger,true);
        ProtoType commonClone= (ProtoType) protoType.clone();
        System.out.println("before update common clone,prototype");
        System.out.println(protoType.toString());
        System.out.println("before update common clone,commonClone");
        System.out.println(commonClone.toString());
        commonClone.updateProtoType();
        System.out.println("after update common clone,prototype");
        System.out.println(protoType.toString());
        System.out.println("after update common clone,commonClone");
        System.out.println(commonClone.toString());

        deepCloneTest();
    }

    private static void deepCloneTest() throws IOException, ClassNotFoundException {
        List<Integer> testInteger= new ArrayList<>(Arrays.asList(1,2,3,4,5));
        ProtoType protoType=new ProtoType("李凌卓",1007,testInteger,true);
        ProtoType commonClone= (ProtoType) protoType.deepClone();
        System.out.println("before update deep clone,prototype");
        System.out.println(protoType.toString());
        System.out.println("before update deep clone,commonClone");
        System.out.println(commonClone.toString());
        commonClone.updateProtoType();
        System.out.println("after update deep clone,prototype");
        System.out.println(protoType.toString());
        System.out.println("after update deep clone,commonClone");
        System.out.println(commonClone.toString());
    }
}
