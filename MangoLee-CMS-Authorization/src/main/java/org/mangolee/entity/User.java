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
@ApiModel(value = "用户实体类")
public class User {

    // 主键ID
    @ApiModelProperty(value = "主键ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户名
    @ApiModelProperty(value = "用户名", example = "Jack")
    private String username;

    // 密码
    @ApiModelProperty(value = "加密密码")
    private String password;

    // 邮箱
    @ApiModelProperty(value = "邮箱", example = "user@mangolee.com")
    private String email;

    // 角色权限
    @ApiModelProperty(value = "角色权限", example = "ADMIN")
    private String role;
}
