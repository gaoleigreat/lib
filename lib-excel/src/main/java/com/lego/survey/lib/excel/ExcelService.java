package com.lego.survey.lib.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.lego.survey.lib.excel.listener.ExcelListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Component
public class ExcelService {

    @Value("${define.report.excel.storePath:/app/survey/report/}")
    private String excelStorePath;


    /**
     * write  excel
     * @param baseRowModels  pojo
     * @param excelFileName  excel name
     */
    public  void writeExcel(List<? extends BaseRowModel> baseRowModels, String excelFileName,String sheetName) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(excelStorePath + excelFileName + ".xlsx");
            ExcelWriter excelWriter = new ExcelWriter(out, ExcelTypeEnum.XLSX,true);
            Sheet sheet = new Sheet(1, 1);
            sheet.setClazz(baseRowModels.get(0).getClass());
            sheet.setSheetName(sheetName);
           // sheet.setHead(getSheetHead());
            excelWriter.write(baseRowModels, sheet);
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<List<String>> getSheetHead() {
        List<List<String>> headList=new ArrayList<>();
        List<String> heads1=new ArrayList<>();
        headList.add(heads1);
        return headList;
    }





    /**
     * read excel to pojo
     * @param excelFileName  excel name
     * @param eventListener  read listener
     * @param baseRowModel  pojo
     */
    public  void readExcel(String excelFileName,
                           ExcelListener eventListener,
                           BaseRowModel baseRowModel,
                           int sheetNo) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(excelFileName);
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, eventListener);
            excelReader.read(new Sheet(sheetNo, 1, baseRowModel.getClass()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
