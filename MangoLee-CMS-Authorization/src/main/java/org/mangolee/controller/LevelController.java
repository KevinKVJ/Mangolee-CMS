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
            @ApiParam(value = "等级", required = true, example = "1")
            @RequestParam("level")
            @NotNull Integer level) {
        if (level == null) {
            return Result.error(400, "Level不能为空");
        }
        return Result.success(levelService.getOne(new QueryWrapper<Level>().eq("level", level)));
    }

    @ApiOperation("根据等级修改等级描述")
    @PutMapping("/update")
    public Result<Level> update(
            @ApiParam(value = "等级", required = true, example = "1")
            @RequestParam("level")
            @NotNull Integer level,
            @ApiParam(value = "等级描述", required = true, example = "Guest默认level为1")
            @RequestParam("description")
            @NotNull String description) {
        if (level == null) {
            return Result.error(400, "Level不能为空");
        }
        if (description == null) {
            return Result.error(400, "Description不能为空");
        }
        QueryWrapper<Level> queryWrapper = new QueryWrapper<Level>().eq("level", level);
        Level level1 = levelService.getOne(queryWrapper);
        if (level1 == null) {
            return Result.error(400, "Level不存在");
        }
        level1.setDescription(description);
        if (!levelService.update(level1, queryWrapper)) {
            return Result.error(400, "更新条目失败");
        }
        return Result.success(levelService.getOne(queryWrapper));
    }

    @ApiOperation("插入新等级")
    @PostMapping("/insert")
    public Result<Level> insert(
            @ApiParam(value = "等级", required = true)
            @RequestBody Level newLevel) {
        if (newLevel == null) {
            return Result.error(400, "Level对象不能为空");
        }
        if (newLevel.getLevel() == null) {
            return Result.error(400, "Level值不能为空");
        }
        if (newLevel.getDescription() == null) {
            return Result.error(400, "Description不能为空");
        }
        if (levelService.getOne(new QueryWrapper<Level>().eq("level", newLevel.getLevel())) != null) {
            return Result.error(400, "Level已经存在");
        }
        if (!levelService.save(newLevel)) {
            return Result.error(400, "插入条目失败");
        }
        return Result.success(newLevel);
    }

    @ApiOperation("删除等级")
    @DeleteMapping("/delete")
    public Result<Void> delete(
            @ApiParam(value = "等级", required = true, example = "1")
            @RequestParam("level")
            @NotNull Integer level
    ) {
        if (level == null) {
            return Result.error(400, "Level不能为空");
        }
        if (!levelService.remove(new QueryWrapper<Level>().eq("level", level))) {
            return Result.error(400, "删除条目失败");
        }
        return Result.success();
    }
}
