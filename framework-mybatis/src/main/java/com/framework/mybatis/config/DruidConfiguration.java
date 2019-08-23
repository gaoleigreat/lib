package com.framework.mybatis.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanglf
 * @description  数据访问监控
 * @since 2018/12/22
 **/
@Configuration
public class DruidConfiguration {

    @Value("${define.druid.user.name:admin}")
    private String userName;

    @Value("${define.druid.user.password:admin123}")
    private String password;

    @Value("${define.druid.allow:}")
    private String allow;

    @Value("${define.druid.deny:}")
    private String deny;

    @Value("${define.druid.url:/druid/*}")
    private String url;

    @Value("${define.druid.resetEnable:true}")
    private String resetEnable;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.setName("DruidStatView");
        servletRegistrationBean.addUrlMappings(url);
        // 不允许清空统计数据
        servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
        servletRegistrationBean.addInitParameter("loginUsername",userName);
        servletRegistrationBean.addInitParameter("loginPassword",password);
        servletRegistrationBean.addInitParameter("allow",allow);
        servletRegistrationBean.addInitParameter("deny",deny);
        return servletRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        // 配置需要监控的URL
        filterRegistrationBean.addUrlPatterns("/*");
        // 排除不需要监控的 资源
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        //  置profileEnable能够监控单个url调用的sql列表
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        //  将 druid 与 cookie 里的用户信息关联  可以监控到用户的sql操作
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        //  将 druid 与 session 里的用户信息关联  可以监控到用户的sql操作
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        filterRegistrationBean.addInitParameter("DruidWebStatFilter", "/*");
        return filterRegistrationBean;
    }

}
