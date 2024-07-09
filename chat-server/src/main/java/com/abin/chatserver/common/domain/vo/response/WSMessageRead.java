package com.abin.chatserver.common.domain.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMessageRead {

    @Schema(name = "消息")
    private Long msgId;

    @Schema(name = "阅读人数（可能为0）")
    private Integer readCount;
}
