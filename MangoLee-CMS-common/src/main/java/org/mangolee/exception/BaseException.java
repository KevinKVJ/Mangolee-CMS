package org.mangolee.exception;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.mangolee.utils.Result;


@ApiModel("自定义基础异常类")
@Getter
@Setter
public class BaseException extends RuntimeException {

    private Integer code;

    public BaseException(Result result) {
        this(result.getCode(), result.getMessage());
    }

    private BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
