package com.framework.common.exception;

import lombok.Data;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
public class BaseException extends RuntimeException {

    protected String msg;

    public BaseException() {
    }

    public BaseException(String message) {
        this.msg = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
