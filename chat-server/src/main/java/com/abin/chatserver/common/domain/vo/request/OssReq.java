package com.abin.chatserver.common.domain.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OssReq {

    @Schema(description = "文件存储路径")
    private String filePath;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "请求的uid")
    private Long uid;

    @Schema(description = "自动生成地址")
    @Builder.Default
    private boolean autoPath = true;
}
