package com.survey.lib.common.utils;

import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.exception.ExceptionBuilder;
import com.survey.lib.common.vo.HeaderVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
public class HeaderUtils {

    public HeaderVo parseHeader(HttpServletRequest request) {
        try {
            String deviceType = request.getHeader(HttpConsts.DEVICE_TYPE);
            String sn = request.getHeader(HttpConsts.HEADER_SN);
            String time = request.getHeader(HttpConsts.HEADER_TIME);
            String token = request.getHeader(HttpConsts.HEADER_TOKEN);
            String osVersion = request.getHeader(HttpConsts.OS_VERSION);
            return HeaderVo.builder()
                    .deviceType(deviceType)
                    .osVersion(osVersion)
                    .sn(sn)
                    .time(time)
                    .token(token)
                    .build();
        } catch (Exception ex) {
            ExceptionBuilder.unKnownException();
            return null;
        }
    }


}
