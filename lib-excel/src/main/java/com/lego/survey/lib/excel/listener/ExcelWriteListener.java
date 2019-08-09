package com.lego.survey.lib.excel.listener;

import com.alibaba.excel.event.WriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author yanglf
 * @description
 * @since 2019/8/8
 **/
@Slf4j
public abstract class ExcelWriteListener implements WriteHandler {

    @Override
    public void sheet(int sheetNo, Sheet sheet) {
        log.info("finish write sheet:{}", sheetNo);

    }

    @Override
    public void row(int rowNum, Row row) {
        log.info("finish write row:{}", rowNum);

    }

    @Override
    public void cell(int cellNum, Cell cell) {
        log.info("finish write cell:{}", cellNum);

    }
}
