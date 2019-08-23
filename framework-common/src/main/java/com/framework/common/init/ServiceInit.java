package com.framework.common.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author yanglf
 * @description
 * @since 2019/2/26
 **/
@Component
public class ServiceInit implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化  service 和  service 的访问权限

    }
}
