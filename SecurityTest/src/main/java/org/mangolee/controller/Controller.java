package org.mangolee.controller;


import org.mangolee.entity.Result;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.stream.Collectors;

@RestController
public class Controller {
    @GetMapping("/test")
    public Result<String> test(@RequestHeader MultiValueMap<String, String> headers)
    {
        headers.forEach((key, value) -> {
            System.out.println(String.format(
                    "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        return Result.success("Header info： "+headers.toString());
    }
}