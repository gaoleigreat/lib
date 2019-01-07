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
        String SETTLEMENT="settlement-service";
        String REPORT="report-service";
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
        String BASE_POINT="/survey/api-settlement/base_point";
        String TEMPLATE_REPORT="/survey/api-report/template_report";

    }


    /**
     * 角色
     */
    public interface Role {
        //超级管理员
        String ADMIN = "admin";
        //公司管理员
        String MASTER = "master";
        // 标段测量管理员
        String SECTION = "section";
        // 测量员
        String SURVEYER = "surveyer";
    }


    /**
     * 角色控制 url
     */
    public  interface RolePath{
        //超级管理员  创建 标段测量管理员 以及公司管理员， 标段测量管理员创建测量员
        String USER_CREATE="/user/create";
        //超级管理员
        String PROJECT_CREATE="/project/create";
        //超级管理员
        String PROJECT_DELETE="/project/delete";
    }


}
