package org.mangolee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Value("${spring.application.name}")
    //@Value("${eleven}")
    private String appName;
    // 配置 swagger3
    @Bean
    public Docket docket(Environment env) {
        // 设置要启用swagger的环境为开发环境 否则不启用
        //Profiles profiles = Profiles.of("dev");
        return new Docket(DocumentationType.OAS_30)
                //.enable(env.acceptsProfiles(profiles))
                .apiInfo(apiInfo())
                .select()
                // 配置要扫描接口的方式
                // basepackage
                .apis(RequestHandlerSelectors.basePackage("org.mangolee.controller"))
                .build();
    }

    // 定义api信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(appName)
                .description(appName+"接口文档")
                .version("1.0")
                .contact(contact())
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    // 定义作者信息
    private Contact contact() {
        return new Contact(
                "Author",
                "https://github.com/KevinKVJ/Mangolee-CMS",
                "author@gmail.com");
    }

}
