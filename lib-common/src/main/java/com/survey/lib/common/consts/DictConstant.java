package com.survey.lib.common.consts;

/**
 * @author yanglf
 * @description 业务字段
 * @since 2018/12/22
 **/
public class DictConstant {

    /**
     * 定义服务名称
     */
    public interface Service {
        String USER = "user-service";
        String AUTH = "auth-service";
        String PROJECT = "project-service";
    }


    /**
     * 平台
     */
    public interface Platform {
        String APP = "/app";
        String WEB = "/web";
    }


    /**
     * url 映射
     */
    public interface Path {
        String AUTH = "/survey/api-auth/auth";
        String GROUP = "/survey/api-project/group";
        String PROJECT = "/survey/api-project/project";
        String SECTION = "/survey/api-project/section";
        String WORKSPACE = "/survey/api-project/workSpace";
        String USER = "/survey/api-user/user";
        String LOG = "/survey/api-user/log";
        String CONFIG = "/survey/api-user/config";
    }


    /**
     * 角色
     */
    public interface Role {
        String ADMIN = "admin";
        String MASTER = "master";
        String SECTION = "section";
        String SURVEYER = "surveyer";
    }


}
