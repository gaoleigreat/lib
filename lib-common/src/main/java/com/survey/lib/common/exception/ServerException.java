package com.survey.lib.common.exception;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class ServerException extends BaseException {

    public ServerException(String message) {
        super(message);
    }

    ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    ServerException(Throwable cause) {
        super(cause);
    }

    ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
