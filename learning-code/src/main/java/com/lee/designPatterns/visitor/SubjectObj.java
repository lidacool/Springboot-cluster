package com.lee.designPatterns.visitor;

public interface SubjectObj {

    void accept(Visitor visitor);

    String getSubject();
}
