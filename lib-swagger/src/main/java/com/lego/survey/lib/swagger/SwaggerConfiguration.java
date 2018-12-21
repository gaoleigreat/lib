package com.lego.survey.lib.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
        return new Docket(DocumentationType.SWAGGER_2)
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
