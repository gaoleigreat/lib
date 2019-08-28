package com.framework.common.exception;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class UnregisteredException extends ApiException {

    public UnregisteredException(String message) {
        this.msg = message;
    }

    public UnregisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnregisteredException(Throwable cause) {
        super(cause);
    }

    public UnregisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
