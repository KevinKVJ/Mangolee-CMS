package org.mangolee.filters;


import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
public class GlobalFilterConfig {
    @Bean
    @Order(0)
    public GlobalFilter testFilter()
    {
        return new TestFilter();
    }
}



