package com.text.pdfdemo;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContractGenerator {

    private static final Pattern BRACE_PATTERN = Pattern.compile("\\{\\{.*?}}");
    private static final String FONT = "D:/temp/assets/webfont.ttf";

    private final String sourcePath;
    private final String outputPath;
    private final Map<String, String> params;
    private final Integer contractIdx;

    public ContractGenerator(String sourcePath, String outputPath, Integer contractIdx, Map<String, String> params) {
        this.sourcePath = sourcePath;
        this.outputPath = outputPath;
        this.params = params;
        this.contractIdx = contractIdx;
    }

    public void generate() throws IOException {
        checkParams();
        generateTargetHtml();
        FileInputStream fis = new FileInputStream(outputPath);
        FileOutputStream os = new FileOutputStream("D:/temp/assets/test.pdf");
        PDFUtil.writeToOutputStreamAsPDF(fis, os);
    }

    private void generateTargetHtml() throws IOException {
        File file = new File(sourcePath);
        try(BufferedWriter out = new BufferedWriter(new FileWriter(outputPath))) {
            try(Scanner reader = new Scanner(file)) {
                while (reader.hasNext()) {
                    String line = reader.nextLine();

                    Matcher matcher = BRACE_PATTERN.matcher(line);
                    while (matcher.find()) {
                        String group = matcher.group();
                        String parKey = group.replace("{{", "").replace("}}", "");
                        String fillStr;
                        if (parKey.startsWith("is")) {
                            fillStr = "1".equals(params.getOrDefault(parKey, "0")) ? "✔" : "□";
                        } else {
                            fillStr = params.getOrDefault(parKey, "");
                        }
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

    public void createPDF(OutputStream out, String html) throws IOException, DocumentException, com.lowagie.text.DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        // 解决中文支持问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(out);
    }

    private void checkParams() {
//        throw new RuntimeException("参数丢失");
    }
}
