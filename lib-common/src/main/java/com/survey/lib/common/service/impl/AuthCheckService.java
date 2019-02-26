package com.survey.lib.common.service.impl;
import com.survey.lib.common.service.IAuthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthCheckService implements IAuthCheckService {

    @Value("${session.domain}")
    private String authDomain;

    @Override
    public Boolean check(String sessionId) {
        // 检查  session
        return false;
    }

    @Override
    public Map<String,Object> setServiceKey(String serviceName, String key) {
        // 设置 service-key
        return null;
    }

    @Override
    public Map<String,Object> delServiceKey(String serviceName) {
        // 通过 service name 删除  service-key
        return null;
    }

    @Override
    public String getService(String key) {
       // 通过 service-key 获取  service name
       return null;
    }
}
