package com.framework.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.framework.excel.listener.ExcelReadListener;
import com.framework.excel.utils.DataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Component
public class ExcelService {


    /**
     * write  excel
     *
     * @param headers
     * @param excelName excel name
     */
    public void writeExcel(List<Object> headers,
                           String excelName,
                           String sheetName,
                           HttpServletResponse response) {
        OutputStream out = null;
        try {
            if (response != null) {
                response.setContentType("application/force-download");
                response.setCharacterEncoding("utf-8");
                response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(excelName, "UTF-8"));
                out = response.getOutputStream();
            } else {
                out = new FileOutputStream(excelName + ".xlsx");
            }
            ExcelWriter excelWriter = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet = new Sheet(1, 1);
            sheet.setSheetName(sheetName);
            excelWriter.write1(DataUtil.createListObject(headers), sheet);
            excelWriter.finish();
            out.flush();
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


    /**
     * write  excel
     *
     * @param baseRowModels pojo
     * @param excelFileName excel name
     */
    public void writeExcel(List<? extends BaseRowModel> baseRowModels,
                           String excelFileName,
                           String sheetName,
                           List<List<String>> headers,
                           HttpServletResponse response) {
        OutputStream out = null;
        try {
            if (response != null) {
                response.setContentType("application/force-download");
                response.setCharacterEncoding("utf-8");
                response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(excelFileName, "UTF-8"));
                out = response.getOutputStream();
            } else {
                out = new FileOutputStream(excelFileName + ".xlsx");
            }
            ExcelWriter excelWriter = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
           /* ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(inputStream,out,ExcelTypeEnum.XLSX,true,
                    new AfterWriteHandlerImpl());*/
            Sheet sheet = new Sheet(1, 1);
            //sheet.setAutoWidth(Boolean.TRUE);
            //sheet.setTableStyle(DataUtil.createTableStyle());
            sheet.setClazz(baseRowModels.get(0).getClass());
            sheet.setSheetName(sheetName);
            if (!CollectionUtils.isEmpty(headers)) {
                sheet.setHead(headers);
            }
            excelWriter.write(baseRowModels, sheet);
            excelWriter.finish();
            out.flush();
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
        List<List<String>> headList = new ArrayList<>();
        List<String> heads1 = new ArrayList<>();
        headList.add(heads1);
        return headList;
    }


    /**
     * read excel to pojo
     *
     * @param excelFileName excel name
     * @param eventListener read listener
     * @param baseRowModel  pojo
     */
    public void readExcel(String excelFileName,
                          ExcelReadListener eventListener,
                          Class<? extends BaseRowModel> baseRowModel,
                          int sheetNo) throws Exception {
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(excelFileName)));
        ExcelReader excelReader;
        if (excelFileName.endsWith(ExcelTypeEnum.XLSX.getValue())) {
            excelReader = new ExcelReader(stream, ExcelTypeEnum.XLSX, null, eventListener);
        } else if (excelFileName.endsWith(ExcelTypeEnum.XLS.getValue())) {
            excelReader = new ExcelReader(stream, ExcelTypeEnum.XLS, null, eventListener);
        } else {
            throw new Exception();
        }
        excelReader.read(new Sheet(sheetNo, 1, baseRowModel));
    }


    /**
     * read   excel to  any
     *
     * @param excelFileName
     * @param eventListener
     */
    public void readExcel(String excelFileName, ExcelReadListener eventListener) throws Exception {
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(excelFileName)));
        ExcelReader excelReader;
        if (excelFileName.endsWith(ExcelTypeEnum.XLSX.getValue())) {
            excelReader = new ExcelReader(stream, ExcelTypeEnum.XLSX, null, eventListener);
        } else if (excelFileName.endsWith(ExcelTypeEnum.XLS.getValue())) {
            excelReader = new ExcelReader(stream, ExcelTypeEnum.XLS, null, eventListener);
        } else {
            throw new Exception();
        }
        excelReader.read();
    }


}
