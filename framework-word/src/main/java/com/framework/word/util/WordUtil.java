package com.framework.word.util;


import com.framework.common.element.OObject;
import com.framework.common.element.OPic;
import com.framework.common.utils.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther xiaodao
 * @date 2019/9/2 10:34
 */
public class WordUtil {


    /**
     * 查找段落里面的变量
     *
     * @param para 要替换的段落
     * @throws Exception
     * @throws IOException
     * @throws InvalidFormatException
     */
    private static Set<String> findInPara(XWPFParagraph para) throws Exception {
        Set<String> resultSet = new HashSet<>();
        List<XWPFRun> runs;
        //有符合条件的占位符
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            String runText = new String();
            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                runText = runText + run.toString();
            }
            Matcher matcher = matcher(runText);
            while (matcher.find()) {
                int i = 1;
                resultSet.add(matcher.group(i));
                i++;
            }

        }

        return resultSet;
    }


    /**
     * 查找段落里面的变量
     *
     * @throws Exception
     */
    public static Set<String> findInPara(XWPFDocument doc) throws Exception {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        Set<String> resultSet = new HashSet<>();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            resultSet.addAll(findInPara(para));
        }
        return resultSet;
    }


    /**
     * 查找表格里面的变量
     *
     * @param
     * @throws Exception
     */
    public static Set<String> findInTable(XWPFTable table) throws Exception {

        Set<String> resultSet = new HashSet<>();
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        rows = table.getRows();
        for (XWPFTableRow row : rows) {
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                paras = cell.getParagraphs();
                for (XWPFParagraph para : paras) {
                    resultSet.addAll(findInPara(para));
                }
            }
        }
        return resultSet;
    }

    /**
     * 查找表格里面的变量
     *
     * @param tableIndex 第几找表，-1为全部
     * @throws Exception
     */
    public static Set<String> findInTable(XWPFDocument doc, Integer tableIndex) throws Exception {
        List<XWPFTable> tableList = doc.getTables();
        if (tableList.size() <= tableIndex) {
            throw new Exception("tableIndex对应的表格不存在");
        }
        Set<String> resultSet = new HashSet<>();
        if (null == tableIndex || tableIndex == -1) {
            for (int i = 0; i < tableList.size(); i++) {
                resultSet.addAll(findInTable(tableList.get(i)));
            }
        } else {
            resultSet.addAll(findInTable(tableList.get(tableIndex)));
        }
        return resultSet;
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    public static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    public static boolean matcherRow(String str) {
        Pattern pattern = Pattern.compile("\\$\\[(.+?)\\]",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }


    /**
     * 设置单元格数据
     * @param tmpCell
     * @param cell
     * @param text
     * @throws Exception
     */
    public static void setCellText(XWPFTableCell tmpCell, XWPFTableCell cell,
                                   String text) throws Exception {

        CTTc cttc2 = tmpCell.getCTTc();
        CTTcPr ctPr2 = cttc2.getTcPr();
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();

        if (ctPr2.getTcW() != null) {
            ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
        }
        if (ctPr2.getVAlign() != null) {
            ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
        }
        if (cttc2.getPList().size() > 0) {
            CTP ctp = cttc2.getPList().get(0);
            if (ctp.getPPr() != null) {
                if (ctp.getPPr().getJc() != null) {
                    cttc.getPList().get(0).addNewPPr().addNewJc()
                            .setVal(ctp.getPPr().getJc().getVal());
                }
            }
        }

        if (ctPr2.getTcBorders() != null) {
            ctPr.setTcBorders(ctPr2.getTcBorders());
        }

        XWPFParagraph tmpP = tmpCell.getParagraphs().get(0);
        XWPFParagraph cellP = cell.getParagraphs().get(0);
        XWPFRun tmpR = null;
        if (tmpP.getRuns() != null && tmpP.getRuns().size() > 0) {
            tmpR = tmpP.getRuns().get(0);
        }

        List<XWPFRun> runList = new ArrayList<XWPFRun>();
        if (text == null) {
            XWPFRun cellR = cellP.createRun();
            runList.add(cellR);
            cellR.setText("");
        } else {
            //这里的处理思路是：$b认为是段落的分隔符，分隔后第一个段落认为是要加粗的
            if (text.indexOf("\b") > -1) {//段落，加粗，主要用于产品行程
                String[] bArr = text.split("\b");
                for (int b = 0; b < bArr.length; b++) {
                    XWPFRun cellR = cellP.createRun();
                    runList.add(cellR);
                    if (b == 0) {//默认第一个段落加粗
                        cellR.setBold(true);
                    }
                    if (bArr[b].indexOf("\n") > -1) {
                        String[] arr = bArr[b].split("\n");
                        for (int i = 0; i < arr.length; i++) {
                            if (i > 0) {
                                cellR.addBreak();
                            }
                            cellR.setText(arr[i]);
                        }
                    } else {
                        cellR.setText(bArr[b]);
                    }
                }
            } else {
                XWPFRun cellR = cellP.createRun();
                runList.add(cellR);
                if (text.indexOf("\n") > -1) {
                    String[] arr = text.split("\n");
                    for (int i = 0; i < arr.length; i++) {
                        if (i > 0) {
                            cellR.addBreak();
                        }
                        cellR.setText(arr[i]);
                    }
                } else {
                    cellR.setText(text);
                }
            }

        }

        // 复制字体信息
        if (tmpR != null) {
            //cellR.setBold(tmpR.isBold());
            //cellR.setBold(true);
            for (XWPFRun cellR : runList) {
                if (!cellR.isBold()) {
                    cellR.setBold(tmpR.isBold());
                }
                cellR.setItalic(tmpR.isItalic());
                cellR.setStrike(tmpR.isStrike());
                cellR.setUnderline(tmpR.getUnderline());
                cellR.setColor(tmpR.getColor());
                cellR.setTextPosition(tmpR.getTextPosition());
                if (tmpR.getFontSize() != -1) {
                    cellR.setFontSize(tmpR.getFontSize());
                }
                if (tmpR.getFontFamily() != null) {
                    cellR.setFontFamily(tmpR.getFontFamily());
                }
                if (tmpR.getCTR() != null) {
                    if (tmpR.getCTR().isSetRPr()) {
                        CTRPr tmpRPr = tmpR.getCTR().getRPr();
                        if (tmpRPr.isSetRFonts()) {
                            CTFonts tmpFonts = tmpRPr.getRFonts();
                            CTRPr cellRPr = cellR.getCTR().isSetRPr() ? cellR
                                    .getCTR().getRPr() : cellR.getCTR().addNewRPr();
                            CTFonts cellFonts = cellRPr.isSetRFonts() ? cellRPr
                                    .getRFonts() : cellRPr.addNewRFonts();
                            cellFonts.setAscii(tmpFonts.getAscii());
                            cellFonts.setAsciiTheme(tmpFonts.getAsciiTheme());
                            cellFonts.setCs(tmpFonts.getCs());
                            cellFonts.setCstheme(tmpFonts.getCstheme());
                            cellFonts.setEastAsia(tmpFonts.getEastAsia());
                            cellFonts.setEastAsiaTheme(tmpFonts.getEastAsiaTheme());
                            cellFonts.setHAnsi(tmpFonts.getHAnsi());
                            cellFonts.setHAnsiTheme(tmpFonts.getHAnsiTheme());
                        }
                    }
                }
            }
        }
        // 复制段落信息
        cellP.setAlignment(tmpP.getAlignment());
        cellP.setVerticalAlignment(tmpP.getVerticalAlignment());
        cellP.setBorderBetween(tmpP.getBorderBetween());
        cellP.setBorderBottom(tmpP.getBorderBottom());
        cellP.setBorderLeft(tmpP.getBorderLeft());
        cellP.setBorderRight(tmpP.getBorderRight());
        cellP.setBorderTop(tmpP.getBorderTop());
        cellP.setPageBreak(tmpP.isPageBreak());
        if (tmpP.getCTP() != null) {
            if (tmpP.getCTP().getPPr() != null) {
                CTPPr tmpPPr = tmpP.getCTP().getPPr();
                CTPPr cellPPr = cellP.getCTP().getPPr() != null ? cellP
                        .getCTP().getPPr() : cellP.getCTP().addNewPPr();
                // 复制段落间距信息
                CTSpacing tmpSpacing = tmpPPr.getSpacing();
                if (tmpSpacing != null) {
                    CTSpacing cellSpacing = cellPPr.getSpacing() != null ? cellPPr
                            .getSpacing() : cellPPr.addNewSpacing();
                    if (tmpSpacing.getAfter() != null) {
                        cellSpacing.setAfter(tmpSpacing.getAfter());
                    }
                    if (tmpSpacing.getAfterAutospacing() != null) {
                        cellSpacing.setAfterAutospacing(tmpSpacing
                                .getAfterAutospacing());
                    }
                    if (tmpSpacing.getAfterLines() != null) {
                        cellSpacing.setAfterLines(tmpSpacing.getAfterLines());
                    }
                    if (tmpSpacing.getBefore() != null) {
                        cellSpacing.setBefore(tmpSpacing.getBefore());
                    }
                    if (tmpSpacing.getBeforeAutospacing() != null) {
                        cellSpacing.setBeforeAutospacing(tmpSpacing
                                .getBeforeAutospacing());
                    }
                    if (tmpSpacing.getBeforeLines() != null) {
                        cellSpacing.setBeforeLines(tmpSpacing.getBeforeLines());
                    }
                    if (tmpSpacing.getLine() != null) {
                        cellSpacing.setLine(tmpSpacing.getLine());
                    }
                    if (tmpSpacing.getLineRule() != null) {
                        cellSpacing.setLineRule(tmpSpacing.getLineRule());
                    }
                }
                // 复制段落缩进信息
                CTInd tmpInd = tmpPPr.getInd();
                if (tmpInd != null) {
                    CTInd cellInd = cellPPr.getInd() != null ? cellPPr.getInd()
                            : cellPPr.addNewInd();
                    if (tmpInd.getFirstLine() != null) {
                        cellInd.setFirstLine(tmpInd.getFirstLine());
                    }
                    if (tmpInd.getFirstLineChars() != null) {
                        cellInd.setFirstLineChars(tmpInd.getFirstLineChars());
                    }
                    if (tmpInd.getHanging() != null) {
                        cellInd.setHanging(tmpInd.getHanging());
                    }
                    if (tmpInd.getHangingChars() != null) {
                        cellInd.setHangingChars(tmpInd.getHangingChars());
                    }
                    if (tmpInd.getLeft() != null) {
                        cellInd.setLeft(tmpInd.getLeft());
                    }
                    if (tmpInd.getLeftChars() != null) {
                        cellInd.setLeftChars(tmpInd.getLeftChars());
                    }
                    if (tmpInd.getRight() != null) {
                        cellInd.setRight(tmpInd.getRight());
                    }
                    if (tmpInd.getRightChars() != null) {
                        cellInd.setRightChars(tmpInd.getRightChars());
                    }
                }
            }
        }
    }

    public static int getPictureType(String picType) {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }


    /**
     * 填充run内容
     *
     * @param para
     * @param params
     * @param tempMap
     * @param
     * @param
     * @throws InvalidFormatException
     * @throws IOException
     * @throws Exception
     */
    public static void fillBlock(XWPFParagraph para, OObject params,
                                 Map<Integer, String> tempMap)
            throws InvalidFormatException, IOException, Exception {
        Matcher matcher;
        if (tempMap != null && tempMap.size() > 0) {
            String wholeText = "";
            List<Integer> tempIndexList = new ArrayList<Integer>();
            for (Map.Entry<Integer, String> entry : tempMap.entrySet()) {
                tempIndexList.add(entry.getKey());
                wholeText += entry.getValue();
            }
            if (wholeText.equals("")) {
                return;
            }
            matcher = matcher(wholeText);
            if (matcher.find()) {
                boolean isPic = false;
                int width = 0;
                int height = 0;
                int picType = 0;
                String path = null;
                String keyText = matcher.group().substring(2, matcher.group().length() - 1);
                Object value = params.getValByKey(keyText);
                String newRunText = "";
                if (value instanceof OPic) {
                    isPic = true;
                    OPic wPic = (OPic) value;
                    width = wPic.getWidth();
                    height = wPic.getHeight();
                    picType = getPictureType(wPic.getType());
                    path = wPic.getUrl();

                } else {//插入图片
                    newRunText = matcher.replaceFirst(String.valueOf(value));
                }

                //模板样式
                XWPFRun tempRun = null;
                // 直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
                // 所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
                for (Integer pos : tempIndexList) {
                    tempRun = para.getRuns().get(pos);
                    tempRun.setText("", 0);
                }
                if (isPic) {
                    //addPicture方法的最后两个参数必须用Units.toEMU转化一下
                    //para.insertNewRun(index).addPicture(getPicStream(path), picType, "测试",Units.toEMU(width), Units.toEMU(height));
                    tempRun.addPicture(FileUtil.getFileInputStream(path), picType, "测试", Units.toEMU(width), Units.toEMU(height));
                } else {
                    //样式继承
                    if (newRunText.indexOf("\n") > -1) {
                        String[] textArr = newRunText.split("\n");
                        if (textArr.length > 0) {
                            //设置字体信息
                            String fontFamily = tempRun.getFontFamily();
                            int fontSize = tempRun.getFontSize();
                            for (int i = 0; i < textArr.length; i++) {
                                if (i == 0) {
                                    tempRun.setText(textArr[0], 0);
                                } else {
                                    if (StringUtils.isNotEmpty(textArr[i])) {
                                        XWPFRun newRun = para.createRun();
                                        //设置新的run的字体信息
                                        newRun.setFontFamily(fontFamily);
                                        if (fontSize == -1) {
                                            newRun.setFontSize(10);
                                        } else {
                                            newRun.setFontSize(fontSize);
                                        }
                                        newRun.addBreak();
                                        newRun.setText(textArr[i], 0);
                                    }
                                }
                            }
                        }
                    } else {
                        tempRun.setText(newRunText, 0);
                    }
                }
            }
            tempMap.clear();
        }
    }


    /**
     * 替换段落里面的变量
     *
     * @param para    要替换的段落
     * @param wObject 参数
     * @throws Exception
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static boolean replaceInPara(XWPFParagraph para, OObject wObject) throws Exception {
        boolean data = false;
        List<XWPFRun> runs;
        //有符合条件的占位符
        if (WordUtil.matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            data = true;
            Map<Integer, String> tempMap = new HashMap<Integer, String>();
            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                //以"$"开头
                boolean begin = runText.indexOf("$") > -1;
                boolean end = runText.indexOf("}") > -1;
                if (begin && end) {
                    tempMap.put(i, runText);
                    WordUtil.fillBlock(para, wObject, tempMap);
                    continue;
                } else if (begin && !end) {
                    tempMap.put(i, runText);
                    continue;
                } else if (!begin && end) {
                    tempMap.put(i, runText);
                    WordUtil.fillBlock(para, wObject, tempMap);
                    continue;
                } else {
                    if (tempMap.size() > 0) {
                        tempMap.put(i, runText);
                        continue;
                    }
                    continue;
                }
            }
        } else if (WordUtil.matcherRow(para.getParagraphText())) {
            runs = para.getRuns();
            data = true;
        }
        return data;
    }


    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param wTable 参数
     * @throws Exception
     */
    public static void replaceInTable(XWPFDocument doc, OObject wTable, int tableIndex) throws Exception {
        List<XWPFTable> tableList = doc.getTables();
        if (tableList.size() <= tableIndex) {
            throw new Exception("tableIndex对应的表格不存在");
        }
        XWPFTable table = tableList.get(tableIndex);
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        rows = table.getRows();
        for (XWPFTableRow row : rows) {
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                paras = cell.getParagraphs();
                for (XWPFParagraph para : paras) {
                    replaceInPara(para, wTable);
                }
            }
        }
    }


    /**
     * 替换段落里面的变量
     *
     * @param doc  要替换的文档
     * @param wDoc 参数
     * @throws Exception
     */
    public static void replaceInPara(XWPFDocument doc, OObject wDoc) throws Exception {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInPara(para, wDoc);
        }
    }

    /**
     * 在表格中插入数据
     *
     * @param doc
     * @param wTables
     * @param tableIndex
     * @param towIndex
     * @throws Exception
     */
    public static void insertValueToTable(XWPFDocument doc, List<OObject> wTables, int tableIndex, int towIndex) throws Exception {
        List<XWPFTable> tableList = doc.getTables();
        if (tableList.size() <= tableIndex) {
            throw new Exception("tableIndex对应的表格不存在");
        }
        XWPFTable table = tableList.get(tableIndex);
        List<XWPFTableRow> rows = table.getRows();
        if (rows.size() < 2) {
            throw new Exception("tableIndex对应表格应该为2行");
        }
        //模板行
        XWPFTableRow tmpRow = rows.get(towIndex);
        List<XWPFTableCell> tmpCells = null;
        List<XWPFTableCell> cells = null;
        XWPFTableCell tmpCell = null;

        tmpCells = tmpRow.getTableCells();
        String cellText = null;
        String cellTextKey = null;

        Map<String, Object> totalMap = null;


        for (int i = 0, len = wTables.size(); i < len; i++) {
            OObject wTable = wTables.get(i);
            XWPFTableRow row = table.createRow();
            row.setHeight(tmpRow.getHeight());
            cells = row.getTableCells();
            // 插入的行会填充与表格第一行相同的列数
            int nullValueSize = 0;
            for (int k = 0, klen = cells.size(); k < klen; k++) {
                tmpCell = tmpCells.get(k);
                XWPFTableCell cell = cells.get(k);
                cellText = tmpCell.getText();
                if (StringUtils.isNotBlank(cellText)) {
                    //转换为mapkey对应的字段
                    cellTextKey = cellText.replace("$", "").replace("{", "").replace("}", "").trim();
                    Object o = wTable.getValByKey(cellTextKey);


                    if (o == null) {
                        nullValueSize = nullValueSize + 1;
                        if (nullValueSize == cells.size()) {
                            table.removeRow(table.getRows().size() - 1);
                        }
                        continue;
                    }
                    if (o instanceof OPic) {
                        //插入图片
                        OPic pic = (OPic) o;
                        Integer width = pic.getWidth();
                        Integer height = pic.getHeight();
                        Integer picType = WordUtil.getPictureType(pic.getType());
                        String path = pic.getUrl();
                        cell.getParagraphs().get(0).insertNewRun(0).addPicture(FileUtil.getFileInputStream(path), picType, "测试", Units.toEMU(width), Units.toEMU(height));
                    } else {
                        WordUtil.setCellText(tmpCell, cell, String.valueOf(o.toString()));
                    }

                }
            }
        }
        // 删除模版行
        table.removeRow(towIndex);

    }

}
