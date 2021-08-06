package org.mangolee.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.mangolee.entity.Result;
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
        Result<Object> result = new Result<>(statusCode, msg, data);
        //把json对象转换成字节数组
        byte[] bytes = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue)
                .getBytes(StandardCharsets.UTF_8);
        //把字节数据转换成一个DataBuffer
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
    static public Mono<Void> response(ServerHttpResponse response, Result result)
    {
        return response(response,result.getCode(),result.getMessage(),result.getData());
    }
}
