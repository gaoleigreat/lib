package com.framework.word.util;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.CollectionUtils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther xiaodao
 * @date 2019/9/5 11:30
 */
public class WordTemplateUtil {
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
            int headerSize = getHeaderSize(keys);
            for (int i = 0; i < headerSize; i++) {
                rows.remove(0);
            }

            rows.forEach(r -> {
                Map<String, Object> rowValueMap = convvertRowValueToMap(r, keys);
                resultList.add(rowValueMap);
            });
        } else {
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
        List<String> key = new ArrayList<>(keys);

        Map<String, Object> resultMap = new HashMap<>();
        if (row == null || CollectionUtils.isEmpty(keys) || row.getTableCells().size() != keys.size()) {
            return resultMap;
        }
        List<XWPFTableCell> tableCells = row.getTableCells();
        tableCells.forEach(t -> {
            resultMap.put(key.remove(0), t.getText());
        });
        return resultMap;
    }


    public static void main(String[] args) throws IOException, FileNotFoundException {
        new FileInputStream("C:/Users/xiaodao/Desktop/word.docx");
        XWPFDocument doc = new XWPFDocument(new FileInputStream("C:/Users/xiaodao/Desktop/word.docx"));
        WordTemplateUtil wordUtilT = new WordTemplateUtil();
        List<String> ke = Arrays.asList();
        String[] as = {"基本信息.姓名", "基本信息.年龄", "学历信息.学校", "学历信息.专业", "住址"};



        List<String> transferedList = new ArrayList<>();
        Arrays.stream(as).forEach(arr -> transferedList.add(arr));
        List<Map<String, Object>> excelData = wordUtilT.getExcelData(doc, null, null);
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

    public int getHeaderSize(List<String> keys) {
        AtomicInteger result = new AtomicInteger();

        keys.forEach(k -> {
            result.set(getBiggerNumber(getSubCount(k, "."), result.get()));
        });
        return result.get();

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


}
