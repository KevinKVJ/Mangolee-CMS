package org.mangolee.utils;

import org.mangolee.exception.BaseException;
import org.mangolee.exception.MyFeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler<T> {
    @Value("${spring.application.name}")
    private String appName;

    private String getMsgHead(Exception e)
    {
        return "Service["+appName+"] handled (" + e.getClass().getName() +") exception: ";
    }

    private String traceStack(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true);
        e.printStackTrace(ps);
        return new String(baos.toByteArray(),StandardCharsets.UTF_8);
    }

    private String defaultMsgBuilder(Exception e)
    {
        return getMsgHead(e)+e.getMessage();
    }

    // 处理业务异常
    @ExceptionHandler({BaseException.class})
    public Result<T> baseExceptionHandler(BaseException e) {
        return Result.error(e.getCode(), defaultMsgBuilder(e));
    }

    // 处理其他异常
    @SuppressWarnings("unchecked")
    @ExceptionHandler({Exception.class})
    public Result<T> exceptionHandler(Exception e) {
        return Result.error(400, defaultMsgBuilder(e));
    }

    //处理路径错误异常
    @SuppressWarnings("unchecked")
    @ExceptionHandler({NoHandlerFoundException.class})
    public Result<T> noFoundHandler(Exception e) {
        return Result.error(404, defaultMsgBuilder(e));
    }

    //处理远程调用异常
    @SuppressWarnings("unchecked")
    @ExceptionHandler({MyFeignException.class})
    public Result<T> feignExceptionHandler(Exception e) {
        return Result.error(500,defaultMsgBuilder(e));
    }
}
