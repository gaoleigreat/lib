package com.framework.mybatis.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Slf4j
public class DataSourceContextHolder {

    static final String DEFAULT_DB = DataSourceType.read.getName();


    /*@Getter*/
    /**
     *
     */
    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    static void read() {
        LOCAL.set(DataSourceType.read.getType());
        log.debug("切换数据源 -> 从");
    }

    static void write() {
        LOCAL.set(DataSourceType.write.getType());
        log.debug("切换数据源 -> 主");
    }


    private static void share() {
        LOCAL.set(DataSourceType.share.getType());
        log.debug("切换到共享数据源 ");
    }


    static String getJdbcType() {
        return LOCAL.get();
    }


    static void setDB(String dataSource) {
        if ((DataSourceType.read.getType()).equals(dataSource)) {
            read();
        } else if ((DataSourceType.share.getType()).equals(dataSource)) {
            share();
        } else {
            write();
        }
    }


    static void cleanDB() {
        LOCAL.remove();
    }

}
