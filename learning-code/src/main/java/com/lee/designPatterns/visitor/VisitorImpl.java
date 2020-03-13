package com.lee.designPatterns.visitor;

public class VisitorImpl implements Visitor {

    @Override
    public void visitor(SubjectObj subject) {
        System.out.println("visitor the subjec:"+subject.getSubject());
    }

}
