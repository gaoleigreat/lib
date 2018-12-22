package com.survey.lib.common.consts;

/**
 * @author yanglf
 * @description   响应信息定义
 * @since 2018/12/22
 **/
public class RespConsts {


    public static final String SUCCESS = "success";
    public static final String FAILURE = "fail";



    /**
     * 失败
     */
    public interface Failure {
        String ret = FAILURE;
        String msg = "请求失败";
    }

    /**
     * 成功
     */
    public interface Success {
        String ret = SUCCESS;
        String msg = "请求成功";
    }
}
