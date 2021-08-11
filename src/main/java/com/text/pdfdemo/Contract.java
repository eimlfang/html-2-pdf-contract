package com.text.pdfdemo;

import java.util.Map;

public abstract class Contract {
    public Contract(String fileName) {
        this.fileName = fileName;
    }

    private String fileName;
    public String getFileName() {
        return fileName;
    };
    abstract Map<String, String> getParametersMap();
}
