package com.lee.designPatterns.command.commandImpl;

import com.lee.designPatterns.command.Command;
import com.lee.designPatterns.command.receiver.Receiver;


public class MyCommand implements Command {

    private Receiver receiver;

    public MyCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exe() {
        receiver.action();
    }
}
