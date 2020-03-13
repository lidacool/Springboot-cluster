package com.lee.theGeneric.genericClass;

public class TestGeneric {

    Genericity generic1=new Genericity("");
    Genericity generic2=new Genericity(1);
    Genericity<Object> objectGeneric=new Genericity<Object>();


    public void showKeyVlaues(Genericity<?> obj){//？:所有类型的父类。是一种真实的类型
        System.out.println(obj.getT());
    }

    public void test(){
        showKeyVlaues(new Genericity<Integer>(1));
        showKeyVlaues(new Genericity<Number>(1));
        showKeyVlaues(new Genericity<String>(""));
    }

    private <T> T genericMethod(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        return tClass.newInstance();
    }

    private void getInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        genericMethod(Class.forName("com.lee.theGeneric.genericClass.Genericity"));
    }


}
