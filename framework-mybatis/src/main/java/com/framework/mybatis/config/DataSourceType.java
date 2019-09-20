package com.framework.mybatis.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@AllArgsConstructor
@Getter
public enum DataSourceType {
    /**
     * 读库
     */
    read("read", "从库"),
    /**
     * 写库
     */
    write("write", "主库"),
    /**
     * 共享库
     */
    share("share","共享数据");

    private String type;
    private String name;

}
