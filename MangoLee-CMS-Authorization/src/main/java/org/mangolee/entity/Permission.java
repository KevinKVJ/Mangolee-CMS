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
}
