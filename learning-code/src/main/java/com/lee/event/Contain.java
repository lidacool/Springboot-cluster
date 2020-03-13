package com.lee.event;

public interface Contain {

     void regist(Subscriber subscriber);

     void unregist(int group);

     void unregist(Subscriber subscriber);
}
