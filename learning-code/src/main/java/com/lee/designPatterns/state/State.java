package com.lee.designPatterns.state;

public class State {

    private boolean isOpen;

    public State(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close(){
        System.out.println("state is close");
    }

    public void open(){
        System.out.println("state is open");
    }
}
