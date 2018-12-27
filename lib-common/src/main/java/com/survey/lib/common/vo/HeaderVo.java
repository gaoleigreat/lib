package com.survey.lib.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeaderVo {
    private String token;
    private String time;
    private String deviceType;
    private String osVersion;
    private String sn;
}
