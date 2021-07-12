package org.mangolee.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("结果实体类")
public class Result<T>{

    @ApiModelProperty(value = "消息代码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "返回消息", example = "操作成功")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;
}

