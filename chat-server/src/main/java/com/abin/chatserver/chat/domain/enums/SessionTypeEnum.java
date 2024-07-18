package com.abin.chatserver.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum SessionTypeEnum {
    GROUP(1, "群聊"),
    SINGLE(2, "单聊");

    private final Integer type;

    private final String desc;

    private static final Map<Integer, SessionTypeEnum> cache;

    static {
        cache = Arrays.stream(SessionTypeEnum.values()).collect(Collectors.toMap(SessionTypeEnum::getType, Function.identity()));
    }

    public static SessionTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
