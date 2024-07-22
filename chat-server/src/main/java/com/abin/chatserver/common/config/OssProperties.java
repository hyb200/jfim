package com.abin.chatserver.common.config;

import com.abin.chatserver.common.domain.enums.OssTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    Boolean enable;

    OssTypeEnum ossType;

    String endpoint;

    String accessKey;

    String secretKey;

    String bucketName;
}