package com.framework.word.util;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @auther xiaodao
 * @date 2019/9/5 11:30
 */
public class WordUtilT {
    /**
     * 获取word表格里面的数据
     *
     * @param doc
     * @param tableIndex
     */
    public List<Map<String, Object>> getExcelData(XWPFDocument doc, Integer tableIndex, List<String> keys) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (null == doc) {
            return resultList;
        }
        if (null == tableIndex || tableIndex < 0) {
            List<XWPFTable> tables = doc.getTables();
            tables.forEach(t -> {
                resultList.addAll(getTableDatas(t, keys));
            });
        } else {
            resultList.addAll(getTableDatas(doc.getTableArray(tableIndex), keys));
        }
        return resultList;
    }

    /**
     * 获取表格里面的数据
     *
     * @param table
     */
    public List<Map<String, Object>> getTableDatas(XWPFTable table, List<String> keys) {
        List<XWPFTableRow> rows = table.getRows();
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (table == null) {
            return resultList;
        }
        XWPFTableRow header = rows.remove(0);

        List<String> headerList = convvertRowValueToList(header);
        if (keys != null) {
            rows.forEach(r -> {
                Map<String, Object> rowValueMap = convvertRowValueToMap(r, convertKeys(keys));
                resultList.add(rowValueMap);
            });
        }else {
            rows.forEach(r -> {
                Map<String, Object> rowValueMap = convvertRowValueToMap(r, headerList);
                resultList.add(rowValueMap);
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
    public List<String> convvertRowValueToList(XWPFTableRow row) {
        List<String> list = new ArrayList<>();
        List<XWPFTableCell> tableCells = row.getTableCells();
        tableCells.forEach(t -> {
            list.add(t.getText());
        });
        return list;
    }

    /**
     * 将行数据转化为map
     * 行的列数和key的size要相等
     *
     * @param row
     * @param keys
     * @return
     */
    public Map<String, Object> convvertRowValueToMap(XWPFTableRow row, List<String> keys) {
        Map<String, Object> resultMap = new HashMap<>();
        if (row == null || CollectionUtils.isEmpty(keys) || row.getTableCells().size() != keys.size()) {
            return resultMap;
        }
        List<XWPFTableCell> tableCells = row.getTableCells();
        tableCells.forEach(t -> {
            resultMap.put(keys.remove(0), t.getText());
        });
        return resultMap;
    }


    public static void main(String[] args) throws IOException {
        new FileInputStream("C:/Users/xiaodao/Desktop/word.docx");
        XWPFDocument doc = new XWPFDocument(new FileInputStream("C:/Users/xiaodao/Desktop/word.docx"));
        WordUtilT wordUtilT = new WordUtilT();
        List<String> ke = Arrays.asList("姓名","年龄","address","work","生日");
        List<Map<String, Object>> excelData = wordUtilT.getExcelData(doc, null, ke);
        System.out.println(excelData);
    }

    /**
     * 将带点的转化为点后面的值
     *
     * @param keys
     * @return
     */
    public List<String> convertKeys(List<String> keys) {
        List<String> resultKeys = new ArrayList<>();
        keys.forEach(k -> {
            if (k.contains(".")) {
                k = k.substring(k.lastIndexOf(".") + 1);
            }
            resultKeys.add(k);
        });
        return resultKeys;
    }

}
