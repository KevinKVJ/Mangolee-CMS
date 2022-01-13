package org.mangolee.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mangolee.Application;
import org.mangolee.entity.Result;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.stream.Collectors;

@RestController
public class Controller {
    private Logger logger = LoggerFactory.getLogger(Application.class);
    @GetMapping("/test")
    public Result<String> test(@RequestHeader MultiValueMap<String, String> headers)
    {
        headers.forEach((key, value) -> {
            System.out.println(String.format(
                    "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        return Result.success("Header info： "+headers.toString());
    }

    @GetMapping("/test2")
    public Result<String> test2()
    {
        logger.error("Hello logger");
        return Result.success("Success");
    }
}