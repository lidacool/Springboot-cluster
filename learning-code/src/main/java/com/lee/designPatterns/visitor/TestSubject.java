package com.lee.designPatterns.visitor;

public class TestSubject {

    public static void main(String[] args) {
        Visitor visitor=new VisitorImpl();
        SubjectObj subject=new MySubjectObj();
        subject.accept(visitor);
    }
}
