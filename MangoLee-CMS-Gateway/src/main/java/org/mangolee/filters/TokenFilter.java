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

@Component
public class TokenFilter implements GatewayFilter, Ordered {
    //@Autowired
    private RedisService redisService;
    @Value("${spring.application.name}")
    private String appName;
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = getTokenFromHeader(exchange);
        if(token == null)
            return FilterUtil.response(exchange.getResponse(),
                    Result.error(403,appName+" Receives unauthorized request, no token"));

//        Result<UserInfo> userVerify = redisService.verify(token);
//        if(userVerify.getCode()!= 200)
//            return FilterUtil.response(exchange.getResponse(),userVerify);

        return chain.filter(exchange).then(
                Mono.fromRunnable(()->{//to do post filter
                     }));
    }

    private String getTokenFromHeader(ServerWebExchange exchange){
        List<String> tokens = exchange.getRequest().getHeaders().get("token");
        if(tokens == null || tokens.size() != 1){
            return null;
        }
        else{
            return tokens.get(0);
        }
    }

    private Map<String,Object> reflectUserInfo(UserInfo userInfo) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = userInfo.getClass().getDeclaredFields();
        for(Field field:fields)
        {
            field.setAccessible(true);
            map.put(field.getName(),field.get(userInfo));
        }
        return map;
    }
    @Override
    public int getOrder() {
        return 10;
    }
}
