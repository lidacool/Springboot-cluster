package com.lee.designPatterns.state;

public class TestState {

    public static void main(String[] args) {
        State state=new State(true);
        StateOperate context=new StateOperate(state);
        context.method();
        context.updateState(false);
        context.method();
    }
}
