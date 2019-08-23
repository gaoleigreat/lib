package com.framework.common.exception;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class SessionTimeoutException extends  ApiException{

    public SessionTimeoutException(String message) {
        super(message);
    }

    public SessionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionTimeoutException(Throwable cause) {
        super(cause);
    }

    public SessionTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
