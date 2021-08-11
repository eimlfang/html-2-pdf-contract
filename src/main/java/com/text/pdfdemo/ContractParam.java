package com.text.pdfdemo;

public class ContractParam {
    private String key;
    private String desc;

    private ContractParam(String k, String d) {
        this.key = k;
        this.desc = d;
    }

    public static ContractParam of(String k, String d) {
        return new ContractParam(k, d);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
