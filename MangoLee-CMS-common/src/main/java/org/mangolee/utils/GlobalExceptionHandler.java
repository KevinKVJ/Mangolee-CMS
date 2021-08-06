package org.mangolee.utils;

import org.mangolee.entity.Result;
import org.mangolee.exception.BaseException;
import org.mangolee.exception.MyFeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler<T> {

    // 处理业务异常
    @ExceptionHandler({BaseException.class})
    public Result<T> baseExceptionHandler(BaseException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    // 处理其他异常
    @SuppressWarnings("unchecked")
    @ExceptionHandler({Exception.class})
    public Result<T> exceptionHandler(Exception e) {
        return Result.BAD_REQUEST;
    }

    //处理远程调用异常
    @SuppressWarnings("unchecked")
    @ExceptionHandler({MyFeignException.class})
    public Result<T> feignExceptionHandler(Exception e) {
        return Result.INTERNAL_ERROR;
    }
}
