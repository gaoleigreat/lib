package com.lego.survey.lib.excel.listener;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Slf4j
public abstract class ExcelReadListener<T> extends AnalysisEventListener<T> {



    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        // 解析  excel sheet
        Integer currentRowNum = analysisContext.getCurrentRowNum();

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 数据解析完成  销毁资源

    }


}
