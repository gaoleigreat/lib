package com.framework.word;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.List;
/**
 * @author yanglf
 * @description
 * @since 2019/1/17
 **/
@Component
public class WordService {

    /**
     * write   word
     *
     * @param reportName report name
     */
    public void writeWord(String reportName, String content) {
        FileOutputStream stream;
        XWPFDocument xwpfDocument;
        try {
            xwpfDocument = new XWPFDocument();
            XWPFParagraph paragraph = xwpfDocument.createParagraph();
            XWPFRun xwpfRun = paragraph.createRun();
            xwpfRun.setText(content);
            stream = new FileOutputStream(new File(reportName));
            xwpfDocument.write(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取  word  信息
     *
     * @param templateName
     * @return
     */
    public String readWordContent(String templateName) {
        try {
            XWPFDocument xwpfDocument = new XWPFDocument(new FileInputStream(templateName));
            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
            StringBuffer stringBuffer = new StringBuffer();
            paragraphs.forEach(paragraph -> {
                List<XWPFRun> runs = paragraph.getRuns();
                runs.forEach(r -> {
                    String s = r.toString();
                    stringBuffer.append(s);
                });
            });
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
