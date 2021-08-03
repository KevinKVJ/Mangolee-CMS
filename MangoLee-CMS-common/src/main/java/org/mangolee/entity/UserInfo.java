package org.mangolee.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户信息类")
public class UserInfo {
    // 主键ID
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long    id;

    // 用户名
    @ApiModelProperty(value = "用户名", example = "Jack")
    private String  username;

    // 邮箱
    @ApiModelProperty(value = "邮箱", example = "user@mangolee.com")
    private String email;

    // 角色权限
    @ApiModelProperty(value = "角色权限", example = "ADMIN")
    private String role;

    // UUID (保证token随机性)
    @ApiModelProperty(value = "UUID")
    private String uuid;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
}
