package com.pawn.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * description: 日志接口配置
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/6 10:49
 */
@Configuration
@EnableSwagger2
public class Knife4jConfiguration {

    //版本
    public static final String VERSION = "1.0.0";
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("博客系统API文档")
                        .description("博客系统API文档")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .contact("2978824265@qq.com")
                        .version(VERSION)
                        .build())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.pawn.blog.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
