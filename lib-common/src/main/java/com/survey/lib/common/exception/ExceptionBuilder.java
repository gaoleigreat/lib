package com.survey.lib.common.exception;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class ExceptionBuilder {
    /**
     * 资源不存在
     * @param resource 资源名称
     * @param pk 资源唯一标识
     * @param val 资源唯一标识值
     * @return
     */
    public static void resourceNotFound(String resource, String pk, Object val) {
        throw new ResourceNotFoundException(resource + "[" + pk + "=" + val + "] not found.");
    }


    /**
     * 服务异常
     * @param message
     */
    public static void  serviceException(String message){
        throw  new ServerException(message);
    }

    /**
     * 未注册异常
     */
    public static void unregisteredException(){
        throw new UnregisteredException("账号未注册");
    }


    /**
     *  登录超时异常
     */
    public static void sessionTimeoutException(){
        throw new SessionTimeoutException("登录超时");
    }




}
