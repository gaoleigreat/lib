package com.framework.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseRowModel {
    @ExcelProperty(index = 0)
    private Long id;
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private int age;
}
