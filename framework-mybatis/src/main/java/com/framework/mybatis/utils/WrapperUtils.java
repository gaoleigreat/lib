package com.framework.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/16 11:18
 * @desc :
 */
@Slf4j
public class WrapperUtils {

    public static void addAdvancedCondition(QueryWrapper wrapper, String symbol, String absoluteField, String value) {
        if (">=".equals(symbol)) {
            wrapper.ge(absoluteField, value);
        } else if (">".equals(symbol)) {
            wrapper.gt(absoluteField, value);
        } else if ("<=".equals(symbol)) {
            wrapper.le(absoluteField, value);
        } else if ("<".equals(symbol)) {
            wrapper.lt(absoluteField, value);
        } else if ("=".equals(symbol)) {
            wrapper.eq(absoluteField, value);
        } else if ("like".equals(symbol)) {
            wrapper.like(absoluteField, "%" + value + "%");
        } else if ("notExists".equals(symbol)) {
            wrapper.exists(absoluteField);
        } else if ("exists".equals(symbol)) {
            wrapper.notExists(absoluteField);
        } else if ("in".equals(symbol)) {
            try {
                if (value.lastIndexOf(",") > 0) {

                    String[] val = value.split(",");
                    Integer[] vals = new Integer[val.length];
                    for (int i = 0; i < val.length; i++) {
                        vals[i] = Integer.valueOf(val[i]);
                    }
                    wrapper.in(absoluteField, Arrays.asList(vals));
                } else {
                    wrapper.in(absoluteField, Arrays.asList(value));
                }
            } catch (Exception e) {
                log.error("", e);
            }
        } else if ("notin".equals(symbol)) {
            try {
                if (value.lastIndexOf(",") > 0) {
                    String[] val = value.split(",");
                    Integer[] vals = new Integer[val.length];
                    for (int i = 0; i < val.length; i++) {
                        vals[i] = Integer.valueOf(val[i]);
                    }
                    wrapper.notIn(absoluteField, Arrays.asList(vals));
                } else {
                    wrapper.notIn(absoluteField, Arrays.asList(new Integer[]{Integer.valueOf(value)}));
                }
            } catch (Exception e) {
                log.error("", e);
            }
        } else {
            throw new RuntimeException("查询符号错误，不能识别的符号");
        }
    }
}
