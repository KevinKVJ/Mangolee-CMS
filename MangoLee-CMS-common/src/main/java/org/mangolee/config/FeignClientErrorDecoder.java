package org.mangolee.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.mangolee.exception.MyFeignException;
import org.mangolee.utils.Result;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        try{
            String errorContent = Util.toString(response.body().asReader());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return new RuntimeException(ioException);
        }
        return new MyFeignException(Result.INTERNAL_ERROR);
    }
}