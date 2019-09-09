package com.framework.excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther xiaodao
 * @date 2019/9/6 10:45
 */
public class ExcelTemplateUtil {
    /**
     * 读取excel里面数据
     */
    public List<Map<String, Object>> readExcelData(Workbook wb, Integer sheetNumber, List<String> keys) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        if (wb == null) {
            return resultMapList;
        }
        //若sheetnumber 为空或者小于0 则读取全部sheet,否则读取对应的sheet
        if (null == sheetNumber || sheetNumber < 0) {
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                resultMapList.addAll(getSheetValue(wb.getSheetAt(i), keys));
            }
        } else {
            Sheet sheet = wb.getSheetAt(sheetNumber);
            resultMapList = getSheetValue(sheet, keys);
        }
        return resultMapList;
    }


    /**
     * 获取字符串中含有多少个其他字符串
     *
     * @param str
     * @param key
     * @return
     */
    public int getSubCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();

            count++;
        }
        return count;
    }

    public int getHeaderSize(List<String> keys) {
        AtomicInteger result = new AtomicInteger();

        keys.forEach(k -> {
            result.set(getBiggerNumber(getSubCount(k, "."), result.get()));
        });
        return result.get();

    }

    /**
     * 获取两个数字中的大数
     *
     * @param a
     * @param b
     * @return
     */
    public int getBiggerNumber(int a, int b) {
        if (a >= b) {
            return a;
        } else {
            return b;
        }

    }

    public List<Map<String, Object>> getSheetValue(Sheet sheet, List<String> keys) {

        List<Map<String, Object>> resultList = new ArrayList<>();
        if (sheet == null) {
            return resultList;
        }
        Iterator<Row> rowIterator = sheet.iterator();
        Row header = rowIterator.next();
        List<String> headerList = convvertRowValueToList(header);
        if (keys != null) {
            int headerSize = getHeaderSize(keys);
            for (int i=0;i<headerSize;i++){
                rowIterator.next();
            }
            rowIterator.forEachRemaining(row -> {
                resultList.add(convvertRowValueToMap(row, keys));

            });
        } else {
            rowIterator.forEachRemaining(row -> {
                resultList.add(convvertRowValueToMap(row, headerList));
            });
        }
        return resultList;
    }


    /**
     * 将行数据转化为list
     *
     * @param row
     * @return
     */
    public List<String> convvertRowValueToList(Row row) {
        List<String> list = new ArrayList<>();
        Iterator<Cell> cellIterator = row.iterator();
        cellIterator.forEachRemaining(cell -> {
            list.add(cell.toString());
        });
        return list;
    }


    /**
     * 將行轉化為map
     *
     * @param row
     * @param keys
     * @return
     */
    public Map<String, Object> convvertRowValueToMap(Row row, List<String> keys) {
        List<String> key = new ArrayList<>(keys);

        Map<String, Object> resultMap = new HashMap<>();
        if (row == null || CollectionUtils.isEmpty(keys) || row.getPhysicalNumberOfCells() != keys.size()) {
            return resultMap;
        }
        Iterator<Cell> cellIterator = row.cellIterator();
        cellIterator.forEachRemaining(cell -> {
            resultMap.put(key.remove(0), cell.toString());
        });
        return resultMap;
    }

    public static void main(String[] args) throws IOException {
        ExcelTemplateUtil excelTemplateUtil = new ExcelTemplateUtil();
        XSSFWorkbook xsf = new XSSFWorkbook(new FileInputStream("C:/Users/xiaodao/Desktop/excel.xlsx"));
        String[] as = {"基本信息.姓名", "基本信息.年龄", "学历信息.学校", "学历信息.专业", "住址"};


        List<String> transferedList = new ArrayList<>();
        Arrays.stream(as).forEach(arr -> transferedList.add(arr));
        List<Map<String, Object>> resultMapList = excelTemplateUtil.readExcelData(xsf, 0, null);
        System.out.println(resultMapList);
    }
}
