package com.survey.lib.common.filter;

import com.survey.lib.common.context.Current;
import com.survey.lib.common.context.RequestContext;
import com.survey.lib.common.utils.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * @author yanglf
 * @description
 * @since 2019/2/26
 **/
@Component
@ServletComponentScan
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
public class SessionFilter implements Filter {

    private Boolean isServiceNameInit = false;

    private static final Logger log = LoggerFactory.getLogger(SessionFilter.class);

    public static final Logger accessLog = LoggerFactory.getLogger("access");

    public static final Logger serviceLog = LoggerFactory.getLogger("service");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if(!isServiceNameInit){
            setServiceKey();
            isServiceNameInit = true;
        }
        // 设置  请求/响应编码

        HttpServletRequest req = null;
        HttpServletResponse res = null;
        if(servletRequest instanceof  HttpServletRequest){
            req = (HttpServletRequest) servletRequest;
            res = (HttpServletResponse) servletResponse;
        }

        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        setRequestContext(req, res);

        String method = req.getMethod();
        String queryString = req.getQueryString();
        String uri = req.getRequestURI();
        String remoteIp = getRemoteAddr(req);
        String localIp = req.getLocalAddr();

        StringBuilder accessLogSB = new StringBuilder();
        String pvId = generatePVID(req,remoteIp,localIp);
        accessLogSB.append(System.currentTimeMillis()).append("\t").append("ACCESS").append("\t").append(pvId).append("\t")
                .append(localIp).append("\t").append(remoteIp).append("\t").append(uri).append("\t").append(queryString).append("\t");
        RequestContext.getCurrent().setAccessLog(accessLogSB);

        //service log
        StringBuilder serviceLogSB = new StringBuilder();
        RequestContext.getCurrent().setServiceLog(serviceLogSB);

        StringBuilder returnLogSB = new StringBuilder();
        RequestContext.getCurrent().setReturnLog(returnLogSB);

        try {
            chain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){

        }finally {
            //accessLog
            accessLog();
            //service log
            serviceLog(res, uri, pvId);
            //return log
            returnLog(res, pvId);

            RequestContext.remove();
            //RequestContext.removeCurrent();
            //res.sendRedirect("/static/login.html");
        }
    }

    @Override
    public void destroy() {
        log.info("sessionFilter destory... ...");
    }

    private void accessLog() {
        accessLog.info(RequestContext.getCurrent().getAccessLog().toString());
    }

    private void returnLog(HttpServletResponse res, String pvId) {
        accessLog.info(System.currentTimeMillis() + "\t" + "RETURN" + "\t" + pvId + "\t" + res.getStatus() + "\t" + RequestContext.getCurrent().getReturnLog());
    }

    private void serviceLog(HttpServletResponse res,String uri, String pvId) {
        serviceLog.info( System.currentTimeMillis() + "\t"+ pvId + "\t" + uri + "\t" + res.getStatus()+ "\t"+ RequestContext.getCurrent().getServiceLog().toString());
    }


    private void setRequestContext(HttpServletRequest req, HttpServletResponse res) {
        Current current = RequestContext.initCurrent();
        current.setResponse(res);
        current.setRequest(req);
        String sessionId = req.getHeader("SESSIONID");
        if(null == sessionId){
            sessionId = req.getSession(true).getId();
        }
        current.setToken(sessionId);
    }


    private String getRemoteAddr(HttpServletRequest req) {
        String remoteIP = req.getRemoteAddr();

        Object xff = req.getHeader("X-Forwarded-For");
        if (xff instanceof String) {
            String strXff = (String) xff;
            String tip = strXff.split(",")[0];
            if (tip.length() > 0) {
                remoteIP = tip;
            }
        }

        return remoteIP;
    }


    private String generatePVID(HttpServletRequest req, String remoteIP,String localIp){
        try{
            String now = String.valueOf(System.currentTimeMillis());
            String uri = req.getRequestURI();

            // md5 加密
            byte[] btInput = (remoteIP + now + uri + localIp).getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch(Exception e){
            log.error("",e);
        }
        return UuidUtils.generateUuid();
    }


    private void setServiceKey() {
        // 设置  feign 拦截器  service-key
        // 删除权限校验  service-key
        // 设置新的权限校验key service-key
       // String key = UUIDHelper.getUUID();
        //FeignRequestInterceptor.setServerKey(key);
       // authCheckService.delServiceKey(serviceName);
        //authCheckService.setServiceKey(serviceName, key);
    }

}
