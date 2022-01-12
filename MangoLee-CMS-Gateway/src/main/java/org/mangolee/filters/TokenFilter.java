package org.mangolee.filters;

import lombok.SneakyThrows;
import org.mangolee.entity.Result;
import org.mangolee.entity.UserInfo;
import org.mangolee.service.RedisService;
import org.mangolee.utils.FilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Token过滤器，属于非global，当路由选项配置此过滤器将会保证成功路由的请求都带有token，否则过滤并返回json信息
 */
@Component
public class TokenFilter implements GatewayFilter, Ordered {
    @Autowired
    private RedisService redisService;
    @Value("${spring.application.name}")
    private String appName;
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = getTokenFromHeader(exchange);
        if(token == null)
            return FilterUtil.response(exchange.getResponse(),
                    Result.error(403,appName+" 验证错误 请求无Token"));

        Result<Long> ttl = redisService.getKeyTtl(token);
        if(ttl.getData() < 0 )
            return FilterUtil.response(exchange.getResponse(), new Result(403,appName+"验证错误 Token过期或不存在",null));

        return chain.filter(exchange).then(
                Mono.fromRunnable(()->{//to do post filter
                }));
    }

    /**
     * 从请求头取出token项
     * @param exchange
     * @return
     */
    private String getTokenFromHeader(ServerWebExchange exchange){
        List<String> tokens = exchange.getRequest().getHeaders().get("token");
        if(tokens == null || tokens.size() != 1){
            return null;
        }
        else{
            return tokens.get(0);
        }
    }


    @Override
    public int getOrder() {
        return 10;
    }
}