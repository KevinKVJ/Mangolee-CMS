package org.mangolee.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class FilterResponseUtil {
    static public Mono<Void> response(ServerHttpResponse response, int statusCode, String msg, Object data)
    {
        response.setStatusCode(HttpStatus.valueOf(200));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", statusCode);
        jsonObject.put("message",msg);
        jsonObject.put("data",data);
        //把json对象转换成字节数组
        byte[] bytes = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
        //把字节数据转换成一个DataBuffer
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
    static public Mono<Void> response(ServerHttpResponse response,Result result)
    {
        return response(response,result.getCode(),result.getMessage(),result.getData());
    }
}
