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
@ApiModel(value = "日志类")
public class Log {

    // 主键ID
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户主键ID
    @ApiModelProperty(value = "用户主键ID")
    private Long userId;

    // 操作IPv4
    @ApiModelProperty(value = "操作IPv4")
    private String ipAddress;

    // 日志信息
    @ApiModelProperty(value = "日志信息")
    private String message;

    // 状态码
    @ApiModelProperty(value = "状态码")
    private String statusCode;

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
