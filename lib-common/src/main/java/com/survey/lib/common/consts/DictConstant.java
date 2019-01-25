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
        String SETTLEMENT = "settlement-service";
        String REPORT = "report-service";
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
        String BASE_POINT = "/survey/api-settlement/basePoint";
        String TEMPLATE_REPORT = "/survey/api-report/templateReport";
        String EXCEL_REPORT = "/survey/api-report/excelReport";
        String WORD_REPORT="/survey/api-report/wordReport";
        String PDF_REPORT="/survey/api-report/pdfReport";
        String SURVEY_POINT = "/survey/api-settlement/surveyPoint";
        String SURVEY_RESULT = "/survey/api-settlement/surveyResult";
        String SURVEY_ORIGINAL = "/survey/api-settlement/surveyOriginal";
        String SURVEY_TASK = "/survey/api-settlement/surveyTask";
        String SURVEY_POINT_TYPE = "/survey/api-settlement/surveyPointType";
        String SURVEY_POINT_EXCEPTION = "/survey/api-settlement/surveyPointException";

    }


    /**
     * 角色
     */
    public interface Role {
        //系统管理员
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
    public interface RolePath {
        //超级管理员  创建 标段测量管理员 以及公司管理员， 标段测量管理员创建测量员
        String USER_CREATE = "/user/create";
        // 删除用户
        String USER_DELETE="/user/delete";
        //系统管理员
        String PROJECT_CREATE = "/project/create";
        // 修改工程信息
        String PROJECT_MODIFY="/project/modify";
        //超级管理员
        String PROJECT_DELETE = "/project/delete";
        //标段创建
        String SECTION_CREATE="/section/create";
        // 删除标段
        String SECTION_DELETE="/section/delete";
        // 修改标段信息
        String SECTION_MODIFY="/section/modify";
        // 创建工区
        String WORKSPACE_CREATE="/workspace/create";
        // 删除工区
        String WORKSPACE_DELETE="/workspace/delete";
        // 修改工区
        String WORKSPACE_MODIFY="/workspace/modify";
    }


    public interface TableNamePrefix {
        //测量点表前缀
        String SURVEY_POINT = "survey_point_";
        // 测量成果数据表前缀
        String SURVEY_RESULT = "survey_result_";
        // 原始数据表前缀
        String SURVEY_ORIGINAL = "survey_original_";
        // 任务表前缀
        String SURVEY_TASK = "survey_task_";
    }


    /**
     * 场景
     */
    public interface Scenes {
        // 基础服务
        String JCFW = "jcfw";
        // 沉降服务
        String CJFW = "cjfw";
        // 管片服务
        String GPFW = "gpfw";
        //数据推送服务
        String SJFW = "sjfw";
        // 成果分享服务
        String CGFW = "cgfw";
    }


    /**
     *
     * 数据是否有效
     */
    public interface  IsValid{
        int YES=0;
        int NO=1;
    }


}
