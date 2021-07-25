package org.mangolee.controller;

import io.swagger.annotations.Api;
import org.mangolee.service.UserpermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/userpermission")
@Api(tags = "用户权限Controller")
public class UserpermissionController {

    @Resource
    UserpermissionService userpermissionService;
}
