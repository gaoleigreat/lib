package com.survey.lib.common.entity;

public class ServicePermission extends BaseModel {

    private static final long serialVersionUID = 5292289745947751530L;

    private String serviceName;

    private String authorized;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }
}
