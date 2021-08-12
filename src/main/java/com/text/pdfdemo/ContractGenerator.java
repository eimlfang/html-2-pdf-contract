package com.text.pdfdemo;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContractGenerator {

    private static final Pattern BRACE_PATTERN = Pattern.compile("\\{\\{.*?}}");
    private static final String FONT = "D:/temp/assets/webfont.ttf";
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
    public ContractGenerator(String sourceHtmlPath, String workingDir, Integer contractId, Map<String, String> params) {
        this.sourceHtmlPath = sourceHtmlPath;
        this.workingDir = workingDir;
        this.params = params;
        this.contractId = contractId;
    }

    public void generate() throws IOException {
        checkParams();
        prepareWorkingDir();
        fillingTemplate();
        convertHtmlToPdf();
    }

    /**
     * 创建工作区
     */
    private void prepareWorkingDir() {
        Random random = new Random(10);
        String format = String.format("%d%d", System.currentTimeMillis(), random.nextInt());
        HashFunction hf = Hashing.md5();
        HashCode hash = hf.newHasher()
                .putString(format, StandardCharsets.UTF_8)
                .hash();
        String workingDirName = hash.toString();
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

    private String getParamContent(String group) {
        String parKey = group.replace("{{", "").replace("}}", "");
        String fillStr;
        int underlineIndex = parKey.indexOf("_");
        if (underlineIndex < 0) {
            throw new IllegalStateException("找不到合法的占位符");
        }
        String prefixType = parKey.substring(0, underlineIndex);
        if ("b".equals(prefixType)) {
            fillStr = "1".equals(params.getOrDefault(parKey, "0")) ? "x" : "□";
        } else {
            fillStr = params.getOrDefault(parKey, "");
        }
        return fillStr;
    }

    private void convertHtmlToPdf() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(new File(workingDir, workingDirName+"/"+ FILLED_HTML_NAME));
        FileOutputStream os = new FileOutputStream(new File(workingDir, workingDirName+"/"+ FINAL_PDF_NAME));
        PDFUtil.writeToOutputStreamAsPDF(fis, os);
    }

    private void checkParams() {
        // TODO: 校验参数
//        throw new RuntimeException("参数丢失");
    }
}
