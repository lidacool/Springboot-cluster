package com.lee.reflectAjavassist;

public class Book {

    private static final String TAG = "我是私有静态终态属性！";

    private String name;
    private int id;

    public Book() {
    }

    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }

    private String declaredMethod(int index) {
        String string = null;
        switch (index) {
            case 0:
                string = "i am first priMethod";
                break;
            case 1:
                string = "i am second priMethod";
                break;
            default:
                string = "i am not defind priMethod";
                break;

        }
        return string;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
