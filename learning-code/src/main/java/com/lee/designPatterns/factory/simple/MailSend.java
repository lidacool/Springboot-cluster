package com.lee.designPatterns.factory.simple;


public class MailSend implements Sender {

    @Override
    public void sender() {
        System.out.println("this is a mailSend class");
    }
}
