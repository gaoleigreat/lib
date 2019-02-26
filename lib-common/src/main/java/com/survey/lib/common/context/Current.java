package com.survey.lib.common.context;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.survey.lib.common.entity.UserPriciple;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Set;

public class Current implements Serializable {

    private UserPriciple user;

    private Set<String> permissions;

    private Long supplierId;

    @JSONField(serialize = false)
    @JsonIgnore
    private Boolean isPermissionChecked = false;

    @JSONField(serialize = false)
    @JsonIgnore
    private String token;

    @JSONField(serialize = false)
    @JsonIgnore
    private StringBuilder serviceLog = new StringBuilder();

    @JSONField(serialize = false)
    @JsonIgnore
    private StringBuilder accessLog = new StringBuilder();

    @JSONField(serialize = false)
    @JsonIgnore
    private StringBuilder returnLog = new StringBuilder();

    @JSONField(serialize = false)
    @JsonIgnore
    private HttpServletRequest request;

    @JSONField(serialize = false)
    @JsonIgnore
    private HttpServletResponse response;

    @JSONField(serialize = false)
    @JsonIgnore
    private Boolean getData = false;

    @JsonIgnore
    public HttpServletRequest getRequest() {
        return request;
    }

    @JsonIgnore
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @JsonIgnore
    public HttpServletResponse getResponse() {
        return response;
    }

    @JsonIgnore
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @JsonIgnore
    public StringBuilder getServiceLog() {
        return serviceLog;
    }

    @JsonIgnore
    public void setServiceLog(StringBuilder serviceLog) {
        this.serviceLog = serviceLog;
    }

    public UserPriciple getUser() {
        return user;
    }

    public void setUser(UserPriciple user) {
        this.user = user;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getPermissionChecked() {
        return isPermissionChecked;
    }

    public void setPermissionChecked(Boolean permissionChecked) {
        isPermissionChecked = permissionChecked;
    }

    public StringBuilder getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(StringBuilder accessLog) {
        this.accessLog = accessLog;
    }

    public StringBuilder getReturnLog() {
        return returnLog;
    }

    public void setReturnLog(StringBuilder returnLog) {
        this.returnLog = returnLog;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Boolean getGetData() {
        return getData;
    }

    public void setGetData(Boolean getData) {
        this.getData = getData;
    }
}
