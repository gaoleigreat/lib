package com.framework.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Component
public class PdfService {


    @Value("define.report.pdf.storePath")
    private String pdfStorePath;


    /**
     * generate pdf
     *
     * @param str
     */
    public void generatePdf(String str, String pdfFileName) {
        OutputStream os;
        try {
            os = new FileOutputStream(pdfStorePath + pdfFileName + ".pdf");
            Document document = new Document();
            PdfWriter.getInstance(document, os);
            document.open();
            document.add(new Paragraph(str));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
