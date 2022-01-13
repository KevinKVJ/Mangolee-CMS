package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Level;
import org.mangolee.entity.Result;
import org.mangolee.exception.BaseException;
import org.mangolee.service.LevelService;
import org.mangolee.utils.GlobalExceptionHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/get")
    public Result<List<Level>> get() {
        try {
            return Result.success(levelService.list());
        } catch (BaseException e) {
            return new GlobalExceptionHandler<List<Level>>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<List<Level>>().exceptionHandler(e);
        }
    }

    @ApiOperation("查询单个等级")
    @GetMapping("/getbylevel/{level}")
    public Result<Level> getByLevel(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level) {
        try {
            if (level == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(levelService.getOne(new QueryWrapper<Level>().eq("level", level)));
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Level>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Level>().exceptionHandler(e);
        }
    }

    @ApiOperation("根据等级修改等级描述")
    @PutMapping("/update/{level}/{description}")
    public Result<Level> update(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level,
            @ApiParam(value = "等级描述")
            @PathVariable("description")
            @NotNull String description) {
        try {
            if (level == null || description == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            QueryWrapper<Level> queryWrapper = new QueryWrapper<Level>().eq("level", level);
            Level level1 = levelService.getOne(queryWrapper);
            level1.setDescription(description);
            if (!levelService.update(level1, queryWrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(levelService.getOne(queryWrapper));
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Level>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Level>().exceptionHandler(e);
        }
    }

    @ApiOperation("插入新等级")
    @PostMapping("/insert/{level}/{description}")
    public Result<Level> insert(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level,
            @ApiParam(value = "等级描述")
            @PathVariable("description")
            @NotNull String description) {
        try {
            if (level == null || description == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            QueryWrapper<Level> queryWrapper = new QueryWrapper<Level>().eq("level", level);
            Level level1 = levelService.getOne(queryWrapper);
            if (level1 != null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            Level level2 = new Level();
            level2.setLevel(level);
            level2.setDescription(description);
            if (!levelService.update(level2, queryWrapper)) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success(level2);
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Level>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Level>().exceptionHandler(e);
        }
    }

    @ApiOperation("删除等级")
    @DeleteMapping("/delete/{level}")
    public Result<Void> delete(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level
    ) {
        try {
            if (level == null) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            if (!levelService.remove(new QueryWrapper<Level>().eq("level", level))) {
                throw new BaseException(Result.BAD_REQUEST);
            }
            return Result.success();
        } catch (BaseException e) {
            return new GlobalExceptionHandler<Void>().baseExceptionHandler(e);
        } catch (Exception e) {
            return new GlobalExceptionHandler<Void>().exceptionHandler(e);
        }
    }
}
