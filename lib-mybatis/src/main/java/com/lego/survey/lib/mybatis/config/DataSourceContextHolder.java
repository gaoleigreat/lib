package com.lego.survey.lib.mybatis.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Slf4j
public class DataSourceContextHolder {
    @Getter
    private static final ThreadLocal<String> local = new ThreadLocal<>();

    public static void read() {
        local.set(DataSourceType.read.getType());
        log.debug("切换数据源 -> 从");
    }

    public static void write() {
        local.set(DataSourceType.write.getType());
        log.debug("切换数据源 -> 主");
    }

    public static String getJdbcType() {
        return local.get();
    }

}
