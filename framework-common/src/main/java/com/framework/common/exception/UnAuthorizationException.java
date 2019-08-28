package com.framework.common.exception;

/**
 * @author yanglf
 * @description
 * @since 2018/12/25
 **/
public class UnAuthorizationException extends ApiException {

    public UnAuthorizationException(String message) {
        this.msg = message;
    }

    public UnAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizationException(Throwable cause) {
        super(cause);
    }

    public UnAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
