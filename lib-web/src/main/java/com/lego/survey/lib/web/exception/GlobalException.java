package com.lego.survey.lib.web.exception;

import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.exception.*;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.invoke.ParameterMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
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


    @ExceptionHandler(value = ParameterMappingException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ParameterMappingException ex){
        log.error("参数缺失:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_OTHER_CODE,"参数["+ex.getParameter().getName()+"]缺失");
    }



    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(MissingServletRequestParameterException ex){
        log.error("参数缺失:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_OTHER_CODE,"参数["+ex.getParameterName()+"]缺失");
    }



    @ExceptionHandler(value = SessionTimeoutException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(SessionTimeoutException ex){
        log.error("登录超时:{}",ex);
        return RespVOBuilder.success(RespConsts.FAIL_LOGIN_CODE,ex.getMessage());
    }


    @ExceptionHandler(value = UnregisteredException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(UnregisteredException ex){
        log.error("未注册:{}",ex);
        return RespVOBuilder.success(RespConsts.FAIL_LOGIN_CODE,ex.getMessage());
    }

    @ExceptionHandler(value = UnAuthorizationException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(UnAuthorizationException ex){
        log.error("权限缺失:{}",ex);
        return RespVOBuilder.success(RespConsts.FAIL_NOPRESSION_CODE,RespConsts.FAIL_NOPRESSION_MSG);
    }


    @ExceptionHandler(value = CallTimeoutException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(CallTimeoutException ex){
        log.error("调用失败:{}",ex);
        return RespVOBuilder.success(RespConsts.ERROR_CALLTIMEOUT_CODE,ex.getMessage());
    }



    @ExceptionHandler(value = ServerException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ServerException ex){
        log.error("服务异常:{}",ex);
        return RespVOBuilder.error(RespConsts.ERROR_SERVER_CODE,ex.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(Exception ex){
        log.error("服务器异常:{}",ex);
        return RespVOBuilder.error(RespConsts.ERROR_OTHER_CODE,ex.getMessage());
    }
}
