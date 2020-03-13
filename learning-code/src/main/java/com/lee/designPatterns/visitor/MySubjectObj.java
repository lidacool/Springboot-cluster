package com.lee.designPatterns.visitor;

public class MySubjectObj implements SubjectObj {
    @Override
    public void accept(Visitor visitor) {
        visitor.visitor(this);
    }

    @Override
    public String getSubject() {
        return "subject";
    }
}
