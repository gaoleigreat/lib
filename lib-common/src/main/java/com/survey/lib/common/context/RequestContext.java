package com.survey.lib.common.context;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class RequestContext {

    public static final Logger log = LoggerFactory.getLogger(RequestContext.class);

    private static ThreadLocal<Current> current = new ThreadLocal<Current>();


    public static void setCurrnet(Current cur) {
        current.set(cur);
    }

    public static Current getCurrent() {
        Current cur = current.get();
        log.info("current:"+ (null != cur));
        if(null != cur && !cur.getGetData() && null != cur.getToken()){
            // 获取到用户数据
            String json="";
            if(!StringUtils.isEmpty(json)){
                Current cache = JSON.parseObject(json, Current.class);
                cur.setUser(cache.getUser());
                cur.setPermissions(cache.getPermissions());
                cur.setSupplierId(cache.getSupplierId());
                cur.setGetData(true);
            }
        }
        return cur;
    }

    public static Current initCurrent(){
        current.remove();
        current.set(new Current());

        return current.get();
    }

    public static void remove(){
        current.remove();
    }

    public static ApplicationContext getApplicationContext() {

        return AppContext.getContext();
    }

    public static WebApplicationContext getWebApplicationContext() {

        return ContextLoader.getCurrentWebApplicationContext();
    }

    public static ServletContext getServletContext() {
        WebApplicationContext wac = getWebApplicationContext();
        if (null == wac) {
            return null;
        }
        return wac.getServletContext();
    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        authCheckService = (IAuthCheckService) AppContext.getContext().getBean("authCheckService");
//    }
}
