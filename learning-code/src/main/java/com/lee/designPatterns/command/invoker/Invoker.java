package com.lee.designPatterns.command.invoker;

import com.lee.designPatterns.command.Command;

public class Invoker {

    private Command command;

    public Invoker(Command command) {
        this.command = command;
    }

    public void command(){
        command.exe();
    }
}
