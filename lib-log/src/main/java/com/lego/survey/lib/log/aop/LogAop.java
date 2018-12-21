package com.lego.survey.lib.log.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Aspect
@Component
@Order(1)
@Slf4j
public class LogAop {

    private ThreadLocal threadLocal=new ThreadLocal();

    @Pointcut("(@annotation(com.lego.survey.lib.log.annotation.Log))")
    public  void logAspect(){

    }

    @Before(value = "logAspect()")
    public  void  doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String token = request.getHeader("token");
        String remoteAddr = request.getRemoteAddr();
        threadLocal.set(System.currentTimeMillis());
        log.info("请求用户:{};请求url:{};请求用户ip:{},请求时间:{}",token,uri,remoteAddr, LocalDate.now().toString());
        log.info("请求方法:{};请求参数:{}",joinPoint.getSignature(), Arrays.asList(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "resp",pointcut = "logAspect()")
    public  void  doAfter(Object resp){
        String jsonStr = JSON.toJSONString(resp);
        long startTime = (Long) threadLocal.get();
        long costTime=startTime-System.currentTimeMillis();
        log.info("请求完成，返回参数:{},当前时间:{};总耗时:{}",jsonStr,LocalDate.now().toString(),costTime);
        threadLocal=null;
    }


}
