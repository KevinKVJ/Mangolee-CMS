package org.mangolee.exception;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;


@ApiModel("自定义基础异常类")
@Getter
@Setter
public class BaseException extends RuntimeException {

    private Integer code;

    public BaseException(ResultEnum resultEnum) {
        this(resultEnum.getCode(), resultEnum.getMessage());
    }

    private BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
