package com.framework.excel.test;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author yanglf
 * @description
 * @since 2019/8/17
 **/
@Component
public class ExcelReader {


    public List<Map<Integer, Object>> reader(String filePath) throws Exception {
        List<Map<Integer, Object>> list = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        String type = filePath.lastIndexOf(".") > 0 ? filePath.substring(filePath.lastIndexOf(".") + 1) : "";
        Workbook workbook;
        if ("xlsx".equals(type)) {
            workbook = new XSSFWorkbook(fis);
        } else {
            workbook = new HSSFWorkbook(fis);
        }
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            Iterator<Cell> cellIterator = row.cellIterator();
            Map<Integer, Object> map = new HashMap<>();
            while (cellIterator.hasNext()) {
                Cell next = cellIterator.next();
                int columnIndex = next.getColumnIndex();
                map.put(columnIndex, next.toString());
            }
            list.add(map);
        }
        return list;
    }
}
