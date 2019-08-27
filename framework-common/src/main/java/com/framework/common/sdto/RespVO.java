package com.framework.common.sdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
@AllArgsConstructor
@ToString
public class RespVO<T> implements Serializable {
    private String ret;
    private int retCode;
    private String msg;
    private T info;


    public RespVO(String ret, int retCode, String msg) {
        this.ret = ret;
        this.retCode = retCode;
        this.msg = msg;
    }
}
