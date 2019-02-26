package com.survey.lib.common.entity;

public class BaseService extends BaseModel {

    private static final long serialVersionUID = -8401358860964633859L;

    private String serviceName;

    private String contextPath;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
