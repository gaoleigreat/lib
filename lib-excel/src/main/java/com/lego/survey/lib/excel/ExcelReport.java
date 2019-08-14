package com.lego.survey.lib.excel;

import com.lego.survey.lib.excel.element.EObject;
import com.lego.survey.lib.excel.element.EPic;
import com.lego.survey.lib.excel.model.Coordinate;
import com.lego.survey.lib.excel.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

@Slf4j
public class ExcelReport {
    private String templatePath;
    private XSSFWorkbook xsf = null;
    private OutputStream os = null;
    private InputStream is = null;

    /**
     * 构造方法
     *
     * @param templatePath
     */
    public ExcelReport(String templatePath) {
        this.templatePath = templatePath;
        init();
    }

    /**
     * 初始化XSSFWorkbook
     *
     * @throws IOException
     */
    private void init() {
        try {
            is = ExcelUtil.getFileStream(this.templatePath);
            xsf = new XSSFWorkbook(is);
        } catch (Exception e) {
            log.error("excel 表格初始化失败", e.getMessage());
        }
    }

    /**
     * 获取Excel中所有可以替换的变量的值和坐标
     *
     * @return List<Coordinate>
     */
    public List<Coordinate> getReplaceParam() {
        List<Coordinate> coordinates = new ArrayList<>();
        int totalSheetNumber = xsf.getNumberOfSheets();
        for (int sheetNumber = 0; sheetNumber < totalSheetNumber; sheetNumber++) {
            XSSFSheet sheet = xsf.getSheetAt(sheetNumber);
            int totalRowNumber = sheet.getPhysicalNumberOfRows();
            for (int rowNumber = 0; rowNumber < totalRowNumber; rowNumber++) {
                XSSFRow row = sheet.getRow(rowNumber);
                int totalLineNumber = row.getPhysicalNumberOfCells();
                for (int lineNumber = 0; lineNumber < totalLineNumber; lineNumber++) {
                    XSSFCell cell = row.getCell(lineNumber);
                    try {
                        String cellValue = cell.getStringCellValue();
                        Matcher matcher = ExcelUtil.matcher(cellValue);
                        while (matcher.find()) {
                            coordinates.add(new Coordinate(sheetNumber, rowNumber, lineNumber, matcher.group(1)));
                        }
                    } catch (Exception e) {
                        log.error("get cell value faile:{}", e.getMessage());
                    }


                }

            }

        }
        return coordinates;
    }


    /**
     * 对Excel中的值进行替换
     */

    public void replaceParame(EObject eObject) {
        List<Coordinate> coordinates = getReplaceParam();
        for (Coordinate coordinate : coordinates) {
            Object value = eObject.getValByKey(coordinate.getValue().toString());
           /* if (value instanceof Integer || value instanceof Double) {
                xsf.getSheetAt(coordinate.getSheetNumber()).getRow(coordinate.getRowNumber()).getCell(coordinate.getLineNumber()).setCellValue(Double.valueOf(value.toString()));
            } else if (value instanceof EPic) {
                try {
                    xsf.getSheetAt(coordinate.getSheetNumber()).getRow(coordinate.getRowNumber()).getCell(coordinate.getLineNumber()).setCellValue("");
                    insertPicture(coordinate.getSheetNumber(), coordinate.getRowNumber(), coordinate.getLineNumber(), value);
                } catch (Exception e) {
                    log.error("插入图片失败：{}", e.getMessage());
                }
            } else {
                xsf.getSheetAt(coordinate.getSheetNumber()).getRow(coordinate.getRowNumber()).getCell(coordinate.getLineNumber()).setCellValue(value.toString());
            }*/
           setCellValue(coordinate.getSheetNumber(),coordinate.getRowNumber(),coordinate.getLineNumber(),eObject.getValByKey(coordinate.getValue().toString()),null);
        }
    }

    /**
     * 动态插入
     *
     * @param sheetNumber 第几个sheet
     * @param rowNumber   第几行
     * @param eObjects    插入的对象 list
     * @param hasBorder   是否带有border
     */

    public void insertIntoTable(int sheetNumber, int rowNumber, List<EObject> eObjects, boolean hasBorder) {

        List<Coordinate> coordinates = new ArrayList<>();
        XSSFRow row = xsf.getSheetAt(sheetNumber).getRow(rowNumber);
        XSSFRow formatRow = xsf.getSheetAt(sheetNumber).getRow(rowNumber-1);
        int totalLineNumber = row.getPhysicalNumberOfCells();
        xsf.getSheetAt(sheetNumber).shiftRows(rowNumber + 1, xsf.getSheetAt(sheetNumber).getLastRowNum(), eObjects.size() - 1, true, false);
        CellStyle style = xsf.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        for (int i = 0; i < eObjects.size() - 1; i++) {
            xsf.getSheetAt(sheetNumber).createRow(rowNumber + i + 1);

            for (int j = 0; j < totalLineNumber; j++) {

                xsf.getSheetAt(sheetNumber).getRow(rowNumber + i + 1).createCell(j);
                if (hasBorder) {
                    xsf.getSheetAt(sheetNumber).getRow(rowNumber + i + 1).getCell(j).setCellStyle(style);
                }

            }
        }


        for (int lineNumber = 0; lineNumber < totalLineNumber; lineNumber++) {
            XSSFCell cell = row.getCell(lineNumber);
            String cellValue = cell.getStringCellValue();
            Matcher matcher = ExcelUtil.matcher(cellValue);
            while (matcher.find()) {
                coordinates.add(new Coordinate(sheetNumber, rowNumber, lineNumber, matcher.group(1)));
            }
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).createCell(lineNumber);
            if (hasBorder) {
                xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellStyle(style);
            }
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).getCellStyle().setDataFormat( formatRow.getCell(lineNumber).getCellStyle().getDataFormat());
        }

        for (int i = 0; i < eObjects.size(); i++) {

            for (Coordinate coordinate : coordinates) {
                setCellValue(coordinate.getSheetNumber(), coordinate.getRowNumber() + i, coordinate.getLineNumber(), eObjects.get(i).getValByKey(coordinate.getValue().toString()),formatRow.getCell(coordinate.getLineNumber()).getCellStyle().getDataFormat());
            }
        }

    }

    /**
     * 输出路径
     *
     * @param outDocPath
     * @return
     * @throws IOException
     */
    public boolean generate(String outDocPath) throws IOException {
        os = new FileOutputStream(outDocPath);
        xsf.write(os);
        ExcelUtil.close(os);
        ExcelUtil.close(is);
        return true;
    }


    /**
     * @param sheetNumber 第几个sheet
     * @param rowNumber   第几行
     * @param lineNumber  第几列
     * @param value       插入值
     */

    private void setCellValue(int sheetNumber, int rowNumber, int lineNumber, Object value,Short format) {
        if (null == value) {
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellValue("");
        } else if (value instanceof Double) {
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellValue(Double.valueOf(value.toString()));
            if (null !=format){
                xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).getCellStyle().setDataFormat(format);
            }
        }else if(value instanceof Integer) {
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellValue(Integer.valueOf(value.toString()));
        } else if (value instanceof EPic) {
            try {
                xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellValue("");
                insertPicture(sheetNumber, rowNumber, lineNumber, value);
            } catch (Exception e) {
                log.error("插入图片失败：{}", e.getMessage());
            }
        } else if (value instanceof Date) {
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellValue((Date) value);
        } else {
            xsf.getSheetAt(sheetNumber).getRow(rowNumber).getCell(lineNumber).setCellValue(value.toString());
        }

    }

    /**
     * @param sheetNumber 第几个sheet
     * @param rowNumber   第几行
     * @param lineNumber  第几列
     * @param value       插入值
     */

    private void insertPicture(int sheetNumber, int rowNumber, int lineNumber, Object value) {
        EPic ePic;
        if (value instanceof EPic) {
            ePic = (EPic) value;
        } else {
            return;
        }

        XSSFDrawing xssfDrawing = xsf.getSheetAt(sheetNumber).createDrawingPatriarch();
        try {
            xsf.addPicture(ExcelUtil.getFileStream(ePic.getUrl()), ExcelUtil.getPictureType(ePic.getType()));

            ClientAnchor anchor = new XSSFClientAnchor(10000, 10000, 0, 0,
                    lineNumber, rowNumber, lineNumber + ePic.getWidth(), rowNumber + ePic.getHeight());
            xssfDrawing.createPicture(anchor, xsf.getAllPictures().size() - 1);
        } catch (Exception e) {
            log.error("获取图片上失败：{}", e.getMessage());
        }
    }


}
