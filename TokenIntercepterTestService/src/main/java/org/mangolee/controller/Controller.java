package org.mangolee.controller;


import org.mangolee.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@RestController
public class Controller {
    @GetMapping("/test")
    public String test()
    {
        return "Test success";
    }
    @GetMapping("/abc")
    public String abc()
    {
        return "abc";
    }
    @GetMapping("/headerParam")
    public String headerParam(@RequestHeader String message)
    {
        return message.toString();
    }
}
