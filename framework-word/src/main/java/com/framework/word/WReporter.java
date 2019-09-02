package com.framework.word;


import com.framework.common.element.OObject;
import com.framework.common.utils.FileUtil;
import com.framework.word.util.WordUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class WReporter {
    private static final Logger logger = LoggerFactory.getLogger(WReporter.class);

    private String templatePath;
    private XWPFDocument doc = null;
    private FileInputStream is = null;
    private OutputStream os = null;

    public WReporter(String templatePath) throws IOException {
        this.templatePath = templatePath;
        init();
    }

    public void init() throws IOException {
        is = new FileInputStream(new File(this.templatePath));
        doc = new XWPFDocument(is);
    }

    /**
     * 替换掉文档里面占位符
     *
     * @param wDoc
     * @return
     * @throws Exception
     */
    public boolean export(OObject wDoc) throws Exception {
        WordUtil.replaceInPara(doc, wDoc);
        return true;
    }

    /**
     * 替换掉表格中的占位符
     *
     * @param wTable
     * @param tableIndex
     * @return
     * @throws Exception
     */
    public boolean export(OObject wTable, int tableIndex) throws Exception {
        WordUtil.replaceInTable(doc, wTable, tableIndex);
        return true;
    }

    /**
     * 循环生成表格
     *
     * @param wTables
     * @param tableIndex
     * @return
     * @throws Exception
     */
    public boolean export(List<OObject> wTables, int tableIndex, int towIndex) throws Exception {

        WordUtil.insertValueToTable(doc, wTables, tableIndex, towIndex);
        return true;
    }


    /**
     * 导出图片 todo
     *
     * @param params
     * @return
     * @throws Exception
     */
    public boolean exportImg(Map<String, Object> params) throws Exception {
        doc.getParagraphs().get(0);

        return true;
    }


    /**
     * 生成word文档
     *
     * @param outDocPath
     * @return
     * @throws IOException
     */
    public boolean generate(String outDocPath) throws IOException {
        os = new FileOutputStream(outDocPath);
        doc.write(os);
        FileUtil.close(os);
        FileUtil.close(is);
        return true;
    }


    /**
     * 查找段落里面的变量
     *
     * @throws Exception
     */
    public Set<String> findInPara() throws Exception {
        Set<String> resultSet = new HashSet<>();
        resultSet = WordUtil.findInPara(doc);
        return resultSet;
    }

    /**
     * 查找表格里面的变量
     * -1 或空表示查询所有表格
     *
     * @param
     * @throws Exception
     */
    public List<Set<String>> findInTable(Integer tableIndex) throws Exception {
        List<XWPFTable> tableList = doc.getTables();

        List<Set<String>> result = new ArrayList<>();
        if (null == tableIndex || tableIndex == -1) {
            for (int i = 0; i < tableList.size(); i++) {
                Set<String> resultSet = new HashSet<>();

                resultSet.addAll(WordUtil.findInTable(tableList.get(i)));
                result.add(resultSet);
            }
        } else {
            if (tableList.size() <= tableIndex) {
                throw new Exception("tableIndex对应的表格不存在");
            }
            Set<String> resultSet = new HashSet<>();
            resultSet.addAll(WordUtil.findInTable(tableList.get(tableIndex)));
            result.add(resultSet);
        }
        return result;
    }


}
