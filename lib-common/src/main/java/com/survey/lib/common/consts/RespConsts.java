package com.survey.lib.common.consts;

/**
 * @author yanglf
 * @description   响应信息定义
 * @since 2018/12/22
 **/
public class RespConsts {


    public static final String SUCCESS = "success";
    public static final String FAILURE = "fail";


    public  static  final int SUCCESS_RESULT=1;
    /**
     * 权限认证失败
     */
    public  static  final  int FAIL_NOPRESSION=-1;

    /**
     * 服务内部错误
     */
    public  static  final int ERROR_SERVER_ERROR=-2;

    /**
     * 调用超时
     */
    public static  final int ERROR_TIMEOUT=-3;
    /**
     * 其他错误
     */
    public static final int ERROR_OTHER=-4;

    /**
     * 结果错误
     */
    public static final int ERROR_RESULT=-5;

    /**
     * 失败
     */
    public interface Failure {
        String ret = FAILURE;
        int retCode=ERROR_RESULT;
        String msg = "请求失败";
    }

    /**
     * 成功
     */
    public interface Success {
        String ret = SUCCESS;
        int retCode=SUCCESS_RESULT;
        String msg = "请求成功";
    }
}
