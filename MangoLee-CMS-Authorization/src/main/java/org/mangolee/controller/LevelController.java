package org.mangolee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mangolee.entity.Level;
import org.mangolee.entity.Result;
import org.mangolee.service.LevelService;
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
        return Result.success(levelService.list());
    }

    @ApiOperation("查询单个等级")
    @GetMapping("/getbylevel/{level}")
    public Result<Level> getByLevel(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level) {
        if (level == null) {
            return Result.error(400, "Level is null");
        }
        return Result.success(levelService.getOne(new QueryWrapper<Level>().eq("level", level)));
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
        if (level == null) {
            return Result.error(400, "Level is null");
        }
        if (description == null) {
            return Result.error(400, "Description is null");
        }
        QueryWrapper<Level> queryWrapper = new QueryWrapper<Level>().eq("level", level);
        Level level1 = levelService.getOne(queryWrapper);
        if (level1 == null) {
            return Result.error(400, "Such level does not exist");
        }
        level1.setDescription(description);
        if (!levelService.update(level1, queryWrapper)) {
            return Result.error(400, "Failed to update the item");
        }
        return Result.success(levelService.getOne(queryWrapper));
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
        if (level == null) {
            return Result.error(400, "Level is null");
        }
        if (description == null) {
            return Result.error(400, "Description is null");
        }
        QueryWrapper<Level> queryWrapper = new QueryWrapper<Level>().eq("level", level);
        Level level1 = levelService.getOne(queryWrapper);
        if (level1 != null) {
            return Result.error(400, "Level already exists");
        }
        Level level2 = new Level();
        level2.setLevel(level);
        level2.setDescription(description);
        if (!levelService.save(level2)) {
            return Result.error(400, "Failed to insert the item");
        }
        return Result.success(level2);
    }

    @ApiOperation("删除等级")
    @DeleteMapping("/delete/{level}")
    public Result<Void> delete(
            @ApiParam(value = "等级", required = true)
            @PathVariable("level")
            @NotNull Integer level
    ) {
        if (level == null) {
            return Result.error(400, "Level is null");
        }
        if (!levelService.remove(new QueryWrapper<Level>().eq("level", level))) {
            return Result.error(400, "Failed to delete the item");
        }
        return Result.success();
    }
}
