package com.lee.designPatterns.memento;

public class Original {

    private String value;

    public Original(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Memento createMemento(){
        return  new Memento(value);
    }

    public void  resetMemento(Memento memento){
        this.value=memento.getValue();
    }
}
