package org.mangolee.utils;

public class ResultUtils {

    /**
     * 有数据返回，但无异常的时候调用，将数据封装进result
     * 只要将数据传入success即可
     * 返回result类型
     */
    public static<T> Result<T> success(T object) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), object);
    }

    /**
     * 没有数据返回，并且无异常的时候调用
     * 返回result类型，data设置为null
     */
    public static<T> Result<T> success() {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 有异常的时候调用，默认不返回数据，只返回 code 与信息
     */
    public static<T> Result<T> error(ResultEnum resultEnum) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public static<T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
