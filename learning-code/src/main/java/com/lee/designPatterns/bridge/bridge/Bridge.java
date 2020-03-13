package com.lee.designPatterns.bridge.bridge;

import com.lee.designPatterns.bridge.DataLibrary;

public abstract  class Bridge {

    private DataLibrary dataLibrary;

    public void connectData(){
        dataLibrary.connectData();
    }

    public DataLibrary getDataLibrary() {
        return dataLibrary;
    }

    public void setDataLibrary(DataLibrary dataLibrary) {
        this.dataLibrary = dataLibrary;
    }
}
