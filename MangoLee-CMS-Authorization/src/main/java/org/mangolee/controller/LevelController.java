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
    @GetMapping("/getbylevel")
    public Result<Level> getByLevel(
            @ApiParam(value = "等级", required = true)
            @RequestParam("level")
            @NotNull Integer level) {
        if (level == null) {
            return Result.error(400, "Level is null");
        }
        return Result.success(levelService.getOne(new QueryWrapper<Level>().eq("level", level)));
    }

    @ApiOperation("根据等级修改等级描述")
    @PutMapping("/update")
    public Result<Level> update(
            @ApiParam(value = "等级", required = true)
            @RequestParam("level")
            @NotNull Integer level,
            @ApiParam(value = "等级描述")
            @RequestParam("description")
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
    @PostMapping("/insert")
    public Result<Level> insert(
            @RequestBody Level newLevel) {
        if (newLevel == null) {
            return Result.error(400, "Level object is null");
        }
        if (newLevel.getLevel() == null) {
            return Result.error(400, "Level is null");
        }
        if (newLevel.getDescription() == null) {
            return Result.error(400, "Description is null");
        }
        if (levelService.getOne(new QueryWrapper<Level>().eq("level", newLevel.getLevel())) != null) {
            return Result.error(400, "Level already exists");
        }
        if (!levelService.save(newLevel)) {
            return Result.error(400, "Failed to insert the item");
        }
        return Result.success(newLevel);
    }

    @ApiOperation("删除等级")
    @DeleteMapping("/delete")
    public Result<Void> delete(
            @ApiParam(value = "等级", required = true)
            @RequestParam("level")
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
