package com.abin.chatserver.common.domain.vo;

import com.abin.chatserver.common.domain.enums.ErrorEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }

    public BaseResponse(ErrorEnum errorEnum) {
        this(errorEnum.getCode(), null, errorEnum.getMessage());
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(200, null, "ok");
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "ok");
    }

    public static <T> BaseResponse<T> error(ErrorEnum errorEnum) {
        return new BaseResponse<>(errorEnum.getCode(), null, errorEnum.getMessage());
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }
}
