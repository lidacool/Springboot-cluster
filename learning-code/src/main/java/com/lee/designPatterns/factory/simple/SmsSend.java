package com.lee.designPatterns.factory.simple;

public class SmsSend implements Sender {
    @Override
    public void sender() {
        System.out.println("this is a SMS sender class");
    }
}
