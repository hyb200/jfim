package com.abin.chatserver.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReadStatusEnum {

    UNREAD(1, "未读"),

    READ(2, "已读");

    private final Integer code;

    private final String desc;
}
