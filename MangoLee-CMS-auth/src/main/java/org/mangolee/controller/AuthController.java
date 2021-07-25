package org.mangolee.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "授权Controller")
public class AuthController {

    @PostMapping("/login")
    public Object login(String username, String password) {
        //TODO 编写接口：传入Username和Password作为参数, 调用下层Service接口
        return null;
    }
}
