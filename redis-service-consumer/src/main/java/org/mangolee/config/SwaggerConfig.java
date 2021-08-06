package org.mangolee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.mangolee.controller"))
                .build();
    }

    // 定义api信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MangoLee-CMS")
                .description("redis-service-consumer接口文档")
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
