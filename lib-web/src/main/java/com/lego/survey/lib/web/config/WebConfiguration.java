package com.lego.survey.lib.web.config;

import com.lego.survey.lib.web.interceptor.AuthInterceptor;
import com.lego.survey.lib.web.interceptor.ParamterInterceptor;
import com.lego.survey.lib.web.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new ParamterInterceptor())
                .addPathPatterns("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }
}
