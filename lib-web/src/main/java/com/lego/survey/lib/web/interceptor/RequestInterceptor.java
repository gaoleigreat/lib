package com.lego.survey.lib.web.interceptor;
import com.lego.survey.lib.web.filter.RequestWrapper;
import com.survey.lib.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author yanglf
 * @description 日志拦截器
 * @since 2018/12/22
 **/
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String method = request.getMethod();
            String requestParam = null;
            if (method.equalsIgnoreCase("get") || method.equalsIgnoreCase("delete")) {
                requestParam = HttpUtils.getRequestParams(request);
            } else if (method.equalsIgnoreCase("post") || method.equalsIgnoreCase("patch") || method.equalsIgnoreCase("put")) {
                RequestWrapper requestWrapper;
                if (request instanceof RequestWrapper) {
                    requestWrapper = (RequestWrapper) request;
                    requestParam = HttpUtils.getBodyString(requestWrapper);
                }
            }

            log.info(String.format("Request from -> IP: %s, Method: %s, URL: %s, Params: %s, Headers: %s",
                    HttpUtils.getClientIp(request),
                    request.getMethod(),
                    request.getRequestURL().toString(),
                    requestParam,
                    HttpUtils.getHeaderVo(request)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        PrintWriter writer = response.getWriter();

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
