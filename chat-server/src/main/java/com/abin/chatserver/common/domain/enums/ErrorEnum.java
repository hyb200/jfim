package com.abin.chatserver.common.domain.enums;

import lombok.Getter;

/**
 * 自定义错误码
 */
@Getter
public enum ErrorEnum {
    PARAM_ERROR(40001, "请求参数错误"),
    SYSTEM_ERROR(50001, "系统内部错误"),
    BUSINESS_ERROR(50002, "{}"),
    LOCK_ERROR(50003, "获取分布式锁失败");

    private final int code;

    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
