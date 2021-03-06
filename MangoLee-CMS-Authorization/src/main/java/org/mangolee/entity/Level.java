package org.mangolee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "等级实体类")
public class Level {
    // 等级
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "等级")
    private Integer level;
    // 等级描述
    @ApiModelProperty(value = "等级描述")
    private String description;
}
