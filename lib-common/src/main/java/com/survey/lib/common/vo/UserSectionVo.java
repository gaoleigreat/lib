package com.survey.lib.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2019/7/9
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSectionVo {
    // 标段ID
    private String id;
    // 标段名称
    private String sectionName;
    // 角色
    private String role;
}
