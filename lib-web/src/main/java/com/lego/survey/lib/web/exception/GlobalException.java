package com.lego.survey.lib.web.exception;

import com.survey.lib.common.exception.ResourceNotFoundException;
import com.survey.lib.common.exception.ServerException;
import com.survey.lib.common.exception.SessionTimeoutException;
import com.survey.lib.common.exception.UnregisteredException;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Slf4j
@ControllerAdvice
public class GlobalException {


    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ResourceNotFoundException ex){
        log.error("找不到资源:{}",ex);
        return RespVOBuilder.failure(ex.getMessage());
    }



    @ExceptionHandler(value = SessionTimeoutException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(SessionTimeoutException ex){
        log.error("登录超时:{}",ex);
        return RespVOBuilder.failure(ex.getMessage());
    }


    @ExceptionHandler(value = UnregisteredException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(UnregisteredException ex){
        log.error("未注册:{}",ex);
        return RespVOBuilder.failure(ex.getMessage());
    }


    @ExceptionHandler(value = ServerException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ServerException ex){
        log.error("服务异常:{}",ex);
        return RespVOBuilder.failure(ex.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(Exception ex){
        log.error("服务器异常:{}",ex);
        return RespVOBuilder.failure("系统异常");
    }
}
