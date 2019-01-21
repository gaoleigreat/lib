package com.lego.survey.lib.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Service
public class TecentSmsServiceImpl implements ISmsSenderService {

    @Value("${define.survey.sms.tecent.appKey}")
    private String appKey;


    @Override
    public void sendSms(String sendPhone) {

    }
}
