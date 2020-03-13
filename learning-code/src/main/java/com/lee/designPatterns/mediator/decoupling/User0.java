package com.lee.designPatterns.mediator.decoupling;

import com.lee.designPatterns.mediator.Mediator;

/**
 * 有些类名在mybatis 映射实体时会重复mapper
 * 所以类名尽量具体详细点 保证少重复
 * */
public abstract class User0 {

    private Mediator mediator;

    public User0(Mediator mediator) {
        this.mediator = mediator;
    }

    public Mediator getMediator() {
        return mediator;
    }

    public abstract void work();
}
