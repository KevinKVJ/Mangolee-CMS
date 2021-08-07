package org.mangolee.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("结果实体类")
@JSONType(orders = { "code", "message","data"})
@SuppressWarnings("unchecked")
public class Result<T> {

    @ApiModelProperty(value = "消息代码", example = "200")
    @JSONField(name = "code")
    private Integer code;

    @ApiModelProperty(value = "返回消息", example = "操作成功")
    @JSONField(name = "message")
    private String message;

    @ApiModelProperty(value = "返回数据")
    @JSONField(name = "data")
    private T data;

    public final static Result BAD_REQUEST = new Result(400, "无效请求", null);

    public final static Result UNAUTHORIZED = new Result(401, "未授权", null);

    public final static Result FORBIDDEN = new Result(403, "禁止访问", null);

    public final static Result NOT_FOUND = new Result(404, "找不到结果", null);

    public final static Result INTERNAL_ERROR = new Result(500, "内部错误", null);

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    public static <T> Boolean successful(Result<T> result) { return result.getCode().equals(200); }
}

