package com.text.pdfdemo;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloWorld {
    public static final String DEST = "D:/temp/assets/test.pdf";
    public static final String HTML = "D:/temp/assets/simple.html";
    public static final String HTML2 = "D:\\temp\\pdf-demo\\src\\main\\resources\\html\\doc-2.html";
    private static final String FONT = "D:/temp/assets/webfont.ttf";

    private static final Pattern REG_PATTERN = Pattern.compile("\\{\\{.*?}}");

    public static void main(String[] args) throws DocumentException, IOException {
        Map<String, String> pars = new HashMap<>();
        pars.put("contractNo", "abc123xyz");
        pars.put("firstParty", "小方");
        pars.put("firstPartyAddress", "福建省厦门市湖里区创业北路9号");
        pars.put("firstPartyLegalRepresentative", "没有人");
        pars.put("additionalProvisions", "全车无泡水， 全车原版原漆这次影响最直接的，就是低洼之地的各种车，惨遭泡水大法。不过，并不是所有泡水的车都算是泡水车，也要分情况来看。\n" +
                "\n" +
                "第一种是浸水车，水深仅浸湿车轮，这种情况下其实伤害不是很大，如今汽车装配工艺相较十几年前来说，密封性水平相当高了，所以这种程度的泡水车影响不大。只需把车开到高地晾干即可，但如果你实在不放心，也可以去修理厂检查一遍底盘线路。");
        pars.put("isFullPay", "1");
        pars.put("isSecondPartyPayServiceCharge", "0");
        pars.put("isOtherPaymentType", "1");
        // √
        // □
        // ☑
        ContractGenerator generator
                = new ContractGenerator(HTML2, "D:/temp/assets/target.html", 1, pars);
        generator.generate();
    }


}
