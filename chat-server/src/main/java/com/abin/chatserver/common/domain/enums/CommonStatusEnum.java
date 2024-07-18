package com.abin.chatserver.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 是否正常的通用枚举
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum CommonStatusEnum {
    NORMAL(0, "正常"),
    NOT_NORMAL(1, "不正常"),
    ;

    private final Integer status;
    private final String desc;

    private static final Map<Integer, CommonStatusEnum> cache;

    static {
        cache = Arrays.stream(CommonStatusEnum.values()).collect(Collectors.toMap(CommonStatusEnum::getStatus, Function.identity()));
    }

    public static CommonStatusEnum of(Integer type) {
        return cache.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? NORMAL.getStatus() : NOT_NORMAL.getStatus();
    }
}
