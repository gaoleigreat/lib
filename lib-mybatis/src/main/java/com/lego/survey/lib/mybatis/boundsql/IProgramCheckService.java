package com.lego.survey.lib.mybatis.boundsql;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/2/25
 **/
public interface IProgramCheckService {

    /**
     *
     * @param dimensionCode
     * @param resourceValue
     * @param operationValue
     * @return
     */
    List<String> findProgramItems(String dimensionCode, String resourceValue, String operationValue);


}
