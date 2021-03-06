package org.mangolee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
public class RedisServiceProvidersApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisServiceProvidersApplication.class, args);
    }

}
