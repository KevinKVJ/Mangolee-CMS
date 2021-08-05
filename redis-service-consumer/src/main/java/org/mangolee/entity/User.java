package org.mangolee.entity;

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
    private Long    id;

    // 用户名
    @ApiModelProperty(value = "用户名", example = "Jack")
    private String  username;

    // 密码
    @ApiModelProperty(value = "加密密码")
    private String  password;

    // 邮箱
    @ApiModelProperty(value = "邮箱", example = "user@mangolee.com")
    private String email;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    // 修改时间
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime    gmtModified;

    // 乐观锁
    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    // 逻辑删除
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;
}
