package com.lego.survey.lib.web.controller;

import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Controller
public class NotFountController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    @ResponseBody
    public RespVO error() {
        return RespVOBuilder.failure(RespConsts.ERROR_OTHER_CODE,"resource not found");
    }

}
