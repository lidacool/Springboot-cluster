package com.lee.designPatterns.chainOfResponsibility;

public abstract class AbstractHandle {

    private Handle handle;

    public Handle getHandle() {
        return handle;
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
    }
}
