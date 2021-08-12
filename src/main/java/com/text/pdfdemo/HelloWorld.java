package com.text.pdfdemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HelloWorld {
    public static final String DEST = "D:/temp/assets/test.pdf";
    public static final String HTML = "D:/temp/assets/simple.html";
    public static final String HTML2 = "D:\\temp\\pdf-demo\\src\\main\\resources\\html\\doc-2.html";
    private static final String FONT = "D:/temp/assets/webfont.ttf";

    private static final Pattern REG_PATTERN = Pattern.compile("\\{\\{.*?}}");

    static String paramDemo() {
        return "{\n" +
                "\"t_contractNo\": \"abx123xyz\",\n" +
                "\"t_firstParty\": \"小方\",\n" +
                "\"t_firstPartyAddress\": \"福建省厦门市湖里区\",\n" +
                "\"t_firstPartyLegalRepresentative\": \"\",\n" +
                "\"t_firstPartyIdNum\": \"35260811112222\",\n" +
                "\"t_firstPartyCreditCode\": \"\",\n" +
                "\"p_firstPartyPhone\": \"15605025957\",\n" +
                "\"m_sellingPrice\": \"120000\",\n" +
                "\"p_sellingPriceCn\": \"拾贰万元整\",\n" +
                "\"m_transferCharges\": \"20000\",\n" +
                "\"p_transferChargesCn\": \"贰万元整\",\n" +
                "\"b_isFullPay\": \"0\",\n" +
                "\"b_isSpecialPay\": \"1\",\n" +
                "\"p_subscriptionMoney\": \"2000\",\n" +
                "\"p_subscriptionMoneyCn\": \"贰仟元整\",\n" +
                "\"p_balancePayMoney\": \"100\",\n" +
                "\"p_balancePayMoneyCn\": \"壹佰元整\",\n" +
                "\"b_isOtherPaymentType\": \"0\",\n" +
                "\"p_additionalProvisions\": \"全车无泡水， 全车原版原漆这次影响最直接的，就是低洼之地的各种车，惨遭泡水大法。不过，并不是所有泡水的车都算是泡水车，也要分情况来看。第一种是浸水车，水深仅浸湿车轮，这种情况下其实伤害不是很大，如今汽车装配工艺相较十几年前来说，密封性水平相当高了，所以这种程度的泡水车影响不大。只需把车开到高地晾干即可，但如果你实在不放心，也可以去修理厂检查一遍底盘线路。\",\n" +
                "\"b_isSecondPartyPayServiceCharge\": \"1\",\n" +
                "\"p_secondPartySignYear\": \"2021\",\n" +
                "\"p_secondPartySignMonth\": \"8\",\n" +
                "  \"p_secondPartySignDay\": \"12\"\n" +
                "}";
    }

    public static void main(String[] args) throws DocumentException, IOException {
        Map pars = JSON.parseObject(paramDemo(), Map.class);
        try (ContractGenerator generator
                     = new DefaultContractGenerator(HTML2, "D:/temp/assets", 1, pars)) {
            File finalOutputFile = generator.generate();
            System.out.println("最终输出文件：" + finalOutputFile.getAbsolutePath());
        }
    }


}
