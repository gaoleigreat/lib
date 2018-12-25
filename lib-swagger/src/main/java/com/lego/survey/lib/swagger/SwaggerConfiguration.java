package com.lego.survey.lib.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    @Value("${define.swagger.basePackage:com.lego}")
    private String basePackage;

    @Value("${define.swagger.title:API文档}")
    private String swaggerTitle;

    @Value("${define.swagger.contactName:lego}")
    private String contactName;

    @Value("${define.swagger.contactUrl:lego.cn}")
    private String contactUrl;

    @Value("${define.swagger.contactEmail:survey@legocloud.cn}")
    private String contactEmail;

    @Value("${define.swagger.description:survey}")
    private String swaggerDescription;

    @Value("${define.swagger.serviceUrl:lego.cn}")
    private String swaggerServiceUrl;

    @Value("${define.swagger.version:v1.0}")
    private String swaggerVersion;


    @Bean
    public Docket docket() {
        List<Parameter> parameters=new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("String")).parameterType("header").required(false).build();
        ParameterBuilder versionPar = new ParameterBuilder();
        versionPar.name("version").description("版本号").modelRef(new ModelRef("String")).parameterType("header").required(true).build();
        ParameterBuilder timePar = new ParameterBuilder();
        timePar.name("time").description("当前时间").modelRef(new ModelRef("String")).parameterType("header").required(true).build();
        ParameterBuilder snPar = new ParameterBuilder();
        snPar.name("sn").description("设备序列号").modelRef(new ModelRef("String")).parameterType("header").required(true).build();
        parameters.add(tokenPar.build());
        parameters.add(versionPar.build());
        parameters.add(timePar.build());
        parameters.add(snPar.build());

        List<ResponseMessage> responseMessages=new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(1).message("操作成功").build());
        responseMessages.add(new ResponseMessageBuilder().code(-1).message("权限校验失败").build());
        responseMessages.add(new ResponseMessageBuilder().code(-2).message("登录错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-3).message("服务内部错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-4).message("调用超时错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-5).message("其他错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-6).message("结果错误").build());


        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .globalResponseMessage(RequestMethod.GET,responseMessages)
                .globalResponseMessage(RequestMethod.POST,responseMessages)
                .globalResponseMessage(RequestMethod.PUT,responseMessages)
                .globalResponseMessage(RequestMethod.DELETE,responseMessages)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerTitle)
                .contact(new Contact(contactName,contactUrl,contactEmail))
                .description(swaggerDescription)
                .termsOfServiceUrl(swaggerServiceUrl)
                .version(swaggerVersion)
                .build();
    }

}
