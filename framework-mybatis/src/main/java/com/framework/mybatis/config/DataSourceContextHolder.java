package com.framework.mybatis.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Slf4j
class DataSourceContextHolder {
    @Getter
    private static final ThreadLocal<String> local = new ThreadLocal<>();

    static void read() {
        local.set(DataSourceType.read.getType());
        log.debug("切换数据源 -> 从");
    }

    static void write() {
        local.set(DataSourceType.write.getType());
        log.debug("切换数据源 -> 主");
    }

    static String getJdbcType() {
        return local.get();
    }

}
