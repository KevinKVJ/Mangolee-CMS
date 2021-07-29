package org.mangolee.filters;

import org.mangolee.utils.FilterResponseUtil;
import org.mangolee.utils.Result;
import org.mangolee.utils.ResultEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.logging.Handler;

public class TestFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(true)
        {
            return FilterResponseUtil.response(exchange.getResponse(),ResultEnum.BAD_REQUEST);
        }

        System.out.println("Before");
        return chain.filter(exchange).then(Mono.fromRunnable(()->
        {
            exchange.getResponse().addCookie(ResponseCookie.from("CookieKey","CookieValue").build());
            exchange.getResponse().getHeaders().add("HeaderKey","HeaderValue");
            System.out.println("After");
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
