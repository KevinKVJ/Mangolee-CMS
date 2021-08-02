package org.mangolee.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // 密码
    @ApiModelProperty(value = "加密密码")
    private String  password;

    // 邮箱
    @ApiModelProperty(value = "邮箱", example = "user@mangolee.com")
    private String email;
}
