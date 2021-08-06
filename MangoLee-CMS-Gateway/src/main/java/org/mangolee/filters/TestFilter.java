package org.mangolee.filters;

import org.mangolee.utils.FilterResponseUtil;
import org.mangolee.entity.Result;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TestFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(false)
        {
            return FilterResponseUtil.response(exchange.getResponse(),Result.success("Helloworld"));
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
