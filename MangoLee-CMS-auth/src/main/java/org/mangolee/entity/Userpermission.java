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
@ApiModel(value = "用户权限实体类")
public class Userpermission {

    // 主键ID
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户ID
    @ApiModelProperty(value = "用户ID")
    private Long userid;

    // 权限ID
    @ApiModelProperty(value = "权限ID")
    private Long permissionid;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    // 修改时间
    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime    gmtModified;

    // 乐观锁
    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    // 逻辑删除
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
