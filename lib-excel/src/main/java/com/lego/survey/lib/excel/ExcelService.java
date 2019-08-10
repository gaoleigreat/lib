package com.lego.survey.lib.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.lego.survey.lib.excel.listener.ExcelReadListener;
import com.lego.survey.lib.excel.utils.DataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    public  void writeExcel(List<? extends BaseRowModel> baseRowModels,
                            String excelFileName,
                            String sheetName,
                            List<List<String>> headers) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(excelStorePath + excelFileName + ".xlsx");
            ExcelWriter excelWriter = new ExcelWriter(out, ExcelTypeEnum.XLSX,true);
            Sheet sheet = new Sheet(1, 1);
            sheet.setAutoWidth(Boolean.TRUE);
            sheet.setTableStyle(DataUtil.createTableStyle());
            sheet.setClazz(baseRowModels.get(0).getClass());
            sheet.setSheetName(sheetName);
            if(!CollectionUtils.isEmpty(headers)){
                sheet.setHead(headers);
            }
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
                           ExcelReadListener eventListener,
                           Class<? extends  BaseRowModel> baseRowModel,
                           int sheetNo) {
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(excelFileName));
            ExcelReader excelReader = new ExcelReader(stream, ExcelTypeEnum.XLSX, null, eventListener);
            excelReader.read(new Sheet(sheetNo, 1, baseRowModel));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }






    /**
     * read   excel to  any
     * @param excelFileName
     * @param eventListener
     */
    public  void  readExcel(String excelFileName, ExcelReadListener eventListener){
        BufferedInputStream stream;
        try {
            stream=new BufferedInputStream(new FileInputStream(excelFileName));
            ExcelReader excelReader = new ExcelReader(stream, ExcelTypeEnum.XLSX, null, eventListener);
            excelReader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }



}
