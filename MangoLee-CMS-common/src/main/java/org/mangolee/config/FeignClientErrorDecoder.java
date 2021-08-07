package org.mangolee.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.mangolee.exception.MyFeignException;
import org.mangolee.entity.Result;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 将feign异常的返回值转化成现有的feign异常类
 */
@Configuration
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        String errorContent = null;
        try{
            errorContent = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return new RuntimeException(ioException);
        }
        return new MyFeignException(Result.error(500,errorContent));
    }
}