package com.abin.chatserver.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OssTypeEnum {

    MINIO("minio", 1),
    ;

    private final String name;

    private final Integer type;

}
