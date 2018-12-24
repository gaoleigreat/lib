package com.survey.lib.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RespVO<T> implements Serializable {
    private String ret;
    private String msg;
    private T info;
}
