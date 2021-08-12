package com.text.pdfdemo;

import java.io.*;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ContractGenerator implements Closeable{

    private static final Pattern BRACE_PATTERN = Pattern.compile("\\{\\{.*?}}");
    private static final String FILLED_HTML_NAME = "filled.html";
    private static final String FINAL_PDF_NAME = "final.pdf";

    private final String sourceHtmlPath;
    private final String workingDir;
    private final Map<String, String> params;
    private final Integer contractId;
    private String workingDirName;

    /**
     *
     * @param sourceHtmlPath html模板文件路径
     * @param workingDir 指定操作工作文件夹
     * @param contractId 合同id，为后续占位符替换校验预留
     * @param params 占位符对应数据
     */
    protected ContractGenerator(String sourceHtmlPath, String workingDir, Integer contractId, Map<String, String> params) {
        this.sourceHtmlPath = sourceHtmlPath;
        this.workingDir = workingDir;
        this.params = params;
        this.contractId = contractId;
    }

    protected Map<String, String> getParams() {
        return params;
    }

    public File generate() throws IOException {
        checkPlaceholderParams();
        prepareWorkingDir();
        fillingTemplate();
        return convertHtmlToPdf();
    }

    /**
     * 创建工作区
     */
    private void prepareWorkingDir() {
        Random random = new Random(10);
        String format = String.format("%d%d", System.currentTimeMillis(), random.nextInt());
        String workingDirName = getHashingDirName(format);
        File workingDir = new File(this.workingDir, workingDirName);
        System.out.println("工作区路径：" + workingDir.getAbsolutePath());

        if (!workingDir.exists()) {
            boolean mkdirs = workingDir.mkdir();
            System.out.println("创建工作区结果：" + mkdirs);
            if (mkdirs) {
                this.workingDirName = workingDirName;
            } else {
                throw new IllegalStateException("创建工作区失败");
            }
        }
    }

    /**
     * 生成工作目录
     * @param dirName
     * @return
     */
    protected abstract String getHashingDirName(String dirName);

    /**
     * 填充模板
     * @throws IOException
     */
    private void fillingTemplate() throws IOException {
        File file = new File(sourceHtmlPath);

        try(BufferedWriter out = new BufferedWriter(new FileWriter(new File(workingDir, workingDirName+"/"+ FILLED_HTML_NAME)))) {
            try(Scanner reader = new Scanner(file)) {
                while (reader.hasNext()) {
                    String line = reader.nextLine();

                    Matcher matcher = BRACE_PATTERN.matcher(line);
                    while (matcher.find()) {
                        String group = matcher.group();
                        String fillStr = getParamContent(group);
                        line = line.replace(group, fillStr);
                        matcher = BRACE_PATTERN.matcher(line);
                    }
                    out.write(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract String getParamContent(String key);

    private File convertHtmlToPdf() throws FileNotFoundException {
        InputStream fis = new FileInputStream(new File(workingDir, workingDirName+"/"+ FILLED_HTML_NAME));
        File outputFile = new File(workingDir, workingDirName + "/" + FINAL_PDF_NAME);
        OutputStream os = new FileOutputStream(outputFile);
        convertToPdf(fis, os);
        return outputFile;
    }

    /**
     * 转换html为PDF
     * @param fis
     * @param os
     */
    protected abstract void convertToPdf(InputStream fis, OutputStream os);

    /**
     * 校验替换参数
     */
    protected abstract void checkPlaceholderParams();

    private void cleanUp() {
        File file = new File(workingDir, workingDirName);
        if (file.exists()) {
            file.delete();
        }
    }
    @Override
    public void close() throws IOException {
        System.out.println("清空工作文件");
        cleanUp();
    }
}
