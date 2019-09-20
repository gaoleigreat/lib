package com.framework.mybatis.config;

import com.framework.mybatis.annotation.DB;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Aspect
@Order(1)
@Component
public class DataSourceAop {


    @Before(value = "@annotation(com.framework.mybatis.annotation.DB)")
    public void beforeSwitchDS(JoinPoint point) {
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.DEFAULT_DB;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DB.class)) {
                DB annotation = method.getAnnotation(DB.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 切换数据源
        DataSourceContextHolder.setDB(dataSource);
    }


    @After("@annotation(com.framework.mybatis.annotation.DB)")
    public void afterSwitchDS(JoinPoint point) {
        DataSourceContextHolder.read();
    }


    /**
     * 遇到开启事务时，将数据源切换为写
     */
    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void setReadDataSourceType() {
        DataSourceContextHolder.write();
    }

    /**
     * 事务方法结束后，切换数据源为读
     */
    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.read();
    }
}