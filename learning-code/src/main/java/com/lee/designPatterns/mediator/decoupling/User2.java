package com.lee.designPatterns.mediator.decoupling;

import com.lee.designPatterns.mediator.Mediator;

public class User2 extends User0 {

    public User2(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void work() {
        System.out.println("user2 begin exe");
    }
}
