package com.text.pdfdemo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Contract1 extends Contract {

    public final String contractNo = "合同编号";
    public final String firstParty = "甲方";
    public final String firstPartyAddress = "甲方地址";
    public final String firstPartyLegalRepresentative = "甲方法定代表人";
    public final String firstPartyIdNum = "甲方身份证号";
    public final String firstPartyCreditCode = "甲方社会信用统一代码或工商登记注册号";
    public final String firstPartyPhone = "甲方电话号码";
    public final String additionalProvisions = "附加条款";

    public Contract1(String fileName) {
        super(fileName);
    }

    @Override
    public Map<String, String> getParametersMap() {
        Class<Contract1> aClass = Contract1.class;
        Field[] fields = aClass.getDeclaredFields();
        Map<String, String> parMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                parMap.put(fieldName, (String) field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return parMap;
    }
}
