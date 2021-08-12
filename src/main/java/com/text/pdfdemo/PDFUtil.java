package com.text.pdfdemo;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PDFUtil {
    public static void writeStringToOutputStreamAsPDF(String html, OutputStream os) {
        writeToOutputStreamAsPDF(new ByteArrayInputStream(html.getBytes()), os);
    }

    public static void writeToOutputStreamAsPDF(InputStream html, OutputStream os) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, html, Charset.forName("UTF-8"), new AsianFontProvider());
            document.close();
        } catch (Exception e) {
            throw new IllegalStateException("转换PDF失败：" + e.getMessage());
        }
    }
}
/**
 * 用于中文显示的Provider
 */
class AsianFontProvider extends XMLWorkerFontProvider {
    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        try {
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return new Font(bfChinese, size, style);
        } catch (Exception e) {
        }
        return super.getFont(fontname, encoding, size, style);
    }
}