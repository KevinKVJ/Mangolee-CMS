package org.mangolee;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication()
public class InterceptorApp {
    public static void main(String[] args) {
        SpringApplication.run(InterceptorApp.class,args);
    }
}
