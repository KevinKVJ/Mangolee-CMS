package org.mangolee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Level;
import org.mangolee.entity.Result;
import org.mangolee.exception.BaseException;
import org.mangolee.service.LevelService;
import org.mangolee.utils.GlobalExceptionHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/level")
@Api(tags = "等级Controller")
public class LevelController {

    @Resource
    private LevelService levelService;

    @ApiOperation("获取所有等级")
    @GetMapping("/get/{level}")
    public Result<List<Level>> get(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level) {
        try {
            if (level == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(levelService.list());
        } catch (BaseException e) {
            return new GlobalExceptionHandler<List<Level>>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<List<Level>>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据等级查询")

    @ApiOperation("根据等级修改等级描述")

    @ApiOperation("插入新等级")

    @ApiOperation("删除等级")
}
