package org.mangolee.utils;

import com.alibaba.fastjson.JSONObject;
import org.mangolee.entity.Result;
import org.mangolee.entity.UserInfo;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FilterUtil {
    /**
     * 执行拦截并回复，在filter中调用，封装json格式直接返回，不进行下一步路由
     *
     * @param response
     * @param statusCode
     * @param msg
     * @param data
     * @return
     */
    static public Mono<Void> response(ServerHttpResponse response, int statusCode, String msg, Object data) {
        response.setStatusCode(HttpStatus.valueOf(200));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", statusCode);
        jsonObject.put("message", msg);
        jsonObject.put("data", data);
        //把json对象转换成字节数组
        byte[] bytes = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
        //把字节数据转换成一个DataBuffer
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    static public Mono<Void> response(ServerHttpResponse response, Result result) {
        return response(response, result.getCode(), result.getMessage(), result.getData());
    }

    /**
     * 从exchange中创建新request，并在新request中填充请求参数
     * @param exchange
     * @param ParamMap
     * @return
     */
    static public ServerHttpRequest addRequestParam(ServerWebExchange exchange, Map<String,Object> ParamMap) {
        URI uri = exchange.getRequest().getURI();
        StringBuilder query = new StringBuilder();
        String originalQuery = uri.getRawQuery();
        if (StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }
        for(Map.Entry<String,Object> entry:ParamMap.entrySet()) {
            query.append(entry.getKey());
            query.append('=');
            query.append(entry.getValue().toString());
            query.append("&");
        }
        //去除最后一个&
        query.deleteCharAt(query.length()-1);

        URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build(true).toUri();
        ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
        return request;
    }

    /**
     * 反射爆破遍历pojo类，只适合单层，配合{@link #addRequestParam(ServerWebExchange, Map)}使用
     * @param pojo
     * @return
     * @throws IllegalAccessException
     */
    static public Map<String,Object> reflectPojo(Object pojo) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = pojo.getClass().getDeclaredFields();
        for(Field field:fields)
        {
            field.setAccessible(true);
            map.put(field.getName(),field.get(pojo));
        }
        return map;
    }
}
