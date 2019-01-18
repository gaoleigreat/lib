package com.lego.survey.lib.word;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTPImpl;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.List;
import java.util.Map;
/**
 * @author yanglf
 * @description
 * @since 2019/1/17
 **/
@Component
public class WordService {

    public static void main(String []args){
      writeWord("D:\\JavaWeb\\report\\test.docs");
    }

    /**
     * write   word
     * @param reportName  report name
     */
    public static void writeWord(String reportName) {
        FileOutputStream stream;
        XWPFDocument xwpfDocument;
        try {
            xwpfDocument=new XWPFDocument();
            stream = new FileOutputStream(new File(reportName));
            xwpfDocument.write(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取  excel  信息
     *
     * @param templateName
     * @param contentMap
     * @return
     */
    public static XWPFDocument readWordContent(String templateName, Map<String, String> contentMap) {
        try {
            XWPFDocument xwpfDocument = new XWPFDocument(POIXMLDocument.openPackage(templateName));
            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
            paragraphs.forEach(paragraph -> {
                    List<XWPFRun> runs = paragraph.getRuns();
                    runs.forEach(r -> {
                        String s = r.toString();
                        String replace = null;
                        System.out.println(String.format("读取内容:%s",s));
                        for (Map.Entry<String,String> param : contentMap.entrySet()){
                            if(s.contains(param.getKey())){
                                System.out.println(String.format("替换:%s为%s",param.getKey(),param.getValue()));
                                replace = s.replace("{"+param.getKey()+"}", param.getValue());
                                System.out.println(String.format("替换后的值:%s",replace));
                            }
                        }
                        r.setText(replace,r.getTextPosition());
                    });
            });
            return xwpfDocument;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
