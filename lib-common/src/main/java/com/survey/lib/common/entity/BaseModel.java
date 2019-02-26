package com.survey.lib.common.entity;
import com.survey.lib.common.context.RequestContext;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {

    private static final long serialVersionUID = -2448582046557033044L;

    private Long id;

    private Date creationDate;

    private Long createdBy;

    private Date lastUpdateDate;

    private Long lastUpdatedBy;

    private String lastUpdateUser;

    private String createUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public  void setCreateInfo(){
        UserPriciple user = RequestContext.getCurrent().getUser();
        if(null != user){
            this.createdBy = user.getId();
            this.lastUpdatedBy = user.getId();
        }
        this.creationDate = new Date();
        this.lastUpdateDate = new Date();
    }

    public  void setUpdateInfo(){
        UserPriciple user = RequestContext.getCurrent().getUser();
        if(null != user){
            this.lastUpdatedBy = user.getId();
        }
        this.lastUpdateDate = new Date();
    }
}
