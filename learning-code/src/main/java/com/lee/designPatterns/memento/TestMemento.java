package com.lee.designPatterns.memento;

public class TestMemento {

    public static void main(String[] args) {
        Original original=new Original("original");
        Storage storage=new Storage(original.createMemento());

        // 修改原始类的状态
        System.out.println("初始化状态为：" + original.getValue());
        original.setValue("new resources");
        System.out.println("修改后的状态为：" + original.getValue());

        // 回复原始类的状态
        original.resetMemento(storage.getMemento());
        System.out.println("恢复后的状态为：" + original.getValue());
    }
}
