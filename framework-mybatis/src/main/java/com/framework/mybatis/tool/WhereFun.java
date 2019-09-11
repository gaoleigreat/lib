package com.framework.mybatis.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/10 17:59
 * @desc :
 */
public interface WhereFun {
    void whereFunc(QueryWrapper wrapper, String field, Object vaule);
}
