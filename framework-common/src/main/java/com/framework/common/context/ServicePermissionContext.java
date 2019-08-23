package com.framework.common.context;

import java.util.Set;

/**
 * @author yanglf
 * @description
 * @since 2019/2/26
 **/
public class ServicePermissionContext {

    private static Set<String> serviceAuthorized;

    public static void setServiceAuthorized(Set<String> serviceAuthorized) {
        ServicePermissionContext.serviceAuthorized = serviceAuthorized;
    }

    public static Set<String> getServiceAuthorized(){
        return ServicePermissionContext.serviceAuthorized;
    }

}
