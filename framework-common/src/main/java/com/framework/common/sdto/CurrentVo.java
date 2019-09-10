package com.framework.common.sdto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author yanglf
 * @description
 * @since 2019/7/9
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentVo {

    @JSONField(serialize = false)
    private Boolean isInit = false;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 昵称
     */
    private String name;
    /**
     * 所属单位ID
     */
    private String groupId;
    /**
     * 所属单位名称
     */
    private String groupName;
    /**
     * 工程角色信息
     */
    private List<ProjectRole> projectRoles;
    /**
     * 权限点
     */
    private Set<String> permissions;
    private String token;
    private String deviceType;
    /**
     * 是否验证权限
     */
    private boolean permissionChecked;
}

@Data
class ProjectRole {
    private Long projectId;
    private String projectName;
    private Long roleId;
    private String roleName;
}
