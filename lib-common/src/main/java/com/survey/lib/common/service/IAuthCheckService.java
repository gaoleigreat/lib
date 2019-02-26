package com.survey.lib.common.service;

import java.util.Map;

/**
 * 登录校验
 * weihao 2018-07-05
 */
public interface IAuthCheckService {

    /**
     * 校验
     * @param sessionId
     * @return
     */
    Boolean check(String sessionId);

    /**
     * 服务认证，设置服务令牌
     * @param serviceName
     * @param key
     * @return
     */
    Map<String,Object> setServiceKey(String serviceName, String key);

    /**
     * 删除服务令牌
     * @param serviceName
     * @return
     */
    Map<String,Object> delServiceKey(String serviceName);

    /**
     * 根据令牌获取服务名称
     * @param key
     * @return
     */
    String getService(String key);
}
