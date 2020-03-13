package com.lee.designPatterns.mediator.decoupling;

import com.lee.designPatterns.mediator.Mediator;

public class User1 extends User0 {

    public User1(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void work() {
        System.out.println("user1 begin exe");
    }
}
