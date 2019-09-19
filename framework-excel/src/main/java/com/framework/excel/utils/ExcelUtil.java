package com.framework.excel.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil {

    /*正则匹配${}*/
    public static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }


    /**
     * 正则匹配字符串是否含有
     *
     * @param str
     * @return
     */
    public static boolean isMatcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 获取文件输入流，输入可以使链接，也可以是路径
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static InputStream getFileStream(String filePath) throws Exception {
        if (StringUtils.isNotBlank(filePath) && filePath.startsWith("http")) {
            URL url = new URL(filePath);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream is = conn.getInputStream();
            return is;
        } else if (StringUtils.isNotBlank(filePath)) {
            InputStream is = new FileInputStream(filePath);
            return is;
        } else {
            return null;
        }
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    public static int getPictureType(String picType) {
        int res = Workbook.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = Workbook.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = Workbook.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = Workbook.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = Workbook.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = Workbook.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }


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


    /**
     * @param data
     * @param sheetName
     * @param excelName
     * @param type      0-xlsx 1-xls
     * @param response
     * @throws IOException
     */
    public static void excelWriter(List<List<String>> data,
                                   String sheetName,
                                   String excelName,
                                   Integer type,
                                   HttpServletResponse response) throws Exception {
        response.setContentType("application/force-download");
        response.setCharacterEncoding("utf-8");
        Workbook workbook;
        if (type == 0) {
            response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(excelName + ".xlsx", "UTF-8"));
            workbook = getXlsxExcel(data, sheetName);
        } else if (type == 1) {
            response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(excelName + ".xls", "UTF-8"));
            workbook = getXlsExcel(data, sheetName);
        } else {
            throw new Exception("excel类型错误");
        }
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
    }

    private static Workbook getXlsExcel(List<List<String>> data, String sheetName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        if (!CollectionUtils.isEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < data.get(i).size(); j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(data.get(i).get(j));
                }
            }
        }
        return workbook;
    }


    private static Workbook getXlsxExcel(List<List<String>> data, String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        if (!CollectionUtils.isEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j < data.get(i).size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(data.get(i).get(j));
                }
            }
        }
        return workbook;
    }


}
