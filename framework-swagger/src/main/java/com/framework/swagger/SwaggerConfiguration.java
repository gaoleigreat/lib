package com.framework.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@ConditionalOnExpression(value = "!'${spring.profiles.active}'.equals('prod')")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    private static final String CLIENT_ID = "admin";
    private static final String CLIENT_SECRET = "admin123";
    private static final String AUTH_SERVER = "/";
    @Value("${define.swagger.basePackage:com.lego}")
    private String basePackage;

    @Value("${define.swagger.title:后台接口api文档}")
    private String swaggerTitle;

    @Value("${define.swagger.contactName:lego}")
    private String contactName;

    @Value("${define.swagger.contactUrl:lego.cn}")
    private String contactUrl;

    @Value("${define.swagger.contactEmail:survey@legocloud.cn}")
    private String contactEmail;

    @Value("${spring.application.name}")
    private String swaggerDescription;

    @Value("${define.swagger.serviceUrl:lego.cn}")
    private String swaggerServiceUrl;

    @Value("${define.swagger.version:1.0}")
    private String swaggerVersion;

    @Autowired
    private Environment environment;


    @Bean
    public Docket docket() {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("String")).parameterType("header").required(false).build();

        parameters.add(tokenPar.build());

        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(1).message("操作成功").build());
        responseMessages.add(new ResponseMessageBuilder().code(-1).message("权限校验失败").build());
        responseMessages.add(new ResponseMessageBuilder().code(-2).message("登录错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-3).message("服务内部错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-4).message("调用超时错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-5).message("其他错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-6).message("结果错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(-7).message("数据处理异常").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        //  .securitySchemes(Collections.singletonList(securityScheme()))
        //.securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerTitle)
                //  .contact(new Contact(contactName,contactUrl,contactEmail))
                .description("restful风格接口，服务名称：" + environment.getProperty("application.description") + "</br>路由标识：" + environment.getProperty("spring.application.name")).version("1.0")
                //   .termsOfServiceUrl(swaggerServiceUrl)
                .version(swaggerVersion)
                .build();
    }




   /* @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }*/


   /* private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/token", "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(AUTH_SERVER + "/authorize", CLIENT_ID, CLIENT_ID))
                .build();
        return new OAuthBuilder().name("spring_oauth")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }


    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                new AuthorizationScope("foo", "Access foo API") };
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("spring_oauth", scopes())))
                .forPaths(PathSelectors.regex("/user/*"))
                .build();
    }
*/


}
