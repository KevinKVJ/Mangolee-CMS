package org.mangolee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "权限实体类")
public class Permission {

    // 主键ID
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    // 权限名称
    @ApiModelProperty(value = "权限角色名称")
    private String role;

    // 启用状态
    @ApiModelProperty(value = "启用状态")
    private Integer mount;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    // 修改时间
    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    // 乐观锁
    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    // 逻辑删除
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

}
