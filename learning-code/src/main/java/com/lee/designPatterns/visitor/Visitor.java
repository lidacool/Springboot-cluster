package com.lee.designPatterns.visitor;

public interface Visitor {

    //访问者模式适用于数据结构相对稳定的系统，把数据结构和算法解耦，
    void visitor(SubjectObj subject);

}
