package com.lee.designPatterns.state;

public class StateOperate {

    private State state;

    public StateOperate(State state) {
        this.state = state;
    }

    public void updateState(boolean state) {
        this.state.setOpen(state);
    }

    public void method(){
        if (state.isOpen()){
            state.open();
        }else {
            state.close();
        }
    }
}
