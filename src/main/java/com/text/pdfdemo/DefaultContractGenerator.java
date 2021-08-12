package com.text.pdfdemo;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DefaultContractGenerator extends ContractGenerator {
    /**
     * @param sourceHtmlPath html模板文件路径
     * @param fontPath 字体文件路径
     * @param workingDir     指定操作工作文件夹
     * @param contractId     合同id，为后续占位符替换校验预留
     * @param params         占位符对应数据
     */
    public DefaultContractGenerator(String sourceHtmlPath, String workingDir, Integer contractId, Map<String, String> params) {
        super(sourceHtmlPath, workingDir, contractId, params);
    }

    @Override
    protected String getHashingDirName(String dirName) {
        HashFunction hf = Hashing.md5();
        HashCode hash = hf.newHasher()
                .putString(dirName, StandardCharsets.UTF_8)
                .hash();
        String workingDirName = hash.toString();
        return workingDirName;
    }

    @Override
    protected String getParamContent(String key) {
        String parKey = key.replace("{{", "").replace("}}", "");
        String fillStr;
        int underlineIndex = parKey.indexOf("_");
        if (underlineIndex < 0) {
            throw new IllegalStateException("找不到合法的占位符");
        }
        String prefixType = parKey.substring(0, underlineIndex);
        if ("b".equals(prefixType)) {
            fillStr = "1".equals(this.getParams().getOrDefault(parKey, "0")) ? "x" : "□";
        } else {
            fillStr = this.getParams().getOrDefault(parKey, "");
        }
        return fillStr;
    }

    @Override
    protected void convertToPdf(InputStream fis, OutputStream os) {
        PDFUtil.writeToOutputStreamAsPDF(fis, os);
    }

    @Override
    protected void checkPlaceholderParams() {
        // TODO: 校验参数
//        throw new RuntimeException("参数丢失");
    }
}
