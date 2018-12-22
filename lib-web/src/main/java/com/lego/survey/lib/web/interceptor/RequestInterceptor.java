package com.lego.survey.lib.web.interceptor;

import com.survey.lib.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanglf
 * @description   日志拦截器
 * @since 2018/12/22
 **/
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(String.format("Request from -> IP: %s, Method: %s, URL: %s, Params: %s",
                HttpUtils.getClientIp(request), request.getMethod(), request.getRequestURL().toString(),
                HttpUtils.getRequestParams(request)));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
