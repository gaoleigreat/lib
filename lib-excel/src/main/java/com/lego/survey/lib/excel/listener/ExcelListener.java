package com.lego.survey.lib.excel.listener;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.lego.survey.lib.excel.model.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Slf4j
public abstract class ExcelListener extends AnalysisEventListener {



    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        // 解析  excel sheet
        Integer currentRowNum = analysisContext.getCurrentRowNum();
        if(o instanceof  User){
            User user= (User) o;
            log.info("当前行:{},data:{}",currentRowNum,user);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 数据解析完成  销毁资源

    }


}
