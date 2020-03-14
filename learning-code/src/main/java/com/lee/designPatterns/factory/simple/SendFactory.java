package com.lee.designPatterns.factory.simple;

/**
 * 简单工厂模式
 */
public class SendFactory {

    //common factory pattern
    public Sender produce(String factoryName) {
        switch (factoryName) {
            case "mail":
                return new MailSend();
            case "sms":
                return new SmsSend();
            default:
                throw new IllegalArgumentException();
        }
    }

    //multi factory pattern,flowing two method
    public Sender produceMail() {
        return new MailSend();
    }

    public Sender produceSms() {
        return new SmsSend();
    }

    //static factory pattern,flowing two static method
    public static Sender produceMailStatic() {
        return new MailSend();
    }

    public static Sender produceSmsStatic() {
        return new SmsSend();
    }

}
