package org.mangolee;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"org.mangolee.service"})
public class InterceptorApp {
    public static void main(String[] args) {
        SpringApplication.run(InterceptorApp.class,args);
    }
}
