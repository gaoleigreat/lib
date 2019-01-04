package com.lego.survey.lib.mybatis.config;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@AllArgsConstructor
@Getter
public enum  DataSourceType {
    read("read", "从库"), write("write", "主库");
    private String type;
    private String name;
}
