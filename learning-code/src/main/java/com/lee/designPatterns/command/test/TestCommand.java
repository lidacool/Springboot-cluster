package com.lee.designPatterns.command.test;

import com.lee.designPatterns.command.commandImpl.MyCommand;
import com.lee.designPatterns.command.invoker.Invoker;
import com.lee.designPatterns.command.receiver.Receiver;

public class TestCommand {


    public static void main(String[] args) {
    Receiver receiver=new Receiver();

    MyCommand command=new MyCommand(receiver);
    Invoker invoker=new Invoker(command);
    invoker.command();
    }
}
